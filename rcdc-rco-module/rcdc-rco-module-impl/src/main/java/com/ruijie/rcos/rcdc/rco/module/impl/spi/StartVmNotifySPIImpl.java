package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.codec.adapter.def.annotation.MaintainFilterAction;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.IpLimitAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.StartVmMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.DispatcherRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.StartVmDispatcherDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.exception.ShineUserNotLoginException;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.maintenance.MaintenanceModeValidator;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.StartVmRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/8/22
 *
 * @author hs
 */
@DispatcherImplemetion(ShineAction.START_VM)
@MaintainFilterAction
public class StartVmNotifySPIImpl implements CbbDispatcherHandlerSPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartVmNotifySPIImpl.class);

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private StartVmByStateHandler startVmByStateHandler;

    @Autowired
    private MaintenanceModeValidator maintenanceModeValidator;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private IpLimitAPI ipLimitAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;


    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    private Map<UUID, Lock> startVMMap = Maps.newConcurrentMap();

    /**
     * 分配50个线程数处理启动虚机
     */
    private static final ExecutorService THREAD_POOL = ThreadExecutors.newBuilder("startVMThreadHandler").maxThreadNum(50).queueSize(1).build();

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request is null");

        LOGGER.info("接收到启动虚机的消息，请求参数为:{}", JSON.toJSONString(request));
        THREAD_POOL.execute(() -> {

            DispatcherRequestDTO<StartVmRequest> dispatcherRequestDTO = buildDispatcherRequestDTO(request);

            if (!notifyStartVm(dispatcherRequestDTO)) {
                // 启动虚机失败，相关数据回滚
                startVmErrorRollback(dispatcherRequestDTO);
            }
        });
    }

    private DispatcherRequestDTO<StartVmRequest> buildDispatcherRequestDTO(CbbDispatcherRequest request) {
        StartVmRequest startVmReq;
        try {
            startVmReq = JSON.parseObject(request.getData(), StartVmRequest.class);
        } catch (Throwable e) {
            throw new IllegalArgumentException("解析启动虚机报文失败，请检查报文是否正确", e);
        }
        DispatcherRequestDTO<StartVmRequest> dispatcherRequestDTO = new DispatcherRequestDTO<>();
        dispatcherRequestDTO.setRequestParam(startVmReq);
        dispatcherRequestDTO.setDispatcherKey(request.getDispatcherKey());
        dispatcherRequestDTO.setTerminalId(request.getTerminalId());
        dispatcherRequestDTO.setRequestId(request.getRequestId());
        dispatcherRequestDTO.setData(request.getData());
        dispatcherRequestDTO.setNewConnection(request.getNewConnection());
        return dispatcherRequestDTO;
    }

    private boolean notifyStartVm(DispatcherRequestDTO<StartVmRequest> request) {

        boolean isInMaintenance = maintenanceModeValidator.validate();
        if (isInMaintenance) {
            boolean isIDVTerminal = checkTerminalPlatform(request.getTerminalId());
            if (!isIDVTerminal) {
                LOGGER.warn("[{}]启动虚机失败,系统维护模式[{}],是否IDV终端[{}]", request.getTerminalId(), isInMaintenance, isIDVTerminal);
                String errMsg = LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_SHINE_START_VM_UNDER_MAINTENANCE_ERR);
                maintenanceModeValidator.responseUnderMaintenanceMessage(request, StartVmMessageCode.CODE_ERR_UNDER_MAINTENANCE, errMsg);
                return false;
            }
        }

        StartVmRequest startVmRequest = request.getRequestParam();
        CbbDeskDTO cbbDeskDTO;
        try {
            UUID deskId = startVmRequest.getId();
            cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
        } catch (BusinessException e) {
            responseErrorMessage(request, StartVmMessageCode.CODE_ERR_OTHER, e.getI18nMessage());
            return false;
        }


        if (Boolean.TRUE.equals(cbbDeskDTO.getIsOpenDeskMaintenance())) {
            String errMsg = LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_SHINE_START_VM_UNDER_DESKTOP_MAINTENANCE_ERR);
            maintenanceModeValidator.responseUnderMaintenanceMessage(request, StartVmMessageCode.CODE_ERR_UNDER_DESKTOP_MAINTENANCE, errMsg);
            return false;
        }

        // IP网段限制校验
        if (!checkIpLimit(request, cbbDeskDTO)) {
            return false;
        }

        // 协议代理校验
        if (!checkEnableAgreementAgency(request, cbbDeskDTO.getStrategyId())) {
            return false;
        }

        // Hest桌面校验
        if (!checkHasSupportHest(request, cbbDeskDTO)) {
            return false;
        }


        LOGGER.info("当前非维护模式,允许任何终端启动虚机,reqeust[{}]", JSON.toJSONString(request));
        try {
            doStartVMDispatch(request, cbbDeskDTO);
            return true;
        } catch (ShineUserNotLoginException e) {
            LOGGER.error("执行启动虚机异常, 用户未登录", e);
            responseErrorMessage(request, StartVmMessageCode.USER_NOT_LOGIN, e.getI18nMessage());
            return false;
        } catch (BusinessException e) {
            LOGGER.error("执行启动虚机异常", e);
            if (com.ruijie.rcos.rcdc.hciadapter.module.def.BusinessKey.RCDC_HCIADAPTER_VM_WAKE_ERROR_BY_RESOURCE_INSUFFICIENTLY.equals(e.getKey())) {
                responseErrorMessage(request, StartVmMessageCode.CODE_ERR_WAKE_UP_FAIL_BY_RESOURCE_INSUFFICIENT, e.getI18nMessage());
                return false;
            }
            responseErrorMessage(request, StartVmMessageCode.CODE_ERR_OTHER, e.getI18nMessage());
            return false;
        } catch (Exception e) {
            LOGGER.error("执行启动虚机异常", e);
            if (e instanceof RuntimeException && e.getCause() instanceof BusinessException) {
                BusinessException businessException = (BusinessException) e.getCause();
                responseErrorMessage(request, StartVmMessageCode.CODE_ERR_OTHER, businessException.getI18nMessage());
                return false;
            }
            String errMsg = LocaleI18nResolver.resolve(BusinessKey.RCD_RCO_SHINE_START_VM_SYSTEM_ERR);
            responseErrorMessage(request, StartVmMessageCode.CODE_ERR_OTHER, errMsg);
            return false;
        }

    }

    private boolean checkHasSupportHest(DispatcherRequestDTO<StartVmRequest> request, CbbDeskDTO deskDTO) {
        if (StringUtils.isBlank(request.getData())) {
            return true;
        }
        StartVmRequest startVmRequest = request.getRequestParam();

        boolean isAndroid = BooleanUtils.isTrue(startVmRequest.getAndroid());
        boolean isHestDesk = deskDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE || deskDTO.getDeskType() == CbbCloudDeskType.THIRD;
        if (isAndroid && isHestDesk) {
            LOGGER.info("安卓终端不支持带内HEST，无法启动桌面;云桌面id:{}", startVmRequest.getId());
            String errMsg = LocaleI18nResolver.resolve(BusinessKey.RCD_RCO_SHINE_START_VM_HEST_ERR);
            responseErrorMessage(request, StartVmMessageCode.CODE_ERR_OTHER, errMsg);
            return false;
        }
        return true;
    }

    private boolean checkTerminalPlatform(String terminalId) {
        try {
            CbbTerminalBasicInfoDTO terminalInfo = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            CbbTerminalPlatformEnums terminalPlatform = terminalInfo.getTerminalPlatform();
            if (terminalPlatform == CbbTerminalPlatformEnums.IDV || terminalPlatform == CbbTerminalPlatformEnums.VOI) {
                return true;
            }
        } catch (BusinessException e) {
            LOGGER.info("查询终端类型失败,禁止用户登陆虚机");
        }
        return false;
    }

    private boolean desktopMaintenanceModeValidator(DispatcherRequestDTO<StartVmRequest> dispatcherRequestDTO) {
        StartVmRequest startVmReq = dispatcherRequestDTO.getRequestParam();

        try {
            if (startVmReq != null) {
                CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(startVmReq.getId());
                if (cloudDesktopDetailDTO != null) {
                    LOGGER.warn("[{}]终端启动[{}]云桌面,云桌面维护模式状态[{}]", dispatcherRequestDTO.getTerminalId(), startVmReq.getId(),
                            cloudDesktopDetailDTO.getIsOpenDeskMaintenance());
                    return cloudDesktopDetailDTO.getIsOpenDeskMaintenance();
                }
            }
            return false;
        } catch (BusinessException e) {
            LOGGER.error("查询云桌面信息[{}]失败", startVmReq.getId());
            return false;
        }
    }

    /**
     * IP网段限制校验
     *
     * @param request request
     * @param deskDTO deskDTO
     * @return true 允许打开虚机，false不允许打开虚机
     */
    private boolean checkIpLimit(DispatcherRequestDTO<StartVmRequest> request, CbbDeskDTO deskDTO) {
        try {

            LOGGER.info("VDI网段限制已开启，开始进行IP校验");
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
            LOGGER.info("终端IP为：[{}]", terminalBasicInfoDTO.getIp());
            boolean isIpLimited = ipLimitAPI.isIpLimited(terminalBasicInfoDTO.getIp(), deskDTO);
            LOGGER.info("IP[{}]是否被限制的信息为：[{}]", terminalBasicInfoDTO.getIp(), isIpLimited);
            if (!isIpLimited) {
                return true;
            }
            LOGGER.info("VDI[{}]的IP为[{}}，已被限制登录", terminalBasicInfoDTO.getTerminalName(), terminalBasicInfoDTO.getIp());
            String errMsg = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_SHINE_START_VM_IP_LIMIT, terminalBasicInfoDTO.getIp());
            responseErrorMessage(request, CommonMessageCode.CODE_ERR_OTHER, errMsg);
            return false;
        } catch (BusinessException e) {
            LOGGER.error("查询信息[" + request.getTerminalId() + "]失败", e);
            responseErrorMessage(request, StartVmMessageCode.CODE_ERR_OTHER, e.getI18nMessage());
            return false;
        }
    }

    /**
     * 检查虚机配置是否支持协议代理打开
     *
     * @param request request
     * @param deskStrategyId deskStrategyId
     * @return true 允许打开，false不允许打开
     */
    private boolean checkEnableAgreementAgency(DispatcherRequestDTO<StartVmRequest> request, UUID deskStrategyId) {
        if (StringUtils.isBlank(request.getData())) {
            return true;
        }
        StartVmRequest startVmRequest = request.getRequestParam();
        boolean enableAgreementAgency = BooleanUtils.isTrue(startVmRequest.getEnableAgreementAgency());

        try {
            CbbDeskStrategyDTO deskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(deskStrategyId);

            boolean enableFlag = Boolean.TRUE.equals(deskStrategy.getEnableAgreementAgency());

            boolean enableForceUseAgreementAgency = Boolean.TRUE.equals(deskStrategy.getEnableForceUseAgreementAgency());

            // 开关开启且不是强制,终端sag跟内网都能访问
            if (enableFlag && !enableForceUseAgreementAgency) {
                // 客户端未开启代理，允许打开
                return true;
            }

            // 开关开启并且是强制只允许终端通过SAG访问
            if (enableFlag && enableForceUseAgreementAgency && enableAgreementAgency) {
                // 策略支持协议代理打开
                return true;
            }

            // 开关关闭只允许终端通过内网访问
            if (!enableFlag && !enableAgreementAgency) {
                // 策略支持协议代理打开
                return true;
            }

            LOGGER.info("协议代理状态不一致，无法启动桌面;云桌面id:{}", startVmRequest.getId());
            String errMsg;
            if (enableForceUseAgreementAgency) {
                errMsg = LocaleI18nResolver.resolve(BusinessKey.RCD_RCO_SHINE_START_VM_AGREEMENT_AGENCY_FORCE_ERR);
            } else {
                errMsg = LocaleI18nResolver.resolve(BusinessKey.RCD_RCO_SHINE_START_VM_AGREEMENT_AGENCY_ERR);
            }

            responseErrorMessage(request, StartVmMessageCode.CODE_ERR_OTHER, errMsg);
            return false;
        } catch (BusinessException e) {
            LOGGER.error("查询云桌面信息[" + startVmRequest.getId() + "]失败", e);
            responseErrorMessage(request, StartVmMessageCode.CODE_ERR_OTHER, e.getI18nMessage());
            return false;
        }
    }


    private void doStartVMDispatch(DispatcherRequestDTO<StartVmRequest> request, CbbDeskDTO deskDTO) throws Exception {
        StartVmRequest startVmReq = request.getRequestParam();

        if (startVmReq != null) {
            if (!userDesktopMgmtAPI.isAllowDesktopLogin(deskDTO.getStrategyId(), deskDTO.getName(), Boolean.FALSE, deskDTO.getDeskType().name())) {
                String errMsg = LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_CLOUDDESKTOP_LOGIN_TIME_LIMIT);
                responseErrorMessage(request, StartVmMessageCode.CODE_ERR_OTHER, errMsg);
                return;
            }
            try {
                // todo 存在内存泄漏问题：桌面开机过，后续被删除，内存无法释放
                getCreateLock(startVmReq.getId()).lock();
                LOGGER.info("启动云桌面加锁，云桌面id:{}，终端id：{}", startVmReq.getId(), request.getTerminalId());
                startVM(request, deskDTO);
            } finally {
                LOGGER.info("启动云桌面结束加锁，云桌面id:{}, 终端id：{}", startVmReq.getId(), request.getTerminalId());
                getCreateLock(startVmReq.getId()).unlock();
            }
        }

    }

    private Lock getCreateLock(UUID desktopId) {
        return startVMMap.computeIfAbsent(desktopId, id -> new ReentrantLock());
    }

    private void startVM(DispatcherRequestDTO<StartVmRequest> request, CbbDeskDTO deskDTO) throws Exception {
        StartVmRequest startVmRequest = request.getRequestParam();
        StartVmDispatcherDTO startVmDispatcherDTO = new StartVmDispatcherDTO();
        startVmDispatcherDTO.setDispatcherRequest(request);
        startVmDispatcherDTO.setDeskState(deskDTO.getDeskState());
        startVmDispatcherDTO.setDesktopId(startVmRequest.getId());
        startVmDispatcherDTO.setSupportCrossCpuVendor(startVmRequest.getSupportCrossCpuVendor());
        startVmByStateHandler.execute(startVmDispatcherDTO);
    }

    private void startVmErrorRollback(DispatcherRequestDTO<StartVmRequest> dispatcherRequestDTO) {
        StartVmRequest startVmReq = dispatcherRequestDTO.getRequestParam();
        userDiskMgmtAPI.unbindUserAndDesktopAndDiskRelation(startVmReq.getId());
    }

    private void responseErrorMessage(CbbDispatcherRequest request, int code, String errMsg) {
        try {
            shineMessageHandler.responseMessage(request, code, errMsg);
            LOGGER.info("应答报文给终端[{}]成功，应答状态码：{},应答内容：{}", request.getTerminalId(), code, errMsg);
        } catch (Exception e) {
            LOGGER.error("应答报文给终端[" + request.getTerminalId() + "]失败，应答状态码：" + code + "应答内容：" + errMsg, e);
        }
    }
}
