package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.imagedriver;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDriverMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUploadImageDriverDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractSingleTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;

/**
 *
 * Description: 上传镜像驱动ISO
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年4月08日
 *
 * @author nt
 */
public class UploadDriverIsoBatchTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadDriverIsoBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbImageDriverMgmtAPI cbbImageDriverMgmtAPI;

    private ChunkUploadFile file;

    private String note;

    public UploadDriverIsoBatchTaskHandler(ChunkUploadFile file, BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
            CbbImageDriverMgmtAPI cbbImageDriverMgmtAPI) {
        super(batchTaskItem);
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");
        Assert.notNull(cbbImageDriverMgmtAPI, "cbbImageDriverMgmtAPI is not null");
        Assert.notNull(file, "file is not null");

        this.file = file;
        this.auditLogAPI = auditLogAPI;
        this.cbbImageDriverMgmtAPI = cbbImageDriverMgmtAPI;
    }

    /**
     * 设置note
     *
     * @param note note
     */
    public void setNote(String note) {
        Assert.hasText(note, "note can not be blank");

        this.note = note;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        String fileName = file.getFileName();
        CbbUploadImageDriverDTO request = new CbbUploadImageDriverDTO();
        request.setDriverName(fileName);
        request.setFilePath(file.getFilePath());
        request.setFileSize(file.getFileSize());
        request.setNote(note);

        try {
            cbbImageDriverMgmtAPI.uploadImageDriverFile(request);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_SUCCESS_LOG, fileName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_RESULT_SUCCESS).msgArgs(new String[] {fileName}).build();
        } catch (BusinessException ex) {
            // 上传文件失败
            LOGGER.error("upload image driver iso fail, file label is [{" + file.getFileName() + "}]", ex);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_FAIL_LOG, ex,
                    new String[] {file.getFileName(), ex.getI18nMessage()});
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_RESULT_FAIL, ex, fileName, ex.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_TASK_SUCCESS).msgArgs(new String[] {file.getFileName()})
                    .build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_TASK_FAIL).msgArgs(new String[] {file.getFileName()})
                    .build();
        }
    }
}
