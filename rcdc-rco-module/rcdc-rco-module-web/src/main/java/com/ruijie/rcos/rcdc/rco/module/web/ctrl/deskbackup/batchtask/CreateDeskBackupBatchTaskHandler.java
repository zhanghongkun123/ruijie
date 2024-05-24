package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.dto.CbbCreateDeskBackupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskBackupAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.DesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 创建桌面备份批量任务处理器
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/11
 *
 * @author wuShengQiang
 */
public class CreateDeskBackupBatchTaskHandler extends AbstractBatchTaskHandler {

    private BaseAuditLogAPI auditLogAPI;

    private boolean isBatch = true;

    private String backupName;

    private DeskBackupAPI deskBackupAPI;

    private StateMachineFactory stateMachineFactory;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private final CloudDesktopWebService cloudDesktopWebService;

    private UUID deskId;

    private UUID extStorageId;

    public CreateDeskBackupBatchTaskHandler(DeskBackupAPI deskBackupAPI, UserDesktopMgmtAPI userDesktopMgmtAPI,
                                            Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI,
                                            CloudDesktopWebService cloudDesktopWebService) {

        super(iterator);
        Assert.notNull(deskBackupAPI, "the deskBackupAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");
        this.deskBackupAPI = deskBackupAPI;
        this.auditLogAPI = auditLogAPI;
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
        this.cloudDesktopWebService = cloudDesktopWebService;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        deskId = taskItem.getItemID();
        try {
            cloudDesktopWebService.checkThirdPartyDesktop(deskId, DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_SINGLE_FAIL_THIRD_PARTY);
            CbbCreateDeskBackupDTO cbbCreateDeskBackupDTO = new CbbCreateDeskBackupDTO();
            UUID taskId = UUID.randomUUID();
            cbbCreateDeskBackupDTO.setTaskId(taskId);
            cbbCreateDeskBackupDTO.setDeskBackupId(UUID.randomUUID());
            cbbCreateDeskBackupDTO.setDeskId(deskId);
            cbbCreateDeskBackupDTO.setName(backupName);
            cbbCreateDeskBackupDTO.setExtStorageId(extStorageId);
            deskBackupAPI.createDeskBackup(cbbCreateDeskBackupDTO);

            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();

            //处理待变更镜像模板
            userDesktopMgmtAPI.doWaitUpdateDesktopImage(new DesktopImageUpdateDTO(deskId));
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_SUCCESS_LOG, backupName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_SUCCESS_LOG).msgArgs(new String[]{backupName}).build();
        } catch (BusinessException e) {
            if (StringUtils.isEmpty(backupName)) {
                auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_FAIL_LOG_NAME_NULL, e.getI18nMessage());
                throw new BusinessException(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_FAIL_LOG_NAME_NULL, e, e.getI18nMessage());
            } else {
                auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_FAIL_LOG, backupName, e.getI18nMessage());
                throw new BusinessException(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_FAIL_LOG, e, backupName, e.getI18nMessage());
            }
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 批量创建桌面快照
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_SUCCESS_RESULT);
        }

        // 创建单条桌面快照
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[]{backupName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            if (StringUtils.isEmpty(backupName)) {

                return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_SINGLE_FAIL_RESULT_NAME_NULL)
                        .msgArgs(new String[]{deskId.toString()}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CREATE_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[]{backupName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        }
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    public String getBackupName() {
        return backupName;
    }

    public void setBackupName(String backupName) {
        this.backupName = backupName;
    }

    public void setExtStorageId(UUID extStorageId) {
        this.extStorageId = extStorageId;
    }

    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }
}
