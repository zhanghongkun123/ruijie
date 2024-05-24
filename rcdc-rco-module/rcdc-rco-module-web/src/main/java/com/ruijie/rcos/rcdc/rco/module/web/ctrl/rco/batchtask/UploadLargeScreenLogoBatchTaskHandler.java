package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcdcThemeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ThemePictureTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme.UploadPictureRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.RcoBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import org.springframework.util.Assert;

/**
 * Description: 大屏LOGO上传批处理
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/2
 *
 * @author TD
 */
public class UploadLargeScreenLogoBatchTaskHandler extends AbstractSingleTaskHandler {

    private ChunkUploadFile file;

    private RcdcThemeAPI rcdcThemeAPI;
    
    private BaseAuditLogAPI auditLogAPI;

    public UploadLargeScreenLogoBatchTaskHandler(BatchTaskItem batchTaskItem, 
                                                 ChunkUploadFile file, BaseAuditLogAPI auditLogAPI, RcdcThemeAPI rcdcThemeAPI) {
        super(batchTaskItem);
        this.file = file;
        this.rcdcThemeAPI = rcdcThemeAPI;
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        UploadPictureRequest request = new UploadPictureRequest(file.getFilePath(), ThemePictureTypeEnum.RCDC_LARGE_SCREEN_LOGO);
        try {
            rcdcThemeAPI.uploadPicture(request);
            auditLogAPI.recordLog(RcoBusinessKey.RCDC_RCO_UPLOAD_LARGE_SCREEN_LOGO_SUCCESS_LOG, file.getFileName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcoBusinessKey.RCDC_RCO_UPLOAD_LARGE_SCREEN_LOGO_RESULT_SUCCESS).msgArgs(file.getFileName()).build();
        } catch (BusinessException ex) {
            auditLogAPI.recordLog(RcoBusinessKey.RCDC_RCO_UPLOAD_LARGE_SCREEN_LOGO_FAIL_LOG, ex, file.getFileName(), ex.getI18nMessage());
            throw new BusinessException(RcoBusinessKey.RCDC_RCO_UPLOAD_LARGE_SCREEN_LOGO_RESULT_FAIL, ex, file.getFileName(), ex.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(RcoBusinessKey.RCDC_RCO_UPLOAD_LARGE_SCREEN_LOGO_TASK_SUCCESS).msgArgs(new String[] {file.getFileName()}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(RcoBusinessKey.RCDC_RCO_UPLOAD_LARGE_SCREEN_LOGO_TASK_FAIL).msgArgs(new String[] {file.getFileName()}).build();
        }
    }
}
