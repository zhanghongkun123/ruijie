package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.UserProfileBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 删除用户配置策略批处理
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/9
 *
 * @author WuShengQiang
 */
public class DeleteUserProfileStrategyBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserProfileStrategyBatchTaskHandler.class);

    private boolean isBatch = true;

    private String name = "";

    private BaseAuditLogAPI auditLogAPI;

    private UserProfileMgmtAPI userProfileMgmtAPI;

    public DeleteUserProfileStrategyBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI,
                                                     UserProfileMgmtAPI userProfileMgmtAPI) {
        super(iterator);
        Assert.notNull(userProfileMgmtAPI, "the userProfileMgmtAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");
        this.auditLogAPI = auditLogAPI;
        this.userProfileMgmtAPI = userProfileMgmtAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "the batchTaskItem is null.");
        UUID userProfileStrategyId = batchTaskItem.getItemID();
        UserProfileStrategyDTO userProfileStrategyDTO = userProfileMgmtAPI.findUserProfileStrategyById(userProfileStrategyId);
        String strategyName = userProfileStrategyDTO.getName();
        name = strategyName;
        try {
            userProfileMgmtAPI.deleteUserProfileStrategy(userProfileStrategyId);
        } catch (Exception e) {
            LOGGER.error("删除个性化配置策略[{}]失败", strategyName, e);
            String message;
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                message = ex.getI18nMessage();
            } else {
                message = e.getMessage();
            }
            auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_FAIL_LOG, strategyName, message);
            return DefaultBatchTaskItemResult.fail(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_FAIL_LOG, strategyName, message);
        }

        auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_SUCCESS, strategyName);
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_SUCCESS).msgArgs(new String[] {strategyName}).build();
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 批量删除
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_BATCH_SUCCESS);
        }

        // 删除单条
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_SUCCESS)
                    .msgArgs(new String[] {name}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_FAIL)
                    .msgArgs(new String[] {name}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }

    public void setBatch(boolean batch) {
        isBatch = batch;
    }
}