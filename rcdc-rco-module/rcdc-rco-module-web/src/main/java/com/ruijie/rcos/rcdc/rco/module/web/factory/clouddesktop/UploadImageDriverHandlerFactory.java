package com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/26
 *
 * @author songxiang
 */
@Service
public class UploadImageDriverHandlerFactory {

    @Autowired
    private CbbImageDriverMgmtAPI cbbImageDriverMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 创建批任务处理器:
     * 
     * @param batchTaskItem 批任务项
     * @param request 创建驱动的请求
     * @return 上传驱动批任务Handler
     */
    public UploadImageDriverBatchHandler createHandler(BatchTaskItem batchTaskItem, CbbUploadImageDriverDTO request) {
        Assert.notNull(batchTaskItem, "batchTaskItem must not be null");
        Assert.notNull(request, "request must not be null");
        return new UploadImageDriverBatchHandler(batchTaskItem, auditLogAPI, request);
    }

    /**
     * 批任务处理类
     */
    protected class UploadImageDriverBatchHandler extends AbstractSingleTaskHandler {

        private CbbUploadImageDriverDTO request;

        private BaseAuditLogAPI auditLogAPI;

        private UploadImageDriverBatchHandler(BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
                                              CbbUploadImageDriverDTO request) {
            super(batchTaskItem);
            this.request = request;
            this.auditLogAPI = auditLogAPI;
        }

        @Override
        public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
            Assert.notNull(batchTaskItem, "batchTaskItem must not be null");
            try {
                cbbImageDriverMgmtAPI.uploadImageDriverFile(request);
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_SUCCESS, request.getDriverName());
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_SUCCESS).msgArgs(new String[] {request.getDriverName()})
                        .build();
            } catch (BusinessException e) {
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_FAIL, e, request.getDriverName(),
                        e.getI18nMessage());
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_FAIL, e, request.getDriverName(),
                        e.getI18nMessage());
            }
        }

        @Override
        public BatchTaskFinishResult onFinish(int success, int failCount) {
            if (failCount == 0) {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_SUCCESS).msgArgs(new String[] {request.getDriverName()})
                        .build();
            } else {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_TASK_FAIL)
                        .msgArgs(new String[] {request.getDriverName()}).build();
            }
        }
    }
}
