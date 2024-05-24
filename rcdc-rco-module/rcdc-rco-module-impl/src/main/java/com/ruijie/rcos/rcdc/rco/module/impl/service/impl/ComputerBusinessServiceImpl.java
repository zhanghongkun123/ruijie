package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbOneAgentTcpSendAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CdcNotifyOaDisJoinHostRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOneAgentConnectTypeEnums;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ComputerWorkModelEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OneAgentConnectTypeEnums;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaTrusteeshipHostAPI;
import com.ruijie.rcos.rcdc.rco.module.common.connect.SessionManager;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoUserDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.computer.dto.NotifyComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.computer.enums.RcoComputerActionNotifyEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.computer.service.NotifyComputerChangeService;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.ComputerNetInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.ComputerReportSystemInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.response.GetComputerInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api.ComputerTcpAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api.DesktopUserOpTcpAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.computer.ComputerBaseInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.CancelRequestRemoteAssistSrcTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.message.computer.QueryAssistantVersionResponseContent;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.request.CancelRequestRemoteAssistRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.FileUtil;
import com.ruijie.rcos.rcdc.terminal.module.def.PublicBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.IPv4Util;
import com.ruijie.rcos.sk.base.validator.ValidatorUtil;
import com.ruijie.rcos.sk.connectkit.api.tcp.session.Session;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.util.*;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/30 15:16
 *
 * @author ketb
 */
