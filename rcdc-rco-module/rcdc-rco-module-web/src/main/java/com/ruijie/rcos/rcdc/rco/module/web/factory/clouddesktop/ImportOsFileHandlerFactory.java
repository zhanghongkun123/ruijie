package com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbOsFileMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.osfile.CbbImportOsFileDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ImageFormat;
import com.ruijie.rcos.rcdc.rco.module.def.constants.FilePathContants;
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
import com.ruijie.rcos.sk.base.filesystem.SkyengineFile;

/**
 * 上传OSISO handler
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019年4月1日 <br>
 * 
 * @author wanmulin
 */
@Service
public class ImportOsFileHandlerFactory {

    @Autowired
    private CbbOsFileMgmtAPI osFileMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 
     * @param batchTaskItem 批处理入参
     * @param webRequest web请求
     * @return 返回handler
     */
    public ImportOsFileHandler createHandler(BatchTaskItem batchTaskItem, CbbImportOsFileDTO webRequest) {
        Assert.notNull(batchTaskItem, "batchTaskItem is null");
        Assert.notNull(webRequest, "webRequest is null");

        return new ImportOsFileHandler(batchTaskItem, auditLogAPI, webRequest);
    }

    /**
     * 上传OSISO
     * <br>
     * Description: Function Description <br>
     * Copyright: Copyright (c) 2017 <br>
     * Company: Ruijie Co., Ltd. <br>
     * Create Time: 2019年4月1日 <br>
     * 
     * @author wanmulin
     */
    private class ImportOsFileHandler extends AbstractSingleTaskHandler {

        private CbbImportOsFileDTO webRequest;

        private BaseAuditLogAPI auditLogAPI;
        
        private BusinessException exception = null;

        private ImportOsFileHandler(BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
                                    CbbImportOsFileDTO webRequest) {
            super(batchTaskItem);
            this.webRequest = webRequest;
            this.auditLogAPI = auditLogAPI;
        }

        @Override
        public BatchTaskFinishResult onFinish(int successCount, int failCount) {
            if (failCount == 0) {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_UPLOAD_TASK_SUCCESS)
                        .msgArgs(new String[] {webRequest.getOsFileName()}).build();
            } else {
                BatchTaskFinishResult result;
                
                if (this.exception == null) {
                    result = DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_UPLOAD_TASK_FAIL)
                        .msgArgs(new String[] {webRequest.getOsFileName()}).build();
                } else {
                    result = DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_UPLOAD_ITEM_FAIL_DESC)
                    .msgArgs(new String[] {webRequest.getOsFileName(), this.exception.getI18nMessage()}).build();
                }
                
                return result;
            }
        }

        @Override
        public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
            try {
                checkIOSFileType(webRequest.getOsFileName());
                osFileMgmtAPI.importOsFile(this.webRequest);
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_UPLOAD_SUCCESS_LOG,
                        webRequest.getOsFileName());
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_UPLOAD_ITEM_SUCCESS_DESC)
                        .msgArgs(new String[] {webRequest.getOsFileName()}).build();
            } catch (BusinessException e) {
                this.exception = e;
                
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_UPLOAD_FAIL_LOG, e,
                        webRequest.getOsFileName(), e.getI18nMessage());
                
                // 立即删除镜像文件
                deleteFile(webRequest.getOsFilePath());
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_UPLOAD_ITEM_FAIL_DESC, e, webRequest.getOsFileName(),
                        e.getI18nMessage());
            }
        }

        private void deleteFile(String filePath) {
            SkyengineFile skyengineFile = new SkyengineFile(filePath);
            skyengineFile.delete(false);            
        }
    }

    // 校验镜像类型是ISO
    private ImageFormat checkIOSFileType(String fileName) throws BusinessException {
        String lowerCaseFileName = fileName.toLowerCase();
        boolean enable = lowerCaseFileName.endsWith(FilePathContants.ISO_TYPE);
        if (!enable) {
            // 镜像文件类型不是ISO
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OS_FILETYPE_NOT_ISO, new String[0]);
        }
        return ImageFormat.ISO;
    }
}
