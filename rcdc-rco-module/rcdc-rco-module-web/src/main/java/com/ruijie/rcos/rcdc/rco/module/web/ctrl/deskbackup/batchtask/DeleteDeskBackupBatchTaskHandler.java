package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskBackupAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.dto.CbbDeleteDeskBackupDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.dto.CbbDeskBackupDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: DeleteDeskBackupBatchTaskHandler
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/12
 *
 * @author wuShengQiang
 */
public class DeleteDeskBackupBatchTaskHandler extends AbstractBatchTaskHandler {

    private BaseAuditLogAPI auditLogAPI;

    private boolean isBatch = true;

    private StateMachineFactory stateMachineFactory;

    private CbbVDIDeskBackupAPI cbbVDIDeskBackupAPI;

    private String backupName = "";

    private UUID deskBackupId;

    public DeleteDeskBackupBatchTaskHandler(CbbVDIDeskBackupAPI cbbVDIDeskBackupAPI, Iterator<? extends BatchTaskItem> iterator,
                                            BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(cbbVDIDeskBackupAPI, "the cbbVDIDeskBackupAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");
        this.cbbVDIDeskBackupAPI = cbbVDIDeskBackupAPI;
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        deskBackupId = batchTaskItem.getItemID();
        UUID itemID = batchTaskItem.getItemID();
        String name = "";
        try {
            CbbDeskBackupDTO deskBackupDTO = cbbVDIDeskBackupAPI.findDeskBackupInfoById(itemID);
            backupName = deskBackupDTO.getName();
            name = deskBackupDTO.getName();

            UUID taskId = UUID.randomUUID();
            CbbDeleteDeskBackupDTO cbbDeleteDeskBackupDTO = new CbbDeleteDeskBackupDTO();
            cbbDeleteDeskBackupDTO.setDeskBackupId(itemID);
            cbbDeleteDeskBackupDTO.setTaskId(taskId);
            cbbDeleteDeskBackupDTO.setDeskId(deskBackupDTO.getDeskId());
            cbbVDIDeskBackupAPI.deleteDeskBackup(cbbDeleteDeskBackupDTO);

            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();

            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_SUCCESS_LOG, name);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_SUCCESS_LOG).msgArgs(new String[] {name}).build();
        } catch (BusinessException e) {
            if (StringUtils.isEmpty(name)) {
                auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_FAIL_LOG_NAME_NULL, e.getI18nMessage());
                throw new BusinessException(DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_FAIL_LOG_NAME_NULL, e, e.getI18nMessage());
            } else {
                auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_FAIL_LOG, name, e.getI18nMessage());
                throw new BusinessException(DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_FAIL_LOG, e, name, e.getI18nMessage());
            }
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        // 批量删除桌面备份
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_SUCCESS_RESULT);
        }

        // 删除单条桌面备份
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {backupName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            if (StringUtils.isEmpty(backupName)) {
                return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[] {deskBackupId.toString()}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_DELETE_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[] {backupName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        }
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }
}
