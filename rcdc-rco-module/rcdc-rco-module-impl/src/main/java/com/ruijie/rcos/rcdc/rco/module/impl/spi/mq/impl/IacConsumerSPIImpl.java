package com.ruijie.rcos.rcdc.rco.module.impl.spi.mq.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.ReleaseExternalAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.api.request.SaveAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmStatus;
import com.ruijie.rcos.gss.base.iac.module.dto.*;
import com.ruijie.rcos.gss.base.iac.module.enums.LogTypeEnum;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.log.module.def.api.BaseOperateLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.dto.BaseOperateLogDTO;
import com.ruijie.rcos.gss.log.module.def.dto.BaseSystemLogDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSecurityStrategyAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacClientAuthSecurityDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AuthChangeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CmsDockingAdminOperNotifyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SuperPrivilegeRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SyncAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.DeleteAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpdateAdminDataPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.SuperPrivilegeResponse;
import com.ruijie.rcos.rcdc.rco.module.def.enums.MainAuthAndAssistAuthEnum;
import com.ruijie.rcos.rcdc.rco.module.def.sms.constnts.SmsAndScanCodeCheckConstants;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.SmsPwdRecoverNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api.CertifiedSecurityConfigTcpAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api.RefreshLoginInfoTcpAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AdminDataPermissionService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AdminMgmtService;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.mq.IacConsumerSPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbSessionManagerAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.connectkit.api.tcp.session.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.common.dto.CommonConstants.PROTOCOL_VERSION;
import static com.ruijie.rcos.rcdc.rco.module.def.enums.MainAuthAndAssistAuthEnum.RJ_QRCODE_AUTH;

/**
 * Description: 身份中心消息处理实现类
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月19日
 *
 * @author lihengjing
 */
