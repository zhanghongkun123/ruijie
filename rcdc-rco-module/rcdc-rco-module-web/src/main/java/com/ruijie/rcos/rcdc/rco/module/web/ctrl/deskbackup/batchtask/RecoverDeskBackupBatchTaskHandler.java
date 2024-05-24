package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskBackupAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.dto.CbbDeskBackupDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.dto.CbbRestoreDeskBackupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.DesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.batchtask.RecoverDeskSnapshotBatchTaskHandler;
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
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;

/**
 * Description: RecoverDeskBackupBatchTaskHandler
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/17
 *
 * @author wuShengQiang
 */
public class RecoverDeskBackupBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecoverDeskSnapshotBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private boolean isBatch = true;

    private CbbVDIDeskBackupAPI cbbVDIDeskBackupAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private StateMachineFactory stateMachineFactory;

    private String backupName;

    private UUID deskBackupId;

    public RecoverDeskBackupBatchTaskHandler(CbbVDIDeskBackupAPI cbbVDIDeskBackupAPI, UserDesktopMgmtAPI userDesktopMgmtAPI,
                                             Iterator<? extends BatchTaskItem> iterator,
                                             BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.cbbVDIDeskBackupAPI = cbbVDIDeskBackupAPI;
        this.auditLogAPI = auditLogAPI;
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
    }


    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        deskBackupId = taskItem.getItemID();
        try {
            CbbDeskBackupDTO deskBackupDTO = cbbVDIDeskBackupAPI.findDeskBackupInfoById(deskBackupId);
            backupName = deskBackupDTO.getName();
            UUID taskId = UUID.randomUUID();
            CbbRestoreDeskBackupDTO cbbRestoreDeskBackupDTO = new CbbRestoreDeskBackupDTO();
            cbbRestoreDeskBackupDTO.setDeskBackupId(deskBackupId);
            cbbRestoreDeskBackupDTO.setTaskId(taskId);
            cbbRestoreDeskBackupDTO.setDeskId(deskBackupDTO.getDeskId());
            cbbRestoreDeskBackupDTO.setPlatformId(deskBackupDTO.getPlatformId());
            cbbVDIDeskBackupAPI.restoreDeskBackup(cbbRestoreDeskBackupDTO);
            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();
            LOGGER.info("恢复云桌面【{}】备份【{}】成功", deskBackupDTO.getDeskId(), backupName);
            //处理待变更镜像模板
            userDesktopMgmtAPI.doWaitUpdateDesktopImage(new DesktopImageUpdateDTO(deskBackupDTO.getDeskId()));
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_SUCCESS_LOG, backupName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_SUCCESS_LOG).msgArgs(new String[]{backupName}).build();
        } catch (BusinessException e) {
            if (StringUtils.isEmpty(backupName)) {
                LOGGER.error("恢复云桌面备份【{}】失败", deskBackupId, e);
                auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_FAIL_LOG_NAME_NULL, e.getI18nMessage());
                throw new BusinessException(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_FAIL_LOG_NAME_NULL, e, e.getI18nMessage());
            } else {
                LOGGER.error("恢复云桌面备份【{}】失败", backupName, e);
                auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_FAIL_LOG, backupName, e.getI18nMessage());
                throw new BusinessException(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_FAIL_LOG, e, backupName, e.getI18nMessage());
            }
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 批量恢复桌面备份
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_SUCCESS_RESULT);
        }

        // 恢复单条桌面备份
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[]{backupName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            if (StringUtils.isEmpty(backupName)) {

                return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_SINGLE_FAIL_RESULT_NAME_NULL)
                        .msgArgs(new String[]{deskBackupId.toString()}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[]{backupName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        }
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public String getBackupName() {
        return backupName;
    }

}
