package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolDashboardAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.IpLimitAPI;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.constants.DesktopPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolAssignResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.PoolDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.AllotDesktopPoolFaultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.RelateTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.request.CreateConnectFaultLogRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.AllotDeskErrorCodeMessageConverter;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.DesktopPoolAssignDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.StartVmMessageCode;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.modulekit.api.comm.SpiCustomThreadPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

;

/**
 * Description: 获取池桌面资源
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/12
 *
 * @author linke
 */
@DispatcherImplemetion(ShineAction.GET_DESKTOP_POOL_RESOURCE)
@SpiCustomThreadPoolConfig(threadPoolName = "desktop_pool_resource_pool")
public class GetDesktopPoolResourceSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetDesktopPoolResourceSPIImpl.class);

    private static final Integer SUCCEED = 0;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private IacUserMgmtAPI userAPI;

    @Autowired
    private DesktopPoolUserMgmtAPI desktopPoolUserMgmtAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private UserLoginSession userLoginSession;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private DesktopPoolDashboardAPI desktopPoolDashboardAPI;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private IpLimitAPI ipLimitAPI;

    @Autowired
    private DesktopPoolService desktopPoolService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request不能为null");

        LOGGER.info("终端请求分配池桌面报文:terminalId:{};data:{}", request.getTerminalId(), request.getData());
        DesktopPoolAssignDTO desktopPoolAssignDTO;
        try {
            desktopPoolAssignDTO = shineMessageHandler.parseObject(request.getData(), DesktopPoolAssignDTO.class);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("接收到的分配池桌面报文解析错误，请检查报文是否正确，request[%s]",
                    JSON.toJSONString(request)), e);
        }

        UserInfo userInfo = userLoginSession.getLoginUserInfo(request.getTerminalId());

        if (userInfo == null) {
            LOGGER.error("终端[{}]未登录用户", request.getTerminalId());
            // 明确用户未登录
            responseError(request, StartVmMessageCode.USER_NOT_LOGIN);
            return;
        }
        desktopPoolAssignDTO.setUserName(userInfo.getUserName());
        IacUserDetailDTO cbbUserDetailDTO;
        CbbDesktopPoolDTO desktopPoolDTO;
        try {
            cbbUserDetailDTO = userAPI.getUserByName(desktopPoolAssignDTO.getUserName());
            desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolAssignDTO.getDesktopPoolId());
        } catch (Exception e) {
            responseMessage(request, DesktopPoolConstants.DESKTOP_POOL_ASSIGN_ERROR, desktopPoolAssignDTO.getUserName());
            LOGGER.error("桌面池分配桌面异常, desktopPoolAssignDTO[{}]", JSON.toJSONString(desktopPoolAssignDTO), e);
            return;
        }

        // 终端IP限制
        if (checkTerminalIpLimit(desktopPoolDTO, request, cbbUserDetailDTO)) {
            return;
        }
        // 分配桌面
        assignDesktopPool(request, cbbUserDetailDTO, desktopPoolDTO);
    }

    private boolean checkTerminalIpLimit(CbbDesktopPoolDTO desktopPoolDTO, CbbDispatcherRequest request, IacUserDetailDTO cbbUserDetailDTO) {
        // 终端IP限制
        try {
            PoolDesktopInfoDTO userBindDesktopInfo = null;
            if (desktopPoolDTO.getPoolModel() == CbbDesktopPoolModel.STATIC) {
                // 获取用户已绑定的桌面
                userBindDesktopInfo = desktopPoolService.getUserBindPoolDesktop(cbbUserDetailDTO.getId(), desktopPoolDTO.getId());
            }
            ViewTerminalEntity viewTerminalEntity = terminalService.getViewByTerminalId(request.getTerminalId());
            UUID strategyId = Objects.nonNull(userBindDesktopInfo) ? userBindDesktopInfo.getStrategyId() : desktopPoolDTO.getStrategyId();
            if (ipLimitAPI.isIpLimitedByDeskStrategy(desktopPoolDTO.getPoolType(), viewTerminalEntity.getIp(), strategyId)) {
                String errMsg = LocaleI18nResolver.resolve(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_SHINE_START_VM_IP_LIMIT,
                        viewTerminalEntity.getIp());
                responseErrorMessage(request, CommonMessageCode.CODE_ERR_OTHER, errMsg);
                return true;
            }
        } catch (BusinessException e) {
            LOGGER.error("查询信息终端[{}],桌面池[{}]失败", request.getTerminalId(), desktopPoolDTO.getId(), e);
            responseErrorMessage(request, StartVmMessageCode.CODE_ERR_OTHER, e.getI18nMessage());
            return true;
        }
        return false;
    }

    private void assignDesktopPool(CbbDispatcherRequest request, IacUserDetailDTO cbbUserDetailDTO, CbbDesktopPoolDTO desktopPoolDTO) {
        try {
            DesktopPoolAssignResultDTO assignResultDTO = desktopPoolUserMgmtAPI.assignDesktop(cbbUserDetailDTO.getId(), desktopPoolDTO.getId());
            int allotCode = assignResultDTO.getCode();
            if (allotCode != SUCCEED) {
                // 记录分配失败日志
                AllotDesktopPoolFaultTypeEnum faultType = AllotDeskErrorCodeMessageConverter.getAllocateFaultTypeByErrorCode(allotCode);
                String faultDesc = AllotDeskErrorCodeMessageConverter.getAllocateFaultDescByErrorCode(allotCode);
                recordConnectFailLog(cbbUserDetailDTO, desktopPoolDTO, faultType, faultDesc);
                // 用户分配池桌面失败,返回错误码
                responseMessage(request, assignResultDTO, cbbUserDetailDTO.getUserName());
                desktopPoolUserMgmtAPI.saveAssignFailWarnLog(cbbUserDetailDTO.getUserName(), desktopPoolDTO);
                return;
            }
            responseSuccessContent(request, cbbUserDetailDTO.getUserName(), assignResultDTO);
        } catch (BusinessException e) {
            // 记录分配失败日志
            recordConnectFailLog(cbbUserDetailDTO, desktopPoolDTO, AllotDesktopPoolFaultTypeEnum.NOT_ENOUGH_RESOURCE,
                    e.getI18nMessage());
            responseMessage(request, DesktopPoolConstants.DESKTOP_POOL_ASSIGN_ERROR, cbbUserDetailDTO.getUserName());
            LOGGER.error("桌面池分配桌面异常, CbbDispatcherRequest[{}]", JSON.toJSONString(request), e);
        } catch (Exception e) {
            responseMessage(request, DesktopPoolConstants.DESKTOP_POOL_ASSIGN_ERROR, cbbUserDetailDTO.getUserName());
            String errMessage = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_DESKTOP_POOL_OTHER_ALLOCATE_EXCEPTION);
            // 记录分配失败日志
            recordConnectFailLog(cbbUserDetailDTO, desktopPoolDTO, AllotDesktopPoolFaultTypeEnum.OTHER_FAULT, errMessage);
            LOGGER.error("桌面池分配桌面异常, CbbDispatcherRequest[{}]", JSON.toJSONString(request), e);
        }
    }

    private void recordConnectFailLog(IacUserDetailDTO userDetailDTO, CbbDesktopPoolDTO desktopPoolDTO, AllotDesktopPoolFaultTypeEnum faultType,
                                      String errorMessage) {
        CreateConnectFaultLogRequest request = new CreateConnectFaultLogRequest();

        request.setUserId(userDetailDTO.getId());
        request.setUserName(userDetailDTO.getUserName());
        request.setDesktopPoolName(desktopPoolDTO.getName());
        request.setDesktopPoolType(DesktopPoolType.convertToPoolDeskType(desktopPoolDTO.getPoolModel()));
        request.setCbbDesktopPoolType(desktopPoolDTO.getPoolType());
        request.setRelatedType(RelateTypeEnum.DESKTOP_POOL);
        request.setRelatedId(desktopPoolDTO.getId());
        request.setFaultType(faultType);
        request.setFaultDesc(errorMessage);
        UUID userGroupId = userDetailDTO.getGroupId();
        request.setUserGroupId(userGroupId);
        String groupName = userGroupId.toString();
        IacUserGroupDetailDTO userGroupDetail;
        try {
            userGroupDetail = cbbUserGroupAPI.getUserGroupDetail(userGroupId);
            groupName = userGroupDetail.getName();
        } catch (BusinessException e) {
            LOGGER.warn("查询用户组[" + userGroupId + "]信息异常，e=", e);
        }
        request.setUserGroupName(groupName);

        desktopPoolDashboardAPI.recordConnectFaultLog(request);
    }

    /**
     * 应答失败消息
     *
     * @param request shine请求消息对象
     * @param responseCode 应答code
     * @param userName 用户名字
     */
    private void responseMessage(CbbDispatcherRequest request, int responseCode, String userName) {
        try {
            shineMessageHandler.response(request, responseCode);
            LOGGER.info("应答用户[{}]获取池桌面资源,terminalId={}，应答状态码={}", userName, request.getTerminalId(), responseCode);
        } catch (Exception e) {
            LOGGER.error(String.format("应答用户[%s]获取池桌面资源失败，terminalId[%s]，应答状态[%s]", userName, request.getTerminalId(),
                    responseCode), e);
        }
    }

    /**
     * 应答失败消息
     *
     * @param request shine请求消息对象
     * @param assignResultDTO 应答内容
     * @param userName 用户名字
     */
    private void responseMessage(CbbDispatcherRequest request, DesktopPoolAssignResultDTO assignResultDTO, String userName) {
        int responseCode = assignResultDTO.getCode();
        try {
            shineMessageHandler.responseContent(request, responseCode, assignResultDTO);
            LOGGER.info("应答用户[{}]获取池桌面资源,terminalId={}，应答状态码={}", userName, request.getTerminalId(), responseCode);
        } catch (Exception e) {
            LOGGER.error(String.format("应答用户[%s]获取池桌面资源失败，terminalId[%s]，应答状态[%s]", userName, request.getTerminalId(),
                    responseCode), e);
        }
    }

    /**
     * 应答成功消息
     *
     * @param request shine请求消息对象
     * @param resultDTO 应答数据
     * @param userName 用户名
     */
    private void responseSuccessContent(CbbDispatcherRequest request, String userName, DesktopPoolAssignResultDTO resultDTO) {
        try {
            shineMessageHandler.responseSuccessContent(request, resultDTO);
            LOGGER.info("应答用户[{}]获取池桌面资源, terminalId={}，result={}", userName, request.getTerminalId(), JSON.toJSONString(resultDTO));
        } catch (Exception e) {
            LOGGER.error(String.format("应答用户[%s]获取池桌面资源失败，terminalId[%s]", userName, request.getTerminalId()), e);
        }
    }

    private void responseError(CbbDispatcherRequest request) {
        responseError(request, CommonMessageCode.CODE_ERR_OTHER);
    }

    private void responseError(CbbDispatcherRequest request, Integer resultCode) {
        try {
            // 返回错误消息给shine
            shineMessageHandler.response(request, resultCode);
        } catch (Exception e) {
            LOGGER.error(String.format("终端[%s]绑定桌面池资源应答消息失败，request[%s]", request.getTerminalId(), JSON.toJSONString(request)), e);
        }
    }

    private void responseErrorMessage(CbbDispatcherRequest request, int code, String errMsg) {
        try {
            shineMessageHandler.responseMessage(request, code, errMsg);
        } catch (Exception e) {
            LOGGER.error("应答报文给终端[{}]失败，应答状态码：[{}] 应答内容：[{}]", request.getTerminalId(), code, errMsg, e);
        }
    }
}