public class IacConsumerSPIImpl implements IacConsumerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(IacConsumerSPIImpl.class);

    /**
     线程池名称
     */
    private static final String THREAD_POOL_NAME = "notify-refresh-login-page-info";

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(THREAD_POOL_NAME).maxThreadNum(10).queueSize(1000).build();

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private IacRoleMgmtAPI iacRoleMgmtAPI;

    @Autowired
    private CmsDockingAPI cmsDockingAPI;

    @Autowired
    private AdminManageAPI adminManageAPI;

    @Autowired
    private AdminDataPermissionService adminDataPermissionService;

    @Autowired
    private AdminMgmtService adminMgmtService;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private BaseOperateLogMgmtAPI operateLogMgmtAPI;

    @Autowired
    private BaseSystemLogMgmtAPI systemLogMgmtAPI;

    @Autowired
    private BaseAlarmAPI baseAlarmAPI;

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private CbbSessionManagerAPI cbbSessionManagerAPI;

    @Autowired
    private CertifiedSecurityConfigTcpAPI certifiedSecurityConfigTcpAPI;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    private static final String PUSH_CERTIFIED_SECURITY_CONFIG = "push_certified_security_config";

    @Autowired
    private UserLoginSession userLoginSession;

    @Autowired
    private RefreshLoginInfoTcpAPI refreshLoginInfoTcpAPI;

    @Autowired
    private WebclientNotifyAPI webclientNotifyAPI;

    @Autowired
    CbbTerminalOperatorAPI terminalOperatorAPI;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private IacSecurityStrategyAPI iacSecurityStrategyAPI;

    @Autowired
    private ClientQrCodeAPI clientQrCodeAPI;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Override
    public void invalidLogin(InvalidLoginDTO invalidLoginDTO) {
        Assert.notNull(invalidLoginDTO, "invalidLoginDTO can not be null");
    }

    @Override
    public void kickOutLoginBySameAdmin(KickOutLoginBySameAdminDTO kickOutLoginBySameAdminDTO) {
        Assert.notNull(kickOutLoginBySameAdminDTO, "kickOutLoginBySameAdminDTO can not be null");
    }

    @Override
    public void adminChanged(IacAdminChangedDTO iacAdminChangedDTO) {
        Assert.notNull(iacAdminChangedDTO, "iacAdminChangedDTO can not be null");
        LOGGER.info("管理员事件通知： {}", JSON.toJSONString(iacAdminChangedDTO));
        IacAdminChangedDTO.AdminEvent event = iacAdminChangedDTO.getEvent();
        switch (event) {
            case CREATE:
                handleCreateAdmin(iacAdminChangedDTO);
                break;
            case DELETE:
                handleDeleteAdmin(iacAdminChangedDTO);
                break;
            case UPDATE:
                handleUpdateAdmin(iacAdminChangedDTO);
                break;
            case PASSWORD_CHANGED:
                adminPasswordChanged(iacAdminChangedDTO);
                break;
        }
    }

    @Override
    public void recordAuditLog(IacLogMqInfoDTO logMqInfoDTO) {
        Assert.notNull(logMqInfoDTO, "recordAuditLogDTO cannot be null");
        if (SubSystem.CDC != logMqInfoDTO.getSubSystem()) {
            // 非CDC日志忽略
            return;
        }
        // 操作日志
        if (logMqInfoDTO.getLogType() == LogTypeEnum.OPERATE_LOG) {
            BaseOperateLogDTO operateLogDTO = new BaseOperateLogDTO();
            operateLogDTO.setContent(logMqInfoDTO.getContent());
            operateLogDTO.setOperator(logMqInfoDTO.getOperator());
            operateLogDTO.setLoginIp(logMqInfoDTO.getLoginIp());
            operateLogDTO.setOperatorId(logMqInfoDTO.getOperatorId());
            operateLogDTO.setOperationTime(logMqInfoDTO.getOperationTime());
            operateLogMgmtAPI.addOperateLogRecord(operateLogDTO);
            return;
        }
        // 系统日志
        BaseSystemLogDTO systemLogDTO = new BaseSystemLogDTO();
        systemLogDTO.setContent(logMqInfoDTO.getContent());
        systemLogDTO.setCreateTime(logMqInfoDTO.getOperationTime());
        systemLogMgmtAPI.createSystemLog(systemLogDTO);
    }

    @Override
    public void addAlarm(IacAddAlarmDTO iacAddAlarmDTO) {
        Assert.notNull(iacAddAlarmDTO, "iacAddAlarmDTO cannot be null");
        SaveAlarmRequest alarmRequest = new SaveAlarmRequest();
        alarmRequest.setAlarmCode(iacAddAlarmDTO.getAlarmCode());
        alarmRequest.setAlarmContent(iacAddAlarmDTO.getAlarmContent());
        alarmRequest.setAlarmLevel(AlarmLevel.valueOf(iacAddAlarmDTO.getAlarmLevel().name()));
        alarmRequest.setAlarmName(iacAddAlarmDTO.getAlarmName());
        alarmRequest.setAlarmTime(iacAddAlarmDTO.getAlarmTime());
        alarmRequest.setAlarmType(iacAddAlarmDTO.getAlarmType());
        alarmRequest.setEnableSendMail(iacAddAlarmDTO.getEnableSendMail());
        baseAlarmAPI.saveAlarm(alarmRequest);
    }

    @Override
    public void releaseAlarm(IacReleaseAlarmDTO iacReleaseAlarmDTO) {
        Assert.notNull(iacReleaseAlarmDTO, "iacReleaseAlarmDTO cannot be null");
        ReleaseExternalAlarmRequest alarmRequest = new ReleaseExternalAlarmRequest();
        alarmRequest.setAlarmStatus(AlarmStatus.valueOf(iacReleaseAlarmDTO.getAlarmStatus().name()));
        alarmRequest.setAlarmCode(iacReleaseAlarmDTO.getAlarmCode());
        alarmRequest.setAlarmType(iacReleaseAlarmDTO.getAlarmType());
        try {
            baseAlarmAPI.releaseExternalAlarm(alarmRequest);
        } catch (BusinessException e) {
            LOGGER.error("解决AD域心跳告警的时候出现异常", e);
        }
    }

    @Override
    public void notifyClientInfo(IacAuthChangeDTO iacAuthTypeDTO) {
        Assert.notNull(iacAuthTypeDTO, "iacAuthTypeDTO can not be null");

        LOGGER.info("iac变更认证方式发送给cdc的信息为：{}", JSON.toJSONString(iacAuthTypeDTO));
        MainAuthAndAssistAuthEnum authEnum = MainAuthAndAssistAuthEnum.convert(iacAuthTypeDTO.getAuthKey());
        if (authEnum == null) {
            LOGGER.error("iac通知cdc认证方式发生变更，无对应的变更类型，iac请求为：{}", JSON.toJSONString(iacAuthTypeDTO));
            return;
        }

        if (authEnum == RJ_QRCODE_AUTH) {
            // 通知客户端移动客户端扫码方式变更
            updateQrCodeConfig();
        } else {
            // 通知webclient和shine认证方式发生变更
            notifyAuthChange(iacAuthTypeDTO);
        }
    }

    private void updateQrCodeConfig() {
        try {
            clientQrCodeAPI.updateQrCodeConfig();
        } catch (BusinessException ex) {
            LOGGER.error("移动客户端扫码变更通知客户端异常", ex);
        }
    }

    private void notifyAuthChange(IacAuthChangeDTO iacAuthTypeDTO) {
        LOGGER.info("身份中心登录认证方式变更，开始通知webclient、shine和oc");
        AuthChangeDTO authChangeDTO = new AuthChangeDTO();
        authChangeDTO.setAuthKey(iacAuthTypeDTO.getAuthKey());
        authChangeDTO.setEnable(iacAuthTypeDTO.getEnable());
        // 通知shine/oc登录认证方式变更
        notifyTerminalRefreshLoginInfo(authChangeDTO);
        // 通知webclient登录认证方式变更
        webclientNotifyAPI.notifyRefreshLoginPageInfo(authChangeDTO);
    }

    @Override
    public void accountStrategyChange(IacAccountStrategyChangeDTO request) {
        Assert.notNull(request, "request can not be null");
        // 如果开启了防爆功能
        if (request.getEnable()) {
            // 通知在线IDV、TCI策略已经改变
            certificationStrategyParameterAPI.notifyPwdStrategyModified();
        } else {
            notifyTerminalUserUnlocked(request.getUserNameList());
        }
    }

    @Override
    public void passwordStrategyChange(IacPasswordStrategyChangeDTO request) {
        Assert.notNull(request, "request can not be null");
        // TODO 暂时不需要处理
    }

    @Override
    public void unlockUsers(IacUnlockUserDTO request) {
        Assert.notNull(request, "request can not be null");

        notifyTerminalUserUnlocked(request.getUserNameList());
    }

    private void notifyTerminalUserUnlocked(List<String> userNameList) {
        try {
            // 通知用户绑定的终端解锁
            List<IacUserDetailDTO> userList = iacUserMgmtAPI.listUserByUserNames(userNameList);
            if (CollectionUtils.isEmpty(userList)) {
                return;
            }
            List<UUID> userIdList = userList.stream().map(IacUserDetailDTO::getId).collect(Collectors.toList());
            certificationStrategyParameterAPI.notifyTerminalUserUnlocked(userIdList);
        } catch (Exception e) {
            LOGGER.error("通知终端解锁失败", e);
        }
    }

    private void notifyTerminalRefreshLoginInfo(AuthChangeDTO authChangeDTO) {
        List<TerminalDTO> terminalDTOList = userTerminalMgmtAPI.listByState(CbbTerminalStateEnums.ONLINE);
        List<String> terminalIdList = terminalDTOList.stream().map(TerminalDTO::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(terminalIdList)) {
            LOGGER.info("当前不存在在线终端，不发送认证方式变更通知");
            return;
        }

        LOGGER.info("在线终端数量 {} ，开始发送认证方式变更通知", terminalIdList.size());
        for (String terminalId : terminalIdList) {
            THREAD_EXECUTOR.execute(() -> {
                try {
                    LOGGER.debug("开始发送认证方式变更通知, terminalId : {}", terminalId);
                    refreshLoginInfoTcpAPI.refreshLoginPageInfo(terminalId, authChangeDTO);
                } catch (Exception e) {
                    LOGGER.error(String.format("开始发送认证方式变更通知异常，终端ID：%s，异常：", terminalId), e);
                }
            });
        }
    }

    @Override
    public void userIdentityConfigChange(IacUserIdentityConfigRequest request) {
        Assert.notNull(request, "request cannot be null");
        // 开启统一和辅助认证绑定通知rcenter 动态口令绑定
        IacUserIdentityConfigRequest configRequest = new IacUserIdentityConfigRequest();
        BeanUtils.copyProperties(request, configRequest);
        rccmManageService.notifyUpdateUserIdentityConfig(configRequest);
    }

    @Override
    public void certifiedSecurityConfigUpdatedNotify(IacClientAuthSecurityDTO securityDTO) {
        Assert.notNull(securityDTO, "dto cannot be null");

        uwsDockingAPI.notifyModifyPwdConfigChanged(securityDTO.getChangePassword());
        // 推送安排配置给终端
        List<TerminalDTO> terminalDTOList = userTerminalMgmtAPI.listByState(CbbTerminalStateEnums.ONLINE);
        if (CollectionUtils.isEmpty(terminalDTOList)) {
            LOGGER.info("终端集合为空，无需通知");
            return;
        }
        ThreadExecutors.execute("认证安全配置变更推送给在线终端", () -> {
            LOGGER.info("终端在线数：{}", terminalDTOList.size());
            for (TerminalDTO dto : terminalDTOList) {
                IacClientAuthSecurityDTO securityNotifyDto = new IacClientAuthSecurityDTO();
                BeanUtils.copyProperties(securityDTO, securityNotifyDto);

                // 只有VOI和IDV有离线登录
                if (dto.getPlatform() != CbbTerminalPlatformEnums.VOI && dto.getPlatform() != CbbTerminalPlatformEnums.IDV) {
                    securityNotifyDto.setEnableOfflineLogin(false);
                }
                try {
                    // oneclient不支持string返回值。
                    Session session = cbbSessionManagerAPI.getSessionByAlias(dto.getId());
                    if (session != null && session.getAttribute(PROTOCOL_VERSION) != null) {
                        certifiedSecurityConfigTcpAPI.notifyCertifiedSecurityConfig(dto.getId(), securityNotifyDto);
                    } else {
                        shineMessageHandler.requestContent(dto.getId(), PUSH_CERTIFIED_SECURITY_CONFIG, JSON.toJSONString(securityNotifyDto));
                    }
                } catch (Exception e) {
                    LOGGER.error(String.format("推送认证安全配置变更信息异常，终端ID:%s", dto.getId()), e);
                }
            }
        });
    }

    @Override
    public void smsPwdRecoverNotify(IacSmsPwdRecoverNotifyDTO pwdRecoverDTO) {
        Assert.notNull(pwdRecoverDTO, "iacSmsPwdRecoverNotifyDTO cannot be null");
        SmsPwdRecoverNotifyDTO notifyDTO =
                new SmsPwdRecoverNotifyDTO(pwdRecoverDTO.getEnablePwdRecover(), pwdRecoverDTO.getInterval(), pwdRecoverDTO.getPeriod());
        // 通知在线终端
        List<String> terminalList = cbbTerminalOperatorAPI.getOnlineTerminalIdList();
        if (CollectionUtils.isEmpty(terminalList)) {
            return;
        }
        LOGGER.info("准备通知在线终端消息，[{}]终端在线数：{}", JSON.toJSONString(notifyDTO), terminalList.size());
        terminalList.parallelStream().forEach(terminalId -> {
            try {
                shineMessageHandler.requestContent(terminalId, SmsAndScanCodeCheckConstants.PUSH_SMS_RECOVER_PWD, notifyDTO);
            } catch (Exception e) {
                LOGGER.error("RCDC推送密码找回配置信息异常，终端ID：{}，e={}", terminalId, e);
            }
        });
    }

    private void adminPasswordChanged(IacAdminChangedDTO iacAdminChangedDTO) {
        try {
            // 管理员密码更新同步CMS
            adminChangedSyncCMS(iacAdminChangedDTO);
        } catch (Exception e) {
            LOGGER.error("管理员密码变更信息事件处理异常", e);
        }
    }

    private void handleDeleteAdmin(IacAdminChangedDTO iacAdminChangedDTO) {


        try {
            // 删除权限
            DeleteAdminRequest request = new DeleteAdminRequest();

            RcoAdminDTO rcoAdminDTO = new RcoAdminDTO();
            rcoAdminDTO.setId(iacAdminChangedDTO.getAdminId());
            rcoAdminDTO.setUserName(iacAdminChangedDTO.getUserName());
            request.setAdminIdArr(new RcoAdminDTO[] {rcoAdminDTO});
            adminDataPermissionService.deleteAdminDataPermisssionByAdminId(iacAdminChangedDTO.getAdminId());

            // 通知CMS
            SuperPrivilegeRequest superPrivilegeRequest = new SuperPrivilegeRequest();
            // 角色ID
            List<UUID> idList = new ArrayList<>();
            idList.add(iacAdminChangedDTO.getRoleId());
            UUID[] idArr = idList.toArray(new UUID[0]);
            superPrivilegeRequest.setRoleIdArr(idArr);
            SuperPrivilegeResponse superPrivilegeResponse = adminManageAPI.isSuperPrivilege(superPrivilegeRequest);
            if (superPrivilegeResponse.isSuperPrivilege()) {
                SyncAdminRequest syncAdminRequest = new SyncAdminRequest();
                syncAdminRequest.setOper(CmsDockingAdminOperNotifyEnum.SYNC_DEL_ADMIN.getOper());
                syncAdminRequest.setPassword(StringUtils.EMPTY);
                syncAdminRequest.setName(iacAdminChangedDTO.getUserName());
                cmsDockingAPI.syncAdmin(syncAdminRequest);
                return;
            }
        } catch (BusinessException e) {
            LOGGER.error("删除管理员信息事件处理异常", e);
        }
    }

    private void handleCreateAdmin(IacAdminChangedDTO iacAdminChangedDTO) {

        // 创建权限
        handleUpdateAdmin(iacAdminChangedDTO);

    }

    private void handleUpdateAdmin(IacAdminChangedDTO iacAdminChangedDTO) {

        try {
            // 处理管理员数据权限
            handleAdminPermission(iacAdminChangedDTO);

            // 通知CMS
            SuperPrivilegeRequest superPrivilegeRequest = new SuperPrivilegeRequest();
            // 角色ID
            List<UUID> idList = new ArrayList<>();
            idList.add(iacAdminChangedDTO.getRoleId());
            UUID[] idArr = idList.toArray(new UUID[0]);
            superPrivilegeRequest.setRoleIdArr(idArr);
            SuperPrivilegeResponse superPrivilegeResponse = adminManageAPI.isSuperPrivilege(superPrivilegeRequest);
            if (superPrivilegeResponse.isSuperPrivilege()) {
                SyncAdminRequest syncAdminRequest = new SyncAdminRequest();
                syncAdminRequest.setOper(CmsDockingAdminOperNotifyEnum.SYNC_ADMIN.getOper());
                syncAdminRequest.setName(iacAdminChangedDTO.getUserName());
                LOGGER.info("同步CMS创建管理员的信息为：[{}]", JSON.toJSONString(syncAdminRequest));
                cmsDockingAPI.syncAdmin(syncAdminRequest);
            }

        } catch (BusinessException e) {
            LOGGER.error("创建、更新管理员信息事件处理异常", e);
        }

    }

    private void handleAdminPermission(IacAdminChangedDTO iacAdminChangedDTO) throws BusinessException {
        IacRoleDTO role = iacRoleMgmtAPI.getRole(iacAdminChangedDTO.getRoleId());
        // 如果是超管、安全、审计管理员，这些管理员实际上都没有数据权限，所以清空表里面的信息
        if (permissionHelper.roleIsSuperAdmin(role) || permissionHelper.roleIsSecAdmin(role) || permissionHelper.roleIsAudaAdmin(role)) {
            UpdateAdminDataPermissionRequest request = new UpdateAdminDataPermissionRequest();
            request.setId(iacAdminChangedDTO.getAdminId());
            request.setNeedInitDefaultPermission(Boolean.FALSE);
            adminMgmtService.updateAdminDataPermission(request);
        } else {
            // 如果是自定义、系统管理员，没有权限的情况下则补偿默认数据权限
            UpdateAdminDataPermissionRequest request = new UpdateAdminDataPermissionRequest();
            request.setId(iacAdminChangedDTO.getAdminId());
            request.setNeedInitDefaultPermission(Boolean.TRUE);
            adminMgmtService.updateAdminDataPermission(request);
        }
    }

    /**
     * 管理员密码更新
     *
     * @param iacAdminChangedDTO 管理员变更事件DTO
     */
    public void adminChangedSyncCMS(IacAdminChangedDTO iacAdminChangedDTO) {
        Assert.notNull(iacAdminChangedDTO, "adminPasswordChangeDTO can not be null");
        try {
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(iacAdminChangedDTO.getAdminId());
            SuperPrivilegeRequest superPrivilegeRequest = new SuperPrivilegeRequest();
            superPrivilegeRequest.setRoleIdArr(baseAdminDTO.getRoleIdArr());
            SuperPrivilegeResponse response = adminManageAPI.isSuperPrivilege(superPrivilegeRequest);
            if (response.isSuperPrivilege()) {
                SyncAdminRequest syncAdminRequest = new SyncAdminRequest();
                syncAdminRequest.setOper(CmsDockingAdminOperNotifyEnum.SYNC_ADMIN.getOper());
                syncAdminRequest.setName(baseAdminDTO.getUserName());
                LOGGER.info("同步CMS管理员密码的信息为：[{}]", JSON.toJSONString(syncAdminRequest));
                cmsDockingAPI.syncAdmin(syncAdminRequest);
            }
        } catch (BusinessException e) {
            LOGGER.error("获取管理员[{}]密码失败", iacAdminChangedDTO.getUserName(), e);
            return;
        }

    }
}
