package com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.base.iac.module.enums.AuthUserTypeEnum;
import com.ruijie.rcos.gss.base.iac.module.enums.MainAuthTypeEnum;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.dto.BaseSystemLogDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAuthTypeMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.authtype.IacMainAuthenticationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.authtype.IacSupportMainAuthenticationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WebclientNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AuthChangeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RccmServerConfigInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupSyncDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserSyncDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.RccmUnifiedManageRoleEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SyncUserIdentityConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.RccmAdGroupClusterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.RccmClusterUnifiedManageStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.RcdcUserClusterRestRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageMasterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.user.request.SyncUserPwdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.RccmRestKey;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.UserInfoService;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api.RefreshLoginInfoTcpAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api.UnifiedLoginConfigTcpAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.RcdcDataSyncRestKey;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.dto.RcdcUserDataSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.dto.RcdcUserGroupDataSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.RccmServerConfigSmRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.RequestParamDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoGlobalParameterEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.RccmManageConstant;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.connector.mq.api.WebClientProducerAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.sm.rccm.UpdateUnifiedLoginContextKey;
import com.ruijie.rcos.rcdc.rco.module.impl.sm.rccm.UpdateUnifiedLoginHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.service.UserLicenseService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.BeanUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.util.RestUtil;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description: RccmManageServiceImpl
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/9
 *
 * @author lihengjing
 */
