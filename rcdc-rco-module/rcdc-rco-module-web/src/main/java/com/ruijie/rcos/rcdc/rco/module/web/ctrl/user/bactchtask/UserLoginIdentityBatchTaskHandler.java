package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.Iterator;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DataSyncAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 用户身份验证配置批处理任务类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/26
 *
 * @author lintingling
 */
public class UserLoginIdentityBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginIdentityBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;

    private IacUserLoginIdentityLevelEnum loginIdentityLevel;

    private IacUserMgmtAPI userAPI;

    private DataSyncAPI dataSyncAPI;


    private boolean isBatch = true;

    private String userName;

    public UserLoginIdentityBatchTaskHandler(Iterator<DefaultBatchTaskItem> batchTaskItemIterator, BaseAuditLogAPI auditLogAPI,
                                             IacUserMgmtAPI userAPI) {

        super(batchTaskItemIterator);

        this.auditLogAPI = auditLogAPI;
        this.userAPI = userAPI;
    }

    /**
     * 初始化
     * @param userIdentityConfigAPI userIdentityConfigAPI
     * @param loginIdentityLevel 双网身份验证
     */
    public void init(IacUserIdentityConfigMgmtAPI userIdentityConfigAPI, IacUserLoginIdentityLevelEnum loginIdentityLevel) {
        Assert.notNull(userIdentityConfigAPI, "UserIdentityConfigAPI must not be null");
        Assert.notNull(loginIdentityLevel, "loginIdentityLevel must not be null");
        this.userIdentityConfigAPI = userIdentityConfigAPI;
        this.loginIdentityLevel = loginIdentityLevel;
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    public void setDataSyncAPI(DataSyncAPI dataSyncAPI) {
        this.dataSyncAPI = dataSyncAPI;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_RESULT);
        }

        // 更新单条用户
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {userName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_SINGLE_FAIL_RESULT)
                    .msgArgs(new String[] {userName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "BatchTaskItem is null");

        UUID userId = item.getItemID();
        IacUserIdentityConfigRequest request = new IacUserIdentityConfigRequest(IacConfigRelatedType.USER, userId);
        request.setLoginIdentityLevel(loginIdentityLevel);

        try {
            IacUserDetailDTO userDetail = userAPI.getUserDetail(userId);
            IacUserTypeEnum userType = userDetail.getUserType();
            userName = userDetail.getUserName();

            if (!hasUserIdentityConfig(userType)) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_FAIL_REASON_VISITOR, new String[] {userName});
            }

            userIdentityConfigAPI.updateUserIdentityConfig(request);

            // 此处触发同步
            dataSyncAPI.activeSyncUserData(userId);

            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_SUCCESS, new String[] {userName});
            LOGGER.info("用户修改身份验证成功,用户Id={},userName={}", userId, userName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_SUCCESS).msgArgs(new String[] {userName}).build();
        } catch (BusinessException e) {
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            LOGGER.info("用户修改身份验证失败,用户Id={},userName={}", userId, userName);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_FAIL, new String[] {exceptionMsg});
            throw new BusinessException(UserBusinessKey.RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_FAIL, e, exceptionMsg);
        }
    }
    
    private boolean hasUserIdentityConfig(IacUserTypeEnum userType) throws BusinessException {

        return userIdentityConfigAPI.hasUserIdentityConfig(userType);
    }
}
