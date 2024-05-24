package com.ruijie.rcos.rcdc.rco.module.web.ctrl.extstorage.batchtask;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbExternalStorageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbLocalExternalStorageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditFileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileGlobalConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.extstorage.ExternalStorageBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
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

/**
 * Description: 删除外置存储批处理任务
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/10
 *
 * @author TD
 */
public class DeleteExternalStorageBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteExternalStorageBatchTaskHandler.class);

    private final BaseAuditLogAPI auditLogAPI;

    private final CbbExternalStorageMgmtAPI cbbExternalStorageMgmtAPI;

    private final AuditFileMgmtAPI auditFileMgmtAPI;

    private String extStorageName;

    public DeleteExternalStorageBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.cbbExternalStorageMgmtAPI = SpringBeanHelper.getBean(CbbExternalStorageMgmtAPI.class);
        this.auditFileMgmtAPI = SpringBeanHelper.getBean(AuditFileMgmtAPI.class);
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (Objects.nonNull(extStorageName)) {
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_DELETE_ITEM_SUCCESS_DESC)
                        .msgArgs(new String[] {extStorageName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_DELETE_ITEM_FAIL)
                        .msgArgs(new String[] {extStorageName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return buildDefaultFinishResult(successCount, failCount, ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_DELETE_TASK_RESULT);
        }
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        UUID extStorageId = batchTaskItem.getItemID();
        CbbLocalExternalStorageDTO extStorageDetail = null;
        try {
            extStorageDetail = cbbExternalStorageMgmtAPI.getExternalStorageDetail(extStorageId);
            // 检查外置存储是否可删除
            checkExternalStorageForDelete(extStorageDetail);
            // 删除外置存储
            cbbExternalStorageMgmtAPI.deleteExternalStorage(extStorageId);
            auditLogAPI.recordLog(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_DELETE_ITEM_SUCCESS_DESC, extStorageDetail.getName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS).msgKey
                    (ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_DELETE_ITEM_SUCCESS_DESC).msgArgs(extStorageDetail.getName()).build();
        } catch (Exception e) {
            String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            String name = Objects.nonNull(extStorageDetail) ? extStorageDetail.getName() : extStorageId.toString();
            LOGGER.error(String.format("外置存储[%s]删除失败", name), e);
            auditLogAPI.recordLog(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_DELETE_ITEM_FAIL_DESC, name, message);
            throw new BusinessException(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_DELETE_ITEM_FAIL_DESC, e, name, message);
        }
    }

    public void setExtStorageName(String extStorageName) {
        this.extStorageName = extStorageName;
    }

    private void checkExternalStorageForDelete(CbbLocalExternalStorageDTO extStorageDetail) throws BusinessException {
        AuditFileGlobalConfigDTO auditFileGlobalConfigDTO = auditFileMgmtAPI.obtainAuditFileGlobalConfig();
        // 全局文件流转打开存储且存储被使用
        if (BooleanUtils.toBoolean(auditFileGlobalConfigDTO.getEnableExtStorage()) 
                && Objects.equals(extStorageDetail.getId(), auditFileGlobalConfigDTO.getExternalStorageId())) {
            throw new BusinessException(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_DELETE_IN_USE_ERROR, extStorageDetail.getName());
        }
    }
}
