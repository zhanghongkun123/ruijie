package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.handler;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.upgrade.module.def.api.BaseApplicationPacketAPI;
import com.ruijie.rcos.base.upgrade.module.def.dto.ApplicationPacketDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.CommonUpgradeBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/19 10:45
 *
 * @author chenl
 */
public class PacketDeleteBatchTaskHandler extends AbstractBatchTaskHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(PacketDeleteBatchTaskHandler.class);

    private BaseApplicationPacketAPI baseApplicationPacketAPI;

    private BaseAuditLogAPI auditLogAPI;

    public PacketDeleteBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    public BaseAuditLogAPI getAuditLogAPI() {
        return auditLogAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setBaseApplicationPacketAPI(BaseApplicationPacketAPI baseApplicationPacketAPI) {
        this.baseApplicationPacketAPI = baseApplicationPacketAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null!");

        UUID packetId = batchTaskItem.getItemID();
        ApplicationPacketDTO applicationPacketDTO = baseApplicationPacketAPI.detailPacket(packetId);
        try {
            baseApplicationPacketAPI.deletePacket(packetId);
        } catch (BusinessException e) {
            LOGGER.error("删除升级包发生异常，异常原因：", e);
            auditLogAPI.recordLog(CommonUpgradeBusinessKey.RCDC_PACKET_BATCH_DELETE_FAIL_LOG, applicationPacketDTO.getName(), e.getI18nMessage());
            throw new BusinessException(CommonUpgradeBusinessKey.RCDC_PACKET_BATCH_DELETE_FAIL_LOG, e, applicationPacketDTO.getName(),
                    e.getI18nMessage());
        }

        auditLogAPI.recordLog(CommonUpgradeBusinessKey.RCDC_PACKET_BATCH_DELETE_SUCCESS_LOG, applicationPacketDTO.getName());
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(CommonUpgradeBusinessKey.RCDC_PACKET_BATCH_DELETE_SUCCESS_LOG)
                .msgArgs(new String[]{applicationPacketDTO.getName()}).build();
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, CommonUpgradeBusinessKey.RCDC_PACKET_BATCH_DELETE_TASK_RESULT);
    }
}
