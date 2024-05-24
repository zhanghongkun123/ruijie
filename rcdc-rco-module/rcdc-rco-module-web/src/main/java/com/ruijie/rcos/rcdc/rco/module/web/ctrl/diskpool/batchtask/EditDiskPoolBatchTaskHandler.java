package com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.batchtask;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbDiskPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.DiskPoolBusinessKey;
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
 * Description: 编辑磁盘池批处理任务
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/15
 *
 * @author linke
 */
public class EditDiskPoolBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditDiskPoolBatchTaskHandler.class);

    private CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private CbbDiskPoolDTO diskPoolDTO;

    public EditDiskPoolBatchTaskHandler(List<? extends BatchTaskItem> batchTaskItemList, CbbDiskPoolDTO diskPoolDTO) {
        super(batchTaskItemList);
        this.diskPoolDTO = diskPoolDTO;
        this.cbbDiskPoolMgmtAPI = SpringBeanHelper.getBean(CbbDiskPoolMgmtAPI.class);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        UUID diskPoolId = batchTaskItem.getItemID();
        try {
            cbbDiskPoolMgmtAPI.updateDiskPool(diskPoolDTO, true);

            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_EDIT_ITEM_SUCCESS_DESC, diskPoolDTO.getName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_EDIT_ITEM_SUCCESS_DESC).msgArgs(diskPoolDTO.getName()).build();
        } catch (Exception e) {
            String signName = Objects.nonNull(diskPoolDTO) ? diskPoolDTO.getName() : diskPoolId.toString();
            LOGGER.error("编辑磁盘池[{}]失败", signName, e);
            String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_EDIT_ITEM_FAIL_DESC, signName, message);
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_EDIT_ITEM_FAIL_DESC, e, signName, message);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_EDIT_ITEM_SUCCESS_DESC)
                    .msgArgs(new String[]{diskPoolDTO.getName()}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_EDIT_FAIL)
                    .msgArgs(new String[]{diskPoolDTO.getName()}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }

}
