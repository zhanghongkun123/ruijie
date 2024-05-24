package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.batchtask;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcdcThemeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ThemePictureTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme.UploadPictureRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.RcoBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractSingleTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/21 14:16
 *
 * @author conghaifeng
 */
public class UploadRcdcAdminLogoBatchTaskHandler extends AbstractSingleTaskHandler {


    private BaseAuditLogAPI auditLogAPI;

    private ChunkUploadFile file;

    private RcdcThemeAPI rcdcThemeAPI;

    public UploadRcdcAdminLogoBatchTaskHandler(ChunkUploadFile file, BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
                                               RcdcThemeAPI rcdcThemeAPI) {
        super(batchTaskItem);
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");
        Assert.notNull(rcdcThemeAPI, "rcdcThemeAPI is not null");
        Assert.notNull(file, "file is not null");

        this.file = file;
        this.auditLogAPI = auditLogAPI;
        this.rcdcThemeAPI = rcdcThemeAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        UploadPictureRequest request = new UploadPictureRequest(file.getFilePath(), ThemePictureTypeEnum.RCDC_ADMIN_LOGO);
        try {
            rcdcThemeAPI.uploadPicture(request);
            auditLogAPI.recordLog(RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_ADMIN_LOGO_SUCCESS_LOG, file.getFileName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_ADMIN_LOGO_RESULT_SUCCESS).msgArgs(new String[] {file.getFileName()}).build();
        } catch (BusinessException ex) {
            auditLogAPI.recordLog(RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_ADMIN_LOGO_FAIL_LOG, ex, file.getFileName(), ex.getI18nMessage());
            throw new BusinessException(RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_ADMIN_LOGO_RESULT_FAIL, ex, file.getFileName(), ex.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_ADMIN_LOGO_TASK_SUCCESS).msgArgs(new String[] {file.getFileName()}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_ADMIN_LOGO_TASK_FAIL).msgArgs(new String[] {file.getFileName()}).build();
        }
    }
}