@Service
public class ComputerBusinessServiceImpl implements ComputerBusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerBusinessServiceImpl.class);

    @Autowired
    private ComputerBusinessDAO computerBusinessDAO;

    @Autowired
    private CbbTerminalOperatorAPI terminalOperatorAPI;

    @Autowired
    private NotifyComputerChangeService notifyComputerChangeService;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;


    @Autowired
    private DesktopUserOpTcpAPI desktopUserOpTcpAPI;

    @Autowired
    private ComputerTcpAPI computerTcpAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private RcaTrusteeshipHostAPI rcaTrusteeshipHostAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private CbbOneAgentTcpSendAPI cbbOneAgentTcpSendAPI;

    private static final String THREAD_POOL_NAME = "desk-delete-notify-agent-oa-op";

    private static final int MAX_THREAD_NUM = 30;

    private static final int QUEUE_SIZE = 1000;

    private static final int FAIL_TRY_COUNT = 3;

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(THREAD_POOL_NAME).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    @Override
    public void saveComputerInfo(ComputerBaseInfoDTO baseInfoDTO) throws BusinessException {
        Assert.notNull(baseInfoDTO, "baseInfoDTO is not be null");
        LOGGER.info("save computer info");
        ComputerEntity computerEntity = computerBusinessDAO.findComputerEntityByMac(baseInfoDTO.getMac());
        boolean isCreate = false;
        if (computerEntity == null) {
            computerEntity = new ComputerEntity();
            computerEntity.setId(UUID.randomUUID());
            computerEntity.setTerminalGroupId(Constants.DEFAULT_TERMINAL_GROUP_UUID);
            computerEntity.setType(ComputerTypeEnum.PC);
            computerEntity.setCreateTime(new Date());
            isCreate = true;
        } else {
            if (computerEntity.getType() == ComputerTypeEnum.THIRD) {
                throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_IS_THIRD,baseInfoDTO.getMac());
            }
        }

        computerEntity.setState(ComputerStateEnum.ONLINE);
        BeanUtils.copyProperties(baseInfoDTO, computerEntity);
        if (isCreate) {
            create(computerEntity);
        } else {
            update(computerEntity, 0);
        }
    }

    @Override
    public void relieveFault(UUID computerId) throws BusinessException {
        Assert.notNull(computerId, "computerId不能为空");

        ComputerEntity computerEntity = computerBusinessDAO.findComputerEntityById(computerId);
        if (computerEntity == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_NOT_FOUND_COMPUTER, computerId.toString());
        }
        if (!computerEntity.getFaultState()) {
            LOGGER.info("computer [{}] is not in fault, not need relieve fault", computerId);
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_NO_FAULT, computerEntity.getName());
        }
        // 向终端发送解除故障消息
        try {
            LOGGER.info("start relieve computer [{}] fault", computerEntity.getMac());
            terminalOperatorAPI.relieveFault(com.ruijie.rcos.rcdc.terminal.module.def.constants.Constants.PC_FLAG + computerEntity.getMac(),
                    new CancelRequestRemoteAssistRequest(CancelRequestRemoteAssistSrcTypeEnum.ADMIN));
            computerEntity.setState(ComputerStateEnum.ONLINE);
        } catch (Exception e) {
            if (e.getMessage().equals(PublicBusinessKey.RCDC_TERMINAL_OFFLINE)) {
                computerEntity.setState(ComputerStateEnum.OFFLINE);
            } else {
                throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_RELIEVE_FAULT_FAIL_REFRESH, e);
            }
            LOGGER.error("send relieve fault msg to <{}> fail", computerId);
        }
        computerEntity.setFaultTime(new Date());
        computerEntity.setFaultState(false);
        computerEntity.setFaultDescription("");
        update(computerEntity, 0);
        LOGGER.info("relieve computer fault success id [{}],mac [{}]", computerEntity.getId(), computerEntity.getMac());
    }

    /**
     * @param responseContent
     */
    @Override
    public void getAssistantVersion(QueryAssistantVersionResponseContent responseContent) {
        Assert.notNull(responseContent, "responseContent is not be null");
        File file = new File(Constants.ASSISTANT_APP_COMPONENT_PATH, Constants.ASSISTANT_APP_CONFIG_FILE_NAME);
        if (!file.exists()) {
            // 需要赋空值，不然客户端会接收不到这些字段
            responseContent.setMainVersion(StringUtils.EMPTY);
            responseContent.setMinorVersion(StringUtils.EMPTY);
            responseContent.setThreeVersion(StringUtils.EMPTY);
            responseContent.setFourVersion(StringUtils.EMPTY);
            return;
        }

        Properties verIni = FileUtil.fillProperties(Constants.ASSISTANT_APP_COMPONENT_PATH, Constants.ASSISTANT_APP_CONFIG_FILE_NAME);
        responseContent.setMainVersion(verIni.getProperty(Constants.MAIN_VERSION_PROPERTIES));
        responseContent.setMinorVersion(verIni.getProperty(Constants.MINOR_VERSION_PROPERTIES));
        responseContent.setThreeVersion(verIni.getProperty(Constants.THREE_VERSION_PROPERTIES));
        responseContent.setFourVersion(verIni.getProperty(Constants.FOUR_VERSION_PROPERTIES));
    }

    @Override
    public ComputerStateEnum getComputerState(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId is not be null");
        ComputerEntity entity = computerBusinessDAO.findComputerEntityById(deskId);
        if (entity == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_NOT_FOUND_COMPUTER_NAME);
        }
        return entity.getState();
    }

    @Override
    public ComputerEntity getComputerById(UUID deskId) {
        Assert.notNull(deskId, "deskId is not be null");
        return computerBusinessDAO.findComputerEntityById(deskId);
    }

    @Override
    public ComputerEntity getComputerByTerminalId(String terminalId) {
        Assert.notNull(terminalId, "terminalId is not be null");
        return computerBusinessDAO.findByTerminalId(UUID.fromString(terminalId));
    }

    @Override
    public void updateComputerSystemInfo(String terminalId, ComputerReportSystemInfoDTO reportSystemInfoDTO) {
        Assert.notNull(terminalId, "terminalId is not be null");
        Assert.notNull(reportSystemInfoDTO, "reportSystemInfoDTO is not be null");
        ComputerEntity computerEntity = computerBusinessDAO.findByTerminalId(UUID.fromString(terminalId));

        if (computerEntity != null) {
            if (StringUtils.isNotBlank(reportSystemInfoDTO.getOs()) && StringUtils.isNotBlank(reportSystemInfoDTO.getOsVersion())) {
                computerEntity.setOs(reportSystemInfoDTO.getOs() + "_" + reportSystemInfoDTO.getOsVersion());
            }
            computerEntity.setCpu(reportSystemInfoDTO.getCpu());
            computerEntity.setMemory(reportSystemInfoDTO.getMemory());
            computerEntity.setSystemDisk(reportSystemInfoDTO.getSystemDisk());
            computerEntity.setName(reportSystemInfoDTO.getComputerName());
            computerEntity.setPersonDisk(reportSystemInfoDTO.getPersonDisk());
            update(computerEntity, 0);
            NotifyComputerDTO notifyComputerDTO = new NotifyComputerDTO();
            BeanUtils.copyProperties(computerEntity, notifyComputerDTO);
            notifyComputerDTO.setId(computerEntity.getId());
            try {
                notifyComputerDTO.setActionNotifyEnum(RcoComputerActionNotifyEnum.UPDATE_COMPUTER);
                notifyComputerDTO.setDeskIp(computerEntity.getIp());
                notifyComputerDTO.setMac(computerEntity.getMac());
                buildSpecInfo(computerEntity, notifyComputerDTO);
                notifyComputerChangeService.notifyComputerChange(notifyComputerDTO);
            } catch (BusinessException e) {
                LOGGER.error("通知PC终端[{}]系统信息更新异常", terminalId, e);
            }
        }
    }

    @Override
    public void updateComputerNetworkInfo(String terminalId, ComputerNetInfoDTO computerNetInfoDTO) {
        Assert.notNull(terminalId, "terminalId is not be null");
        Assert.notNull(computerNetInfoDTO, "computerNetInfoDTO is not be null");
        ComputerEntity computer = getComputerByTerminalId(terminalId);
        if (computer != null) {
            String networkNumber = null;
            BeanUtils.copyProperties(computerNetInfoDTO, computer);
            boolean isIpNotEmpty = StringUtils.isNotBlank(computerNetInfoDTO.getHostIp());
            boolean isSubnetMaskNotBlank = StringUtils.isNotBlank(computerNetInfoDTO.getMask());
            if (isIpNotEmpty && isSubnetMaskNotBlank && ValidatorUtil.isIPv4Address(computerNetInfoDTO.getHostIp())
                    && ValidatorUtil.isIPv4Mask(computerNetInfoDTO.getMask())) {
                networkNumber = IPv4Util.getNetworkNumberAddress(computerNetInfoDTO.getHostIp(), computerNetInfoDTO.getMask());
            }
            computer.setNetworkNumber(networkNumber);
            computer.setSubnetMask(computerNetInfoDTO.getMask());
            computer.setIp(computerNetInfoDTO.getHostIp());
            update(computer, 0);
        }
    }

    @Override
    public ComputerEntity findByIp(String ip) {
        Assert.hasText(ip, "ip is not be empty");
        return computerBusinessDAO.findByIp(ip);
    }

    @Override
    public List<ComputerEntity> findComputerInfoByGroupIdList(List<UUID> groupIdList) {
        Assert.notEmpty(groupIdList, "groupIdList must not be empty");
        return computerBusinessDAO.findByTerminalGroupIdList(groupIdList);
    }

    @Override
    public List<ComputerEntity> findComputerInfoComputerInfoByIdList(List<UUID> computerIdList) {
        Assert.notEmpty(computerIdList, "computerIdList must not be empty");
        return computerBusinessDAO.findByIdIn(computerIdList);
    }

    @Override
    public int countComputerByIdList(List<UUID> computerIdList) {
        Assert.notEmpty(computerIdList, "idList must not be empty");
        return computerBusinessDAO.countByIdIn(computerIdList);
    }

    @Override
    public ComputerEntity findByTerminalIdOrIp(String terminalId, String ip) {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(ip, "idList must not be null");
        return computerBusinessDAO.findByTerminalIdOrIp(terminalId, ip);
    }

    @Override
    public void saveComputer(ComputerEntity computerEntity) {
        Assert.notNull(computerEntity, "computerEntity must not be empty");
        update(computerEntity, 0);
    }

    @Override
    public void offline(UUID computerId) {
        Assert.notNull(computerId, "computerId must not be null");

        try {

            computerBusinessDAO.updateState(computerId, ComputerStateEnum.OFFLINE);
            LOGGER.info("更新pc终端[{}]状态为离线", computerId);
            CbbDeskDTO deskDTO = cbbDeskMgmtAPI.findById(computerId);
            if (deskDTO != null) {
                // 判断云桌面是否存在 更新桌面状态为离线
                LOGGER.info("更新桌面[{}]状态为离线", computerId);
                cbbIDVDeskMgmtAPI.updateIDVDeskStateByDeskId(computerId, CbbCloudDeskState.OFF_LINE);
            }
        } catch (BusinessException e) {
            LOGGER.error("PC终端[{}]离线, 修改对应的第三方云桌面[id:{}]状态为[OFF_LINE]出现异常:{}",
                    computerId, computerId, e);
        }
    }

    @Override
    public void updateWorkModel(UUID computerId, @Nullable ComputerWorkModelEnum computerWorkModel) {
        Assert.notNull(computerId, "computerId must not be null");
        Optional<ComputerEntity> computerEntityOptional = computerBusinessDAO.findById(computerId);
        if (!computerEntityOptional.isPresent()) {
            return;
        }
        if (computerWorkModel == null) {
            //异步通知oa
            THREAD_EXECUTOR.execute(() -> {
                try {
                    LOGGER.info("通知OA解除主机[{}]用户绑定", computerId);
                    CdcNotifyOaDisJoinHostRequest removeComputerDTO = new CdcNotifyOaDisJoinHostRequest();
                    removeComputerDTO.setType(OneAgentConnectTypeEnums.THIRD_HOST);
                    removeComputerDTO.setPlatformId(rcaTrusteeshipHostAPI.getPlatformId());
                    cbbOneAgentTcpSendAPI.notifyOaDisJoinHost(computerId.toString(), removeComputerDTO);
                } catch (Exception ex) {
                    LOGGER.error("通知OA解除主机[{}]用户绑定异常", computerId, ex);
                }
            });
        }
        computerBusinessDAO.updateWorkModel(computerId, computerWorkModel);

        notifyComputerChange(computerId, computerWorkModel, computerEntityOptional);

        ComputerEntity computerEntity = computerEntityOptional.get();

        computerEntity.setWorkModel(computerWorkModel);
        THREAD_EXECUTOR.execute(() -> {
            if (computerWorkModel != null) {
                try {
                    GetComputerInfoResponse response = getComputerInfoDTO(computerEntity);
                    LOGGER.info("通知OA PC终端[{}]信息[{}]", computerId, JSON.toJSONString(response));
                    computerTcpAPI.notifyOaComputerInfo(computerEntity.getId().toString(), response);
                } catch (Exception ex) {
                    LOGGER.error("cdc通知OA-PC终端[{}]信息异常", computerId, ex);
                }
            }
        });

    }

    private void notifyComputerChange(UUID computerId, ComputerWorkModelEnum computerWorkModel, Optional<ComputerEntity> computerEntityOptional) {
        try {
            if (computerWorkModel != null) {
                ComputerEntity computerEntity = computerEntityOptional.get();
                NotifyComputerDTO notifyComputerDTO = new NotifyComputerDTO();
                BeanUtils.copyProperties(computerEntity, notifyComputerDTO);
                notifyComputerDTO.setId(computerEntity.getId());
                notifyComputerDTO.setActionNotifyEnum(RcoComputerActionNotifyEnum.UPDATE_COMPUTER);
                notifyComputerDTO.setDeskIp(computerEntity.getIp());
                notifyComputerDTO.setMac(computerEntity.getMac());
                buildSpecInfo(computerEntity, notifyComputerDTO);
                notifyComputerChangeService.notifyComputerChange(notifyComputerDTO);
            }
        } catch (BusinessException e) {
            LOGGER.error("通知PC终端[{}]系统信息更新异常", computerId, e);
        }
    }

    @Override
    public GetComputerInfoResponse getComputerInfoDTO(ComputerEntity computerEntity) throws BusinessException {
        Assert.notNull(computerEntity, "computerEntity must not be null");
        GetComputerInfoResponse response = new GetComputerInfoResponse();
        RcoUserDesktopDTO userDesktopDTO = null;
        try {
            userDesktopDTO = userDesktopMgmtAPI.findByDesktopId(computerEntity.getId());
        } catch (BusinessException ex) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("获取云桌面[{}]信息异常,该日志可以忽略", computerEntity.getId());
            }
        }
        if (userDesktopDTO != null) {
            response.setUserId(userDesktopDTO.getUserId());
            response.setHostId(userDesktopDTO.getCbbDesktopId());
            CbbDeskDTO deskDTO = cbbDeskMgmtAPI.findById(computerEntity.getId());
            if (userDesktopDTO.getUserId() != null) {
                IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userDesktopDTO.getUserId());
                response.setUserName(userDetail.getUserName());
            }
            if (deskDTO != null) {
                response.setSessionType(deskDTO.getSessionType());
                response.setPoolModel(deskDTO.getDesktopPoolType().name());
            }
        }
        response.setHostId(computerEntity.getId());
        response.setTerminalId(computerEntity.getTerminalId());
        if (computerEntity != null && computerEntity.getWorkModel() != null) {
            response.setPlatformId(rcaTrusteeshipHostAPI.getPlatformId());
            response.setWorkModel(CbbOneAgentConnectTypeEnums.THIRD_HOST.name());
        }
        return response;
    }

    @Override
    public void removeById(UUID computerId) {
        Assert.notNull(computerId, "computerId must not be null");
        computerBusinessDAO.deleteById(computerId);
    }

    @Override
    public List<ComputerEntity> findAllByStatusAndType(ComputerStateEnum computerState, ComputerTypeEnum computerType) {
        Assert.notNull(computerState, "computerState must not be null");
        Assert.notNull(computerType, "computerType must not be null");
        return computerBusinessDAO.findAllByStateAndType(computerState, computerType);
    }

    @Override
    public void online(UUID computerId) {
        Assert.notNull(computerId, "computerId must not be null");
        ComputerEntity computerEntity = computerBusinessDAO.getOne(computerId);
        if (computerEntity != null && computerEntity.getState() == ComputerStateEnum.OFFLINE) {
            Session session = sessionManager.getSessionByAlias(computerId.toString());
            if (session != null) {
                computerEntity.setState(ComputerStateEnum.ONLINE);
                LOGGER.info("更新pc终端[{}]状态为在线", computerId);
                update(computerEntity, 0);
                try {
                    // 判断云桌面是否存在 更新桌面状态为运行中
                    CbbDeskDTO deskDTO = cbbDeskMgmtAPI.findById(computerEntity.getId());
                    if (deskDTO != null) {
                        LOGGER.info("更新桌面[{}]状态为在线", computerId);
                        cbbIDVDeskMgmtAPI.updateIDVDeskStateByDeskId(computerEntity.getId(), CbbCloudDeskState.RUNNING);
                    }
                } catch (Exception ex) {
                    LOGGER.error("更新桌面[{}]为运行中异常", computerEntity.getId(), ex);
                }
            }
        }
    }

    @Override
    public void moveGroupNotify(ComputerEntity entity) throws BusinessException {
        Assert.notNull(entity, "entity must not be null");
        if (entity.getType() == ComputerTypeEnum.THIRD) {
            NotifyComputerDTO notifyComputerDTO = new NotifyComputerDTO();
            BeanUtils.copyProperties(entity, notifyComputerDTO);
            buildSpecInfo(entity, notifyComputerDTO);
            notifyComputerDTO.setActionNotifyEnum(RcoComputerActionNotifyEnum.MOVE_COMPUTER);
            notifyComputerChangeService.notifyComputerChange(notifyComputerDTO);
        }
    }

    @Override
    public void update(ComputerEntity entity, int count) {
        Assert.notNull(entity, "entity must not be null");

        try {
            LockableExecutor.executeWithTryLock(entity.getId().toString(), () -> {
                computerBusinessDAO.save(entity);
            }, 3);
        } catch (Exception e) {
            LOGGER.error("更新pc终端[{}]异常", entity.getId(), e);
            // 重试3次
            if (count >= FAIL_TRY_COUNT) {
                return;
            }
            if (e instanceof ObjectOptimisticLockingFailureException) {
                ComputerEntity computerEntity = computerBusinessDAO.getOne(entity.getId());
                BeanUtils.copyProperties(entity, computerEntity);
                computerEntity.setVersion(computerEntity.getVersion());
                update(computerEntity, count + 1);
            } else {
                update(entity, count + 1);
            }
        }
    }

    @Override
    public void create(ComputerEntity entity) {
        Assert.notNull(entity, "entity must not be null");
        computerBusinessDAO.save(entity);
    }

    private void buildSpecInfo(ComputerEntity computerEntity, NotifyComputerDTO notifyComputerDTO) {
        notifyComputerDTO.setMemory(StringUtils.isNotBlank(computerEntity.getMemory()) ?
                Integer.parseInt(computerEntity.getMemory()) : 0);
        notifyComputerDTO.setCpu(StringUtils.isNotBlank(computerEntity.getCpu()) ?
                Integer.parseInt(computerEntity.getCpu()) : 0);
        notifyComputerDTO.setPersonSize(computerEntity.getPersonDisk());
        notifyComputerDTO.setSystemSize(computerEntity.getSystemDisk());
    }

}
