package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.handler;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.upgrade.module.def.api.BaseApplicationPacketAPI;
import com.ruijie.rcos.base.upgrade.module.def.api.request.ApplicationPacketUploadRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.CommonUpgradeBusinessKey;
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
 * Create Time: 2020/5/19 10:45
 *
 * @author chenl
 */
public class PacketUploadBatchTaskHandler extends AbstractSingleTaskHandler {


    private BaseAuditLogAPI auditLogAPI;

    private BaseApplicationPacketAPI baseApplicationPacketAPI;

    private ChunkUploadFile file;

    public PacketUploadBatchTaskHandler(ChunkUploadFile file, BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
                                        BaseApplicationPacketAPI baseApplicationPacketAPI) {
        super(batchTaskItem);
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");
        Assert.notNull(baseApplicationPacketAPI, "baseApplicationPacketAPI is not null");
        Assert.notNull(file, "file is not null");

        this.file = file;
        this.auditLogAPI = auditLogAPI;
        this.baseApplicationPacketAPI = baseApplicationPacketAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        try {
            ApplicationPacketUploadRequest request = new ApplicationPacketUploadRequest();
            request.setFileName(file.getFileName());
            request.setFilePath(file.getFilePath());
            request.setFileMd5(file.getFileMD5());
            baseApplicationPacketAPI.uploadPacket(request);
            auditLogAPI.recordLog(CommonUpgradeBusinessKey.RCDC_PACKET_UPLOAD_SUCCESS_LOG, file.getFileName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CommonUpgradeBusinessKey.RCDC_PACKET_UPLOAD_SUCCESS_LOG).msgArgs(file.getFileName()).build();
        } catch (BusinessException ex) {
            auditLogAPI.recordLog(CommonUpgradeBusinessKey.RCDC_PACKET_UPLOAD_FAIL_LOG, file.getFileName(), ex.getI18nMessage());
            throw new BusinessException(CommonUpgradeBusinessKey.RCDC_PACKET_UPLOAD_FAIL_LOG, ex, file.getFileName(), ex.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CommonUpgradeBusinessKey.RCDC_PACKET_UPLOAD_TASK_SUCCESS).msgArgs(new String[]{file.getFileName()}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CommonUpgradeBusinessKey.RCDC_PACKET_UPLOAD_TASK_FAIL).msgArgs(new String[]{file.getFileName()}).build();
        }
    }
}