@Service
public class RccmManageServiceImpl implements RccmManageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RccmManageServiceImpl.class);

    @Autowired
    private GlobalParameterService globalParameterService;

    private static final String RCCM_SERVER_CONFIG_VALUE_EMPTY = "";

    @Autowired
    private RestUtil restUtil;

    @Autowired
    private WebClientProducerAPI webClientProducerAPI;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Autowired
    private UnifiedLoginConfigTcpAPI unifiedLoginConfigTcpAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private UserLoginSession userLoginSession;

    @Autowired
    private UserLicenseService userLicenseService;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private IacAuthTypeMgmtAPI iacAuthTypeMgmtAPI;

    @Autowired
    private RefreshLoginInfoTcpAPI refreshLoginInfoTcpAPI;

    private static final Cache<String, String> USER_PWD_MODIFY_CACHE_MAP;

    private static final Object LOCK = new Object();

    static {
        USER_PWD_MODIFY_CACHE_MAP = CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.SECONDS).build();
    }

    /**
     * 线程池名称
     */
    private static final String THREAD_POOL_NAME = "notify-update-user-identity-config";

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(THREAD_POOL_NAME).maxThreadNum(10).queueSize(1000).build();

    @Override
    public void notifyListenRccmServer() {
        RccmServerConfigDTO target = new RccmServerConfigDTO();
        target.setListen(true);
        updateRccmServerConfig(target);
        RccmServerConfigDTO rccmServerConfig = getRccmServerConfig();

        checkUnifiedLoginChanged(false, isUnifiedLogin(rccmServerConfig));

        userLicenseService.updateWebClientUserSessionInfo(Constants.DEFAULT_CLUSTER_ID, rccmServerConfig.getClusterId());
    }

    @Override
    public void saveOrUpdateRccmServerConfig(RccmServerConfigDTO rccmServerConfigDTO) {
        Assert.notNull(rccmServerConfigDTO, "rccmServerConfigDTO must be not null");
        if (globalParameterService.existParamKey(RccmManageConstant.RCCM_SERVER_CONFIG_KEY)) {
            this.updateRccmServerConfig(rccmServerConfigDTO);
        } else {
            this.saveRccmServerConfig(rccmServerConfigDTO);
        }
    }

    @Override
    public void saveRccmServerConfig(RccmServerConfigDTO rccmServerConfigDTO) {
        Assert.notNull(rccmServerConfigDTO, "rccmServerConfigDTO must be not null");
        RcoGlobalParameterEntity rcoGlobalParameterEntity = new RcoGlobalParameterEntity();
        rcoGlobalParameterEntity.setId(UUID.randomUUID());
        rcoGlobalParameterEntity.setParamKey(RccmManageConstant.RCCM_SERVER_CONFIG_KEY);
        rcoGlobalParameterEntity.setParamValue(JSON.toJSONString(rccmServerConfigDTO));
        rcoGlobalParameterEntity.setCreateTime(new Date());
        rcoGlobalParameterEntity.setUpdateTime(new Date());
        globalParameterService.saveParameter(rcoGlobalParameterEntity);

        checkUnifiedLoginChanged(false, isUnifiedLogin(rccmServerConfigDTO));
    }

    @Override
    public synchronized void updateRccmServerConfig(RccmServerConfigDTO rccmServerConfigDTO) {
        Assert.notNull(rccmServerConfigDTO, "rccmServerConfigDTO must be not null");
        synchronized (LOCK) {
            RccmServerConfigDTO rccmServerConfig = getRccmServerConfig();
            // 退出纳管数据为空，此时不需要更新数据
            boolean hasNewJoin = rccmServerConfigDTO.getHasNewJoin() == null || Boolean.FALSE.equals(rccmServerConfigDTO.getHasNewJoin());
            if (hasNewJoin && (rccmServerConfig == null || StringUtils.isEmpty(rccmServerConfig.getServerIp()))) {
                return;
            }

            boolean isBeforeUnifiedLogin = isUnifiedLogin(rccmServerConfig);
            if (rccmServerConfig == null) {
                rccmServerConfig = new RccmServerConfigDTO();
            }
            BeanUtil.copyPropertiesIgnoreNull(rccmServerConfigDTO, rccmServerConfig);
            globalParameterService.updateParameter(RccmManageConstant.RCCM_SERVER_CONFIG_KEY, JSON.toJSONString(rccmServerConfig));

            if (rccmServerConfigDTO.getHasUnifiedLogin() != null) {
                rccmServerConfig.setHasUnifiedLogin(rccmServerConfigDTO.getHasUnifiedLogin());
                checkUnifiedLoginChanged(isBeforeUnifiedLogin, isUnifiedLogin(rccmServerConfig));
            }

        }

    }

    @Override
    public RccmServerConfigDTO getRccmServerConfig() {
        String rccmConfig = globalParameterService.findParameter(RccmManageConstant.RCCM_SERVER_CONFIG_KEY);
        if (StringUtils.isEmpty(rccmConfig)) {
            // 配置不存在不存在，返回NULL
            return null;
        }
        RccmServerConfigDTO rccmServerConfigDTO = JSON.parseObject(rccmConfig, RccmServerConfigDTO.class);
        if (StringUtils.isEmpty(rccmServerConfigDTO.getServerIp())) {
            // 配置serverIp不存在，返回NULL
            return null;
        }
        return rccmServerConfigDTO;
    }

    @Override
    public boolean getRccmServerHasUnifiedLoginConfig() {
        RccmServerConfigDTO rccmServerConfig = getRccmServerConfig();
        if (rccmServerConfig != null && Objects.nonNull(rccmServerConfig.getHasUnifiedLogin())) {
            return rccmServerConfig.getHasUnifiedLogin();
        }
        return false;
    }

    @Override
    public boolean canModifyGlobalSettings() {
        RccmServerConfigDTO rccmServerConfig = getRccmServerConfig();
        if (rccmServerConfig == null) {
            return true;
        }
        // RCCM纳管且统一登录开启且未开启辅助认证 则不可编辑辅助认证策略
        return !(Boolean.TRUE.equals(rccmServerConfig.getHasJoin()) && Boolean.TRUE.equals(rccmServerConfig.getHasUnifiedLogin())
                && (rccmServerConfig.getEnableAssistAuth() == null || Boolean.FALSE.equals(rccmServerConfig.getEnableAssistAuth())));
    }

    @Override
    public void updateUnifiedLogin(Boolean hasUnifiedLogin, Boolean enableAssistAuth) throws BusinessException {
        Assert.notNull(hasUnifiedLogin, "hasUnifiedLogin must be not null");
        Assert.notNull(enableAssistAuth, "enableAssistAuth must be not null");
        RccmServerConfigDTO rccmServerConfig = getRccmServerConfig();
        if (rccmServerConfig != null) {
            RccmServerConfigSmRequest rccmServerConfigSmRequest = new RccmServerConfigSmRequest();
            rccmServerConfigSmRequest.setHasUnifiedLogin(hasUnifiedLogin);
            rccmServerConfigSmRequest.setEnableAssistAuth(enableAssistAuth);
            stateMachineFactory.newBuilder(UpdateUnifiedLoginHandler.class)//
                    .initArg(UpdateUnifiedLoginContextKey.UPDATE_UNIFIED_LOGIN_REQUEST, rccmServerConfigSmRequest)//
                    .lockResources(rccmServerConfig.getClusterId().toString())//
                    .start()//
                    .waitForAllProcessFinish();
        }

        RccmServerConfigDTO target = new RccmServerConfigDTO();
        target.setHasUnifiedLogin(hasUnifiedLogin);
        target.setEnableAssistAuth(enableAssistAuth);
        updateRccmServerConfig(target);

    }

    @Override
    public boolean isUnifiedLogin() {
        return isUnifiedLogin(getRccmServerConfig());
    }

    @Override
    public boolean isUnifiedLoginAndNotAssistAuth() {
        RccmServerConfigDTO rccmServerConfig = getRccmServerConfig();
        if (rccmServerConfig == null) {
            return false;
        }
        return rccmServerConfig.getHasUnifiedLogin() && rccmServerConfig.getHasJoin()
                && rccmServerConfig.getHealth() && Boolean.FALSE.equals(rccmServerConfig.getEnableAssistAuth());
    }

    private boolean isUnifiedLogin(RccmServerConfigDTO rccmServerConfig) {
        if (rccmServerConfig == null) {
            return false;
        }
        return rccmServerConfig.getHasUnifiedLogin() && rccmServerConfig.getHasJoin() && rccmServerConfig.getHealth();
    }

    private void checkUnifiedLoginChanged(boolean beforeUnifiedLogin, boolean afterUnifiedLogin) {
        if (beforeUnifiedLogin != afterUnifiedLogin) {
            LOGGER.info("统一登录由[{}]变更为[{}]，开始通知webclient", beforeUnifiedLogin, afterUnifiedLogin);
            webClientProducerAPI.notifyUnifiedLogin(new RccmServerConfigInfoDTO(afterUnifiedLogin));

            List<TerminalDTO> terminalDTOList = userTerminalMgmtAPI.listByState(CbbTerminalStateEnums.ONLINE);
            notifyUnifiedLoginToTerminal(terminalDTOList,afterUnifiedLogin);

            // 发送账号密码开启消息，让客户端进行登录认证方式刷新
            AuthChangeDTO authChangeDTO = new AuthChangeDTO();
            authChangeDTO.setAuthKey(MainAuthTypeEnum.ACCOUNT_PWD.name());
            authChangeDTO.setEnable(true);
            // 通知webclient登录认证方式变更
            notifyAuthTypeTonToWebClient(authChangeDTO);
            // 通知在线终端认证方式变更
            notifyAuthTypeTonToTerminal(authChangeDTO,terminalDTOList);
        }
    }

    private void notifyUnifiedLoginToTerminal( List<TerminalDTO> terminalDTOList, boolean afterUnifiedLogin) {
        List<String> terminalIdList = terminalDTOList.stream().filter(item ->
                (item.getPlatform() == CbbTerminalPlatformEnums.APP || item.getPlatform() == CbbTerminalPlatformEnums.VDI)
                && userLoginSession.getLoginUserInfo(item.getId()) != null).map(TerminalDTO::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(terminalIdList)) {
            LOGGER.info("当前不存在已登录的在线VDI/APP终端，不发送统一登录变更通知");
            return;
        }

        LOGGER.info("已登录的在线VDI/APP终端数量 {} ，开始发送统一登录变更通知", terminalIdList.size());
        for (String terminalId : terminalIdList) {
            THREAD_EXECUTOR.execute(() -> {
                try {
                    LOGGER.debug("开始发送统一登录变更通知, terminalId : {}", terminalId);
                    unifiedLoginConfigTcpAPI.unifiedLoginConfigUpdate(terminalId,
                            new RccmServerConfigInfoDTO(afterUnifiedLogin));
                } catch (Exception e) {
                    LOGGER.error(String.format("开始发送统一登录变更通知异常，终端ID：%s，异常：", terminalId), e);
                }
            });
        }
    }

    private void notifyAuthTypeTonToTerminal(AuthChangeDTO authChangeDTO, List<TerminalDTO> terminalDTOList) {
        List<String> terminalIdList = terminalDTOList.stream().map(TerminalDTO::getId)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(terminalIdList)) {
            LOGGER.info("当前不存在在线终端，不发送认证方式变更通知");
            return;
        }

        // 如果开启了Rcenter统一登录，不支持企业微信、飞书、钉钉、oauth2.0认证和锐捷客户端扫码认证。因此Rcenter开关需要通知变更
        if (Boolean.FALSE.equals(checkNeedNoticeAuthTypeList())) {
            LOGGER.info("当前不存在需要通知认证方式，不发送认证方式变更通知");
            return;
        }

        for (String terminalId : terminalIdList) {
            THREAD_EXECUTOR.execute(() -> {
                try {
                    LOGGER.debug("开始发送认证方式变更通知, terminalId : {}", terminalId);
                    refreshLoginInfoTcpAPI.refreshLoginPageInfo(terminalId, authChangeDTO);
                } catch (Exception e) {
                    LOGGER.error(String.format("开始发送统一登录变更通知异常，终端ID：%s，异常：", terminalId), e);
                }
            });
        }
    }

    private void notifyAuthTypeTonToWebClient(AuthChangeDTO authChangeDTO) {
        if (Boolean.FALSE.equals(checkNeedNoticeAuthTypeList())) {
            return;
        }

        try {
            webClientProducerAPI.notifyRefreshLoginPageInfo(authChangeDTO);
        } catch (BusinessException e) {
            LOGGER.error("开始发送统一登录变更网页客户端通知异常", e);
        }
    }

    private boolean checkNeedNoticeAuthTypeList() {
        try {
            IacSupportMainAuthenticationDTO iacSupportMainAuthentication = iacAuthTypeMgmtAPI.mainAuthentication(
                    SubSystem.CDC, AuthUserTypeEnum.ADMIN);

            if (Objects.isNull(iacSupportMainAuthentication)) {
                // 当前没有主认证开启，无需通知
                return false;
            }

            if (CollectionUtils.isEmpty(iacSupportMainAuthentication.getMainAuthenticationList())) {
                // 当前没有主认证开启，无需通知
                return false;
            }

            for (IacMainAuthenticationDTO iacMainAuthenticationDTO : iacSupportMainAuthentication.getMainAuthenticationList()) {
                // 如果支持的主认证包含企业微信/钉钉/飞书/AUTH2/锐捷客户端则需要通知刷新
                if (MainAuthTypeEnum.WORK_WEIXIN == iacMainAuthenticationDTO.getAuthType()
                        || MainAuthTypeEnum.DINGDING == iacMainAuthenticationDTO.getAuthType()
                        || MainAuthTypeEnum.FEISHU == iacMainAuthenticationDTO.getAuthType()
                        || MainAuthTypeEnum.AUTH2 == iacMainAuthenticationDTO.getAuthType()
                        || MainAuthTypeEnum.RJ_QRCODE_AUTH == iacMainAuthenticationDTO.getAuthType()) {
                    return true;
                }
            }
        } catch (BusinessException ex) {
            LOGGER.error("[Rcenter开关]获取认证方式变更通知列表异常。", ex);
        }

        // 出现异常通知客户端刷新获取最新登录方式
        return true;
    }

    @Override
    public void exitJoinRccmServer() {
        synchronized (LOCK) {
            globalParameterService.updateParameter(RccmManageConstant.RCCM_SERVER_CONFIG_KEY, RCCM_SERVER_CONFIG_VALUE_EMPTY);
        }
    }

    @Override
    public void pushUser(List<String> usernameList, boolean force) {
        Assert.notNull(usernameList, "usernameList must be not null");
        if (CollectionUtils.isEmpty(usernameList)) {
            return;
        }
        RccmServerConfigDTO rccmServerConfig = getRccmServerConfig();
        if (rccmServerConfig == null || !rccmServerConfig.getHasJoin() || !rccmServerConfig.getHealth()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("RCCM服务未就绪，取消新增用户数据推送，集合大小:{}", usernameList.size());
            }
            return;
        }

        // 需要判断用户是否有桌面资源
        if (!force) {
            // 根据用户名查询是否有桌面资源
            usernameList = userInfoService.getUserDesktopResource(usernameList);
            if (CollectionUtils.isEmpty(usernameList)) {
                return;
            }
        }

        RcdcUserClusterRestRequest rcdcUserClusterRestRequest = new RcdcUserClusterRestRequest();
        rcdcUserClusterRestRequest.setClusterId(rccmServerConfig.getClusterId());
        rcdcUserClusterRestRequest.setUsernameList(usernameList);
        RequestParamDTO<RcdcUserClusterRestRequest> requestParamDTO =
                restUtil.buildRccmRequestParamDTO(rccmServerConfig, RccmRestKey.PUSH_USER, rcdcUserClusterRestRequest);
        try {
            restUtil.tryRequest(requestParamDTO);
        } catch (BusinessException ex) {
            LOGGER.error("推送到RCCM用户失败" + ex.getMessage());
        }
    }

    @Override
    public void pushAdGroupToRccm(Boolean hasAdGroup) {
        Assert.notNull(hasAdGroup, "hasAdGroup must be not null");
        RccmServerConfigDTO rccmServerConfig = getRccmServerConfig();
        if (rccmServerConfig == null || !rccmServerConfig.getHasJoin() || !rccmServerConfig.getHealth()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("RCCM服务未就绪，取消新增用户数据推送");
            }
            return;
        }
        RccmAdGroupClusterRequest rccmAdGroupClusterRequest = new RccmAdGroupClusterRequest();
        rccmAdGroupClusterRequest.setClusterId(rccmServerConfig.getClusterId());
        rccmAdGroupClusterRequest.setHasAdGroup(hasAdGroup);
        RequestParamDTO<RccmAdGroupClusterRequest> requestParamDTO =
                restUtil.buildRccmRequestParamDTO(rccmServerConfig, RccmRestKey.PUSH_AD_GROUP, rccmAdGroupClusterRequest);
        try {
            restUtil.tryRequest(requestParamDTO);
        } catch (BusinessException ex) {
            LOGGER.error("推送到RCCMAd域安全组[{}]失败", hasAdGroup, ex);
        }
    }

    @Override
    public void syncUserGroup(UserGroupSyncDataDTO userGroupSyncDataDTO) throws BusinessException {
        Assert.notNull(userGroupSyncDataDTO, "userGroupSyncDataDTO must be not null");
        RccmServerConfigDTO serverConfigDTO = this.getRccmServerConfig();
        RequestParamDTO<RcdcUserGroupDataSyncRequest> requestParamDTO = new RequestParamDTO<>();
        requestParamDTO.setAccount(serverConfigDTO.getAccount());
        if (serverConfigDTO.getPassword() != null) {
            String decryptPwd = AesUtil.descrypt(serverConfigDTO.getPassword(), RedLineUtil.getRealAdminRedLine());
            requestParamDTO.setPwd(decryptPwd);
        }
        requestParamDTO.setIp(serverConfigDTO.getServerIp());
        requestParamDTO.setPort(serverConfigDTO.getGatewayPort());
        requestParamDTO.setPath(RcdcDataSyncRestKey.RCDC_ACTIVE_SYNC_USER_GROUP_DATA);

        RcdcUserGroupDataSyncRequest rcdcUserOperDataSyncRequest = new RcdcUserGroupDataSyncRequest();
        rcdcUserOperDataSyncRequest.setClusterId(serverConfigDTO.getClusterId());
        rcdcUserOperDataSyncRequest.setUserGroupSyncDataDTO(userGroupSyncDataDTO);

        requestParamDTO.setRequestData(rcdcUserOperDataSyncRequest);
        LOGGER.info("rcdc主动同步用户组数据 {}", JSON.toJSONString(rcdcUserOperDataSyncRequest));
        try {
            restUtil.onceRequest(requestParamDTO);
        } catch (BusinessException ex) {
            LOGGER.error("请求集群接口异常", ex);
            throw ex;
        }

    }

    @Override
    public void syncUser(UserSyncDataDTO userSyncDataDTO) throws BusinessException {
        Assert.notNull(userSyncDataDTO, "userSyncDataDTO must be not null");
        RccmServerConfigDTO serverConfigDTO = this.getRccmServerConfig();
        RequestParamDTO<RcdcUserDataSyncRequest> requestParamDTO = new RequestParamDTO<>();
        requestParamDTO.setAccount(serverConfigDTO.getAccount());
        if (serverConfigDTO.getPassword() != null) {
            String decryptPwd = AesUtil.descrypt(serverConfigDTO.getPassword(), RedLineUtil.getRealAdminRedLine());
            requestParamDTO.setPwd(decryptPwd);
        }
        requestParamDTO.setIp(serverConfigDTO.getServerIp());
        requestParamDTO.setPort(serverConfigDTO.getGatewayPort());
        requestParamDTO.setPath(RcdcDataSyncRestKey.RCDC_ACTIVE_SYNC_USER_DATA);

        RcdcUserDataSyncRequest rcdcUserOperDataSyncRequest = new RcdcUserDataSyncRequest();
        rcdcUserOperDataSyncRequest.setClusterId(serverConfigDTO.getClusterId());
        rcdcUserOperDataSyncRequest.setUserSyncDataDTO(userSyncDataDTO);

        requestParamDTO.setRequestData(rcdcUserOperDataSyncRequest);
        LOGGER.info("rcdc主动同步用户数据 {}", JSON.toJSONString(rcdcUserOperDataSyncRequest));
        try {
            restUtil.onceRequest(requestParamDTO);
        } catch (BusinessException ex) {
            LOGGER.error("请求集群接口异常", ex);
            throw ex;
        }

    }

    @Override
    public void pushDeskStrategyToRccm(UnifiedManageMasterRequest<?> masterRequest) throws BusinessException {
        Assert.notNull(masterRequest, "masterRequest is not null.");
        RccmServerConfigDTO serverConfigDTO = this.getRccmServerConfig();
        RequestParamDTO<UnifiedManageMasterRequest<?>> requestParamDTO = new RequestParamDTO<>();
        requestParamDTO.setAccount(serverConfigDTO.getAccount());
        if (serverConfigDTO.getPassword() != null) {
            String decryptPwd = AesUtil.descrypt(serverConfigDTO.getPassword(), RedLineUtil.getRealAdminRedLine());
            requestParamDTO.setPwd(decryptPwd);
        }
        requestParamDTO.setIp(serverConfigDTO.getServerIp());
        requestParamDTO.setPort(serverConfigDTO.getGatewayPort());
        requestParamDTO.setPath(RccmRestKey.PUSH_UNIFIED_MANAGE_DATA);
        requestParamDTO.setRequestData(masterRequest);
        LOGGER.info("转发给RCCM的请求内容：{}", JSON.toJSONString(masterRequest));
        try {
            restUtil.onceRequest(requestParamDTO);
        } catch (BusinessException ex) {
            LOGGER.error("请求集群接口异常", ex);
            throw ex;
        }
    }

    @Override
    public boolean isMaster() {

        return getClusterUnifiedManageStrategy().stream().anyMatch(o -> o.getRole() == RccmUnifiedManageRoleEnum.MASTER);
    }

    @Override
    public boolean isSlave() {

        return getClusterUnifiedManageStrategy().stream().anyMatch(o -> o.getRole() == RccmUnifiedManageRoleEnum.SLAVE);
    }

    @Override
    public void notifyUserUpdatePwd(IacUserDetailDTO userDetail) {
        Assert.notNull(userDetail, "userDetail is not null.");
        String userName = USER_PWD_MODIFY_CACHE_MAP.getIfPresent(userDetail.getUserName());
        // 通过同步用户修改密码接口进来不通知rcenter
        if (userDetail.getUserName().equals(userName)) {
            return;
        }
        List<RccmClusterUnifiedManageStrategyDTO> manageStrategyDTOList = getClusterUnifiedManageStrategy();
        // rcenter 集群同步策略-开启用户同步才同步
        if (manageStrategyDTOList.isEmpty() || Boolean.FALSE.equals(manageStrategyDTOList.get(0).getEnableSyncUser())) {
            return;
        }
        RccmServerConfigDTO rccmServerConfig = getRccmServerConfig();
        if (Objects.nonNull(rccmServerConfig)) {
            if (Boolean.FALSE.equals(rccmServerConfig.getHealth())) {
                String tip = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_NOT_CONNECT_RCCM);
                String msg = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_NOTIFY_USER_UPDATE_PWD_FAIL, userDetail.getUserName(), tip);
                saveSystemLog(msg);
                LOGGER.warn("用户[{}]同步修改密码失败，原因：{}", userDetail.getUserName(), tip);
                return;
            }
            SyncUserPwdRequest syncUserPwdRequest = new SyncUserPwdRequest();
            syncUserPwdRequest.setUserName(userDetail.getUserName());
            syncUserPwdRequest.setPassword(userDetail.getPassword());
            syncUserPwdRequest.setClusterId(rccmServerConfig.getClusterId());
            RequestParamDTO<SyncUserPwdRequest> requestParamDTO =
                    restUtil.buildRccmRequestParamDTO(rccmServerConfig, RccmRestKey.SYNC_USER_PASSWORD, syncUserPwdRequest);
            try {
                restUtil.tryRequest(requestParamDTO);
                String msg = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_NOTIFY_USER_UPDATE_PWD_SUCCESS, userDetail.getUserName());
                saveSystemLog(msg);
            } catch (BusinessException ex) {
                LOGGER.error("推送到RCCM同步用户[{}]密码失败", userDetail.getUserName(), ex);
                String msg = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_NOTIFY_USER_UPDATE_PWD_FAIL,
                        userDetail.getUserName(), ex.getI18nMessage());
                saveSystemLog(msg);
            }
        }

    }

    private RequestParamDTO<SyncUserPwdRequest> buildSyncRequestData(IacUserDetailDTO userDetail, RccmServerConfigDTO rccmServerConfig) {
        SyncUserPwdRequest syncUserPwdRequest = new SyncUserPwdRequest();
        syncUserPwdRequest.setUserName(userDetail.getUserName());
        syncUserPwdRequest.setPassword(userDetail.getPassword());
        syncUserPwdRequest.setClusterId(rccmServerConfig.getClusterId());
        RequestParamDTO<SyncUserPwdRequest> requestParamDTO = new RequestParamDTO<>();
        requestParamDTO.setAccount(rccmServerConfig.getAccount());
        if (rccmServerConfig.getPassword() != null) {
            String decryptPwd = AesUtil.descrypt(rccmServerConfig.getPassword(), RedLineUtil.getRealAdminRedLine());
            requestParamDTO.setPwd(decryptPwd);
        }
        requestParamDTO.setIp(rccmServerConfig.getServerIp());
        requestParamDTO.setPort(rccmServerConfig.getGatewayPort());
        requestParamDTO.setPath(RccmRestKey.SYNC_USER_PASSWORD);

        requestParamDTO.setRequestData(syncUserPwdRequest);
        return requestParamDTO;
    }

    @Override
    public void addSyncUserPasswordCache(String userName) {
        Assert.hasText(userName, "userName not be empty");
        USER_PWD_MODIFY_CACHE_MAP.put(userName, userName);
    }

    @Override
    public void delSyncUserPasswordCache(String userName) {
        Assert.hasText(userName, "userName not be empty");
        USER_PWD_MODIFY_CACHE_MAP.invalidate(userName);
    }

    @Override
    public void notifyUpdateUserIdentityConfig(IacUserIdentityConfigRequest configRequest) {
        Assert.notNull(configRequest, "configRequest is not null.");
        // 当前只支持USER
        if (configRequest.getRelatedType() != IacConfigRelatedType.USER) {
            return;
        }

        List<RccmClusterUnifiedManageStrategyDTO> manageStrategyDTOList = getClusterUnifiedManageStrategy();
        // rcenter 集群同步策略-开启用户同步才同步
        if (manageStrategyDTOList.isEmpty() || Boolean.FALSE.equals(manageStrategyDTOList.get(0).getEnableSyncUser())) {
            return;
        }
        THREAD_EXECUTOR.execute(() -> {
            RccmServerConfigDTO rccmServerConfig = getRccmServerConfig();
            if (Objects.nonNull(rccmServerConfig)) {
                // 开启统一和辅助认证解绑通知rcenter 动态口令解绑
                SyncUserIdentityConfigRequest request = new SyncUserIdentityConfigRequest();
                String username = "";
                try {
                    username = userInfoService.getUsernameById(configRequest.getRelatedId());
                    if (Boolean.FALSE.equals(rccmServerConfig.getHealth())) {
                        String tip = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_NOT_CONNECT_RCCM);
                        String msg = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_SYNC_USER_IDENTITY_FAIL, username, tip);
                        saveSystemLog(msg);
                        LOGGER.warn("用户[{}]同步修改密码失败，原因：{}", username, tip);
                        return;
                    }
                    request.setUserName(username);
                    BeanUtils.copyProperties(configRequest, request);
                    RequestParamDTO<SyncUserIdentityConfigRequest> requestParamDTO =
                            restUtil.buildRccmRequestParamDTO(rccmServerConfig, RccmRestKey.SYNC_USER_IDENTITY_CONFIG, request);
                    restUtil.tryRequest(requestParamDTO);
                    String msg = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_SYNC_USER_IDENTITY_SUCCESS, username);
                    saveSystemLog(msg);
                } catch (BusinessException ex) {
                    LOGGER.error("推送到RCCM用户辅助认证信息[{}]失败", configRequest.getRelatedId(), ex);
                    String msg = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_SYNC_USER_IDENTITY_FAIL,
                            username, ex.getI18nMessage());
                    saveSystemLog(msg);
                }
            }
        });
    }

    private RequestParamDTO<SyncUserIdentityConfigRequest> buildRequestData(IacUserIdentityConfigRequest configRequest,
                                                                            RccmServerConfigDTO rccmServerConfig, SyncUserIdentityConfigRequest request) {
        RequestParamDTO<SyncUserIdentityConfigRequest> requestParamDTO = new RequestParamDTO<>();
        BeanUtils.copyProperties(configRequest, request);

        requestParamDTO.setAccount(rccmServerConfig.getAccount());
        if (rccmServerConfig.getPassword() != null) {
            String decryptPwd = AesUtil.descrypt(rccmServerConfig.getPassword(), RedLineUtil.getRealAdminRedLine());
            requestParamDTO.setPwd(decryptPwd);
        }
        requestParamDTO.setIp(rccmServerConfig.getServerIp());
        requestParamDTO.setPort(rccmServerConfig.getGatewayPort());
        requestParamDTO.setPath(RccmRestKey.SYNC_USER_IDENTITY_CONFIG);
        request.setClusterId(rccmServerConfig.getClusterId());
        requestParamDTO.setRequestData(request);
        return requestParamDTO;
    }



    @Override
    public Boolean hasOtpCodeTab() {
        RccmServerConfigDTO rccmServerConfig = getRccmServerConfig();
        // 开启统一登录和辅助认证-不支持动态口令直接登录
        return !(rccmServerConfig != null && rccmServerConfig.enableAssistAuth());
    }

    @Override
    public void pushUserByUserIdList(List<UUID> userIdList) {
        Assert.notEmpty(userIdList, "userIdList is not empty.");
        THREAD_EXECUTOR.execute(() -> {
            try {
                List<IacUserDetailDTO> userDetailDTOList = cbbUserAPI.listUserByUserIds(userIdList);
                List<String> userNameList = userDetailDTOList.stream().map(IacUserDetailDTO::getUserName).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(userNameList)) {
                    //推送用户数据
                    pushUser(userNameList, true);
                }
            } catch (Exception e) {
                LOGGER.error("推送用户[{}]用户异常", JSON.toJSONString(userIdList));
            }
        });
    }

    private void saveSystemLog(String content) {
        BaseSystemLogDTO logDTO = new BaseSystemLogDTO();
        logDTO.setId(UUID.randomUUID());
        logDTO.setContent(content);
        logDTO.setCreateTime(new Date());
        baseSystemLogMgmtAPI.createSystemLog(logDTO);
    }

    @Override
    public void saveClusterUnifiedManageStrategy(List<RccmClusterUnifiedManageStrategyDTO> strategyList) {
        Assert.notNull(strategyList, "strategyList must be not null");
        boolean existParamKey = globalParameterService.existParamKey(RccmManageConstant.RCCM_UNIFIED_MANAGE_INFO);
        if (existParamKey) {
            globalParameterService.updateParameter(RccmManageConstant.RCCM_UNIFIED_MANAGE_INFO, JSON.toJSONString(strategyList));
        } else {
            RcoGlobalParameterEntity rcoGlobalParameterEntity = new RcoGlobalParameterEntity();
            rcoGlobalParameterEntity.setId(UUID.randomUUID());
            rcoGlobalParameterEntity.setParamKey(RccmManageConstant.RCCM_UNIFIED_MANAGE_INFO);
            rcoGlobalParameterEntity.setParamValue(JSON.toJSONString(strategyList));
            rcoGlobalParameterEntity.setCreateTime(new Date());
            rcoGlobalParameterEntity.setUpdateTime(new Date());
            globalParameterService.saveParameter(rcoGlobalParameterEntity);
        }
    }

    @Override
    public List<RccmClusterUnifiedManageStrategyDTO> getClusterUnifiedManageStrategy() {
        String unifiedManageStrategyInfo = globalParameterService.findParameter(RccmManageConstant.RCCM_UNIFIED_MANAGE_INFO);
        if (StringUtils.isEmpty(unifiedManageStrategyInfo)) {
            return Lists.newArrayList();
        }
        return JSON.parseObject(unifiedManageStrategyInfo, new TypeReference<List<RccmClusterUnifiedManageStrategyDTO>>() {
            // 类型转化
        });
    }
}
