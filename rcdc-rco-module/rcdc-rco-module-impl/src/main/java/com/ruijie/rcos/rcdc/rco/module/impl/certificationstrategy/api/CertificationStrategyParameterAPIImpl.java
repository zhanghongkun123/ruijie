package com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.api;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacLockInfoDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacLockStatus;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.callback.CbbTerminalCallback;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CertificationTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.constants.CertificationStrategyConstants;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.AuthenticationService;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.CertificationStrategyService;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.certification.CertificationHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.SyncUserInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 认证策略
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年03月09日
 *
 * @author zhang.zhiwen
 */
public class CertificationStrategyParameterAPIImpl implements CertificationStrategyParameterAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificationStrategyParameterAPIImpl.class);

    private static final ExecutorService LOCK_USER_THREAD_POOL = ThreadExecutors.newBuilder("lockUserThread").maxThreadNum(20).queueSize(20).build();

    private static final ExecutorService NOTIFY_TERMINAL_USER_UNLOCK_POOL =
            ThreadExecutors.newBuilder("notifyTerminalUserUnlock").maxThreadNum(20).queueSize(20).build();

    /**
     * 用户类型
     */
    private static final CertificationTypeEnum USER_TYPE = CertificationTypeEnum.USER;

    /**
     * 终端管理密码
     */
    private static final CertificationTypeEnum TERMINAL_MNG_PWD = CertificationTypeEnum.TERMINAL;

    @Autowired
    private CertificationStrategyService certificationStrategyService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private ViewTerminalDAO viewTerminalDAO;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;
    
    @Autowired
    private CertificationHelper certificationHelper;

    @Autowired
    private UserService userService;


    @Override
    public PwdStrategyDTO getPwdStrategy() {
        return certificationStrategyService.getPwdStrategyParameter();
    }

    @Override
    public boolean getTerminalLockedStatusById(UUID realTerminalId) {
        Assert.notNull(realTerminalId, "realTerminalId must not be null");
        return authenticationService.getLockedStatusByIdAndType(realTerminalId, TERMINAL_MNG_PWD);
    }

    @Override
    public void unlockTerminalManagePwd(String terminalId) throws BusinessException {
        Assert.notNull(terminalId, "terminalId must not be null");
        CbbShineMessageRequest messageRequest = CbbShineMessageRequest.create(ShineAction.UNLOCK_TERMINAL_MANAGER_PWD, terminalId);
        messageRequest.setContent(new JSONObject());
        CbbShineMessageResponse shineMessageResponse;
        try {
            LOGGER.info("解锁终端的请求消息为：{}", messageRequest);
            shineMessageResponse = cbbTranspondMessageHandlerAPI.syncRequest(messageRequest);
        } catch (Exception e) {
            LOGGER.error("解锁终端失败，终端mac地址：{}", terminalId);
            throw new BusinessException(BusinessKey.RCDC_UNLOCK_TERMINAL_MNG_PWD_FAIL, e);
        }
        LOGGER.info("解锁终端请求的mac地址为：[{}]，返回结果为：[{}]", terminalId, JSONObject.toJSONString(shineMessageResponse));
        int responseCode = shineMessageResponse.getCode();
        if (responseCode == 0) {
            TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(terminalId);
            UUID realTerminalId = terminalDTO.getRealTerminalId();
            authenticationService.unlock(realTerminalId, TERMINAL_MNG_PWD);
        } else {
            LOGGER.error("解锁终端失败，终端mac地址：[{}]，返回码为：[{}]", terminalId, responseCode);
            throw new BusinessException(BusinessKey.RCDC_UNLOCK_TERMINAL_MNG_PWD_FAIL);
        }
    }

    @Override
    public void notifyPwdStrategyModified() {
        // 查询所有在线IDV、TCI终端
        List<ViewTerminalEntity> terminalEntityList = viewTerminalDAO.findByTerminalState(CbbTerminalStateEnums.ONLINE);
        List<String> terminalIdList = getNeedNotifyTerminalList(terminalEntityList);
        SyncUserInfoDTO syncUserInfoDTO = buildSyncUserInfoDTO(true, null);
        // 调用
        notifyTerminal(terminalIdList, syncUserInfoDTO);
    }

    @Override
    public void notifyTerminalUserUnlocked(List<UUID> userIdList) {
        Assert.notNull(userIdList, "userIdList must not be null");

        UUID uuid = UUID.randomUUID();
        LOGGER.info("执行异步线程通知用户解锁，uuid={}", uuid);
        for (UUID userId : userIdList) {
            NOTIFY_TERMINAL_USER_UNLOCK_POOL.execute(() -> notifyTerminalUserUnlocked(userId));
        }
        LOGGER.info("执行异步线程通知用户解锁完成，uuid={}", uuid);
    }

    @Override
    public void notifyTerminalUserUnlocked(UUID userId) {
        Assert.notNull(userId, "userId must not be null");
        List<ViewTerminalEntity> terminalEntityList = viewTerminalDAO.findByBindUserId(userId);
        List<String> terminalIdList = getNeedNotifyTerminalList(terminalEntityList);
        SyncUserInfoDTO syncUserInfoDTO = buildSyncUserInfoDTO(false, userId);
        notifyTerminal(terminalIdList, syncUserInfoDTO);
    }

    /**
     * 获取需要通知的终端id列表（需要通知的为在线的IDV、VOI类型）
     *
     * @param terminalEntityList 终端信息
     * @return 需要通知的终端id列表
     */
    private List<String> getNeedNotifyTerminalList(List<ViewTerminalEntity> terminalEntityList) {
        return terminalEntityList.stream().filter(terminalEntity -> {
            CbbTerminalPlatformEnums platform = terminalEntity.getPlatform();
            CbbTerminalStateEnums terminalState = terminalEntity.getTerminalState();
            return (platform == CbbTerminalPlatformEnums.IDV
                    || platform == CbbTerminalPlatformEnums.VOI) && terminalState == CbbTerminalStateEnums.ONLINE;
        }).map(ViewTerminalEntity::getTerminalId).collect(Collectors.toList());
    }

    /**
     * 构建同步信息，如果是编辑策略生成的，则不需要填写用户的信息，只需要填写全局策略对应的信息
     *
     * @param isEditStrategy 是否为编辑策略
     * @param userId 用户id
     * @return SyncUserInfoDTO
     */
    private SyncUserInfoDTO buildSyncUserInfoDTO(boolean isEditStrategy, UUID userId) {
        SyncUserInfoDTO syncUserInfoDTO = new SyncUserInfoDTO();
        if (!isEditStrategy && userId != null) {
            RcoViewUserEntity viewUserEntity = userService.getUserInfoById(userId);
            if (viewUserEntity != null) {
                IacLockInfoDTO userAccountLockInfo = certificationHelper.getUserAccountLockInfo(viewUserEntity.getUserName());
                syncUserInfoDTO.setLock(userAccountLockInfo.getLockStatus() == IacLockStatus.LOCKED);
                syncUserInfoDTO.setErrorTimes(viewUserEntity.getPwdErrorTimes());
                syncUserInfoDTO.setLockTime(viewUserEntity.getLockTime());
            }
        }
        PwdStrategyDTO pwdStrategyDTO = certificationStrategyService.getPwdStrategyParameter();
        syncUserInfoDTO.setUserLockedErrorsTimes(pwdStrategyDTO.getUserLockedErrorTimes());
        syncUserInfoDTO.setPwdLockTime(pwdStrategyDTO.getUserLockTime());
        syncUserInfoDTO.setPreventsBruteForce(pwdStrategyDTO.getPreventsBruteForce());
        syncUserInfoDTO.setHasEditStrategy(isEditStrategy);
        syncUserInfoDTO.setTimestamp(System.currentTimeMillis());

        return syncUserInfoDTO;
    }

    /**
     * 将用户信息、配置信息同步给终端
     *
     * @param terminalIdList 终端id
     * @param syncUserInfoDTO 同步的消息
     */
    private void notifyTerminal(List<String> terminalIdList, SyncUserInfoDTO syncUserInfoDTO) {
        if (CollectionUtils.isEmpty(terminalIdList)) {
            return;
        }
        terminalIdList = terminalIdList.stream().distinct().collect(Collectors.toList());
        LOGGER.info("需要同步锁定信息的终端列表为：[{}]", terminalIdList);
        String syncObj = JSON.toJSONString(syncUserInfoDTO);
        LOGGER.info("同步的消息为：[{}]", syncObj);
        List<String> finalTerminalIdList = terminalIdList;
        for (String terminalId : finalTerminalIdList) {
            CbbShineMessageRequest request = CbbShineMessageRequest.create(ShineAction.SYNC_USER_SECURITY_INFO, terminalId);
            request.setContent(syncObj);
            try {
                LOGGER.info("与终端[{}]同步用户锁定信息，请求id=[{}]，同步的信息为：[{}]", terminalId, syncUserInfoDTO.getUuid(), syncObj);
                messageHandlerAPI.asyncRequest(request, new CbbTerminalCallback() {
                    @Override
                    public void success(String s, CbbShineMessageResponse cbbShineMessageResponse) {
                        LOGGER.info("与终端[{}]同步用户锁定信息响应成功，请求id=[{}]", terminalId, syncUserInfoDTO.getUuid());
                    }

                    @Override
                    public void timeout(String s) {
                        Assert.notNull(s, "s can not null");
                        LOGGER.error("与终端[{}]同步用户锁定信息响应失败，请求id=[{}]，响应消息为：[{}]", terminalId, syncUserInfoDTO.getUuid(), s);
                    }
                });
            } catch (BusinessException e) {
                LOGGER.error(String.format("向终端[{%s}]同步信息失败，失败原因：", terminalId), e);
            }
        }
    }


    @Override
    public Boolean isNeedUpdatePassword(String password) {
        Assert.notNull(password, "password must not be null");

        // 如果不符合当前密码复杂度，则返回true
        return !certificationStrategyService.validatePwdByStrategyLevel(password);
    }

    @Override
    public Boolean isNeedForceUpdatePwd() {
        PwdStrategyDTO pwdStrategyDTO = certificationStrategyService.getPwdStrategyParameter();
        if (Objects.nonNull(pwdStrategyDTO) && Objects.nonNull(pwdStrategyDTO.getEnableForceUpdatePassword())) {
            return pwdStrategyDTO.getEnableForceUpdatePassword();
        }
        return CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_FIRST_FORCE_UPDATE_PASSWORD_DEFAULT;

    }
}
