package com.ruijie.rcos.rcdc.rco.module.web.ctrl.otpcertification.batchtask;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserOtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.otpcertification.OtpCertificationBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年08月01日
 *
 * @author zhanghongkun
 */
public class ResetOtpBatchTaskHandler extends AbstractBatchTaskHandler {

    private UserOtpCertificationAPI userOtpCertificationAPI;

    private BaseAuditLogAPI auditLogAPI;

    private IacUserMgmtAPI userAPI;

    public ResetOtpBatchTaskHandler(UserOtpCertificationAPI userOtpCertificationAPI, Iterator<? extends BatchTaskItem> iterator,
                                    BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(userOtpCertificationAPI, "the userOtpCertificationAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");
        this.userOtpCertificationAPI = userOtpCertificationAPI;
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "BatchTaskItem can not be null");

        UUID userId = batchTaskItem.getItemID();
        IacUserDetailDTO userDetail = userAPI.getUserDetail(userId);
        // 重置用户动态口令
        userOtpCertificationAPI.resetById(userId);
        auditLogAPI.recordLog(OtpCertificationBusinessKey.RCDC_OTP_SECRET_KEY_RESET_SUCCESS, new String[] {userDetail.getUserName()});
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(OtpCertificationBusinessKey.RCDC_OTP_SECRET_KEY_RESET_SUCCESS).msgArgs(new String[] {userDetail.getUserName()}).build();
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, OtpCertificationBusinessKey.RCDC_OTP_SECRET_KEY_RESET_TASK_SUCCESS);
    }

    public void setUserAPI(IacUserMgmtAPI userAPI) {
        this.userAPI = userAPI;
    }
}

