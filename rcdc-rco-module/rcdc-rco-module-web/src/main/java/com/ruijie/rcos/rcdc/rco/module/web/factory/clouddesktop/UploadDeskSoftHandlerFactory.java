package com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSoftMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateDeskSoftDTO;
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
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;

/**
 * 
 * Description: 安装包上传handler
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月17日
 * 
 * @author Ghang
 */
@Service
public class UploadDeskSoftHandlerFactory {

    @Autowired
    private CbbDeskSoftMgmtAPI deskSoftMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    // 前端json中关于文件描述的key
    private static final String FILE_NOTE = "note";

    /**
     * 
     * @param batchTaskItem 批处理入参
     * @param file 上传安装包入参
     * @return UploadDeskSoftHandler 返回参数
     */
    public UploadDeskSoftHandler createHandler(BatchTaskItem batchTaskItem, ChunkUploadFile file) {
        Assert.notNull(batchTaskItem, "batchTaskItem is null");
        Assert.notNull(file, "ChunkUploadFile is null");
        return new UploadDeskSoftHandler(batchTaskItem, auditLogAPI, file);
    }

    /**
     * Description: Function Description
     * Copyright: Copyright (c) 2019
     * Company: Ruijie Co., Ltd.
     * Create Time: 2019年1月18日
     * 
     * @author Ghang
     */
    private class UploadDeskSoftHandler extends AbstractSingleTaskHandler {

        private ChunkUploadFile file;

        private BaseAuditLogAPI auditLogAPI;
        
        private BusinessException businessException = null;

        private UploadDeskSoftHandler(BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
                ChunkUploadFile file) {
            super(batchTaskItem);
            this.file = file;
            this.auditLogAPI = auditLogAPI;
        }

        @Override
        public BatchTaskFinishResult onFinish(int successCount, int failCount) {
            if (failCount == 0) {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_TASK_SUCCESS)
                        .msgArgs(new String[] {file.getFileName()}).build();
            } else {
                if (businessException == null) {
                    return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                            .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_TASK_FAIL)
                            .msgArgs(new String[] {file.getFileName()}).build();
                } else {
                    return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                            .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_ITEM_FAIL_DESC)
                            .msgArgs(new String[] {file.getFileName(), businessException.getI18nMessage()}).build();
                }
            }
        }

        @Override
        public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
            try {
                JSONObject json = file.getCustomData();
                String fileDescription = json.getString(FILE_NOTE);
                CbbCreateDeskSoftDTO request = new CbbCreateDeskSoftDTO();
                request.setFileName(file.getFileName());
                request.setFilePath(file.getFilePath());
                request.setNote(fileDescription);
                deskSoftMgmtAPI.createDeskSoft(request);
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_SUCCESS_LOG,
                        file.getFileName());
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_ITEM_SUCCESS_DESC)
                        .msgArgs(new String[] {file.getFileName()}).build();
            } catch (BusinessException e) {
                businessException = e;
                
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_FAIL_LOG, e,
                        file.getFileName(), e.getI18nMessage());
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_UPLOAD_ITEM_FAIL_DESC, e, file.getFileName(),
                        e.getI18nMessage());
            }
        }
    }
}
