package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.UserProfileBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 删除用户配置路径批处理
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/9
 *
 * @author WuShengQiang
 */
public class DeleteUserProfilePathBatchTaskHandler extends AbstractBatchTaskHandler {

    private boolean isBatch = true;

    private String name = "";

    private BaseAuditLogAPI auditLogAPI;

    private UserProfileMgmtAPI userProfileMgmtAPI;

    public DeleteUserProfilePathBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI,
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
        UUID userProfilePathId = batchTaskItem.getItemID();
        UserProfilePathDTO userProfilePathDTO = userProfileMgmtAPI.findUserProfilePathById(userProfilePathId);
        String pathName = userProfilePathDTO.getName();
        name = pathName;
        userProfileMgmtAPI.deleteUserProfilePath(userProfilePathId);

        auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_DELETE_SUCCESS, pathName);
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_DELETE_SUCCESS).msgArgs(new String[] {pathName}).build();
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 批量删除
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_DELETE_BATCH_SUCCESS);
        }

        // 删除单条
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_DELETE_SUCCESS)
                    .msgArgs(new String[] {name}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_DELETE_FAIL)
                    .msgArgs(new String[] {name}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }

    public void setBatch(boolean batch) {
        isBatch = batch;
    }
}