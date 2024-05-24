package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbBackupAPI;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbBackupLogAPI;
import com.ruijie.rcos.rcdc.backup.module.def.dto.CbbBackupLogDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STOP_RUNNING_FAIL_RESULT;
import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STOP_RUNNING_TASK_NOT_EXIST;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月13日
 *
 * @author zhanghongkun
 */
public class StopServerBackupSingleTaskHandler extends AbstractSingleTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(StopServerBackupSingleTaskHandler.class);

    private CbbBackupAPI cbbBackupAPI;

    private BaseAuditLogAPI auditLogAPI;

    private CbbBackupLogAPI backupLogAPI;

    private PageQueryBuilderFactory factory;

    public StopServerBackupSingleTaskHandler(CbbBackupAPI cbbBackupAPI, BatchTaskItem batchTaskItem,
                                             BaseAuditLogAPI auditLogAPI, CbbBackupLogAPI backupLogAPI,
                                             PageQueryBuilderFactory factory) {
        super(batchTaskItem);
        Assert.notNull(cbbBackupAPI, "the cbbBackupAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");

        this.cbbBackupAPI = cbbBackupAPI;
        this.auditLogAPI = auditLogAPI;
        this.backupLogAPI = backupLogAPI;
        this.factory = factory;
    }


    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        final PageQueryResponse<CbbBackupLogDTO> response = getBackupLogDTO(batchTaskItem);
        final CbbBackupLogDTO backupLogDTO = response.getItemArr()[0];

        try {
            cbbBackupAPI.stopBackup(batchTaskItem.getItemID());
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STOP_RUNNING_SUCCESS_LOG, backupLogDTO.getBackupType().getName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STOP_RUNNING_SUCCESS_RESULT).msgArgs(new String[] {}).build();
        } catch (BusinessException e) {
            LOGGER.error("停止备份任务失败", e);
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STOP_RUNNING_FAIL_LOG, backupLogDTO.getBackupType().getName(), e.getI18nMessage());
            return DefaultBatchTaskItemResult.fail(RCDC_SERVER_BACKUP_STOP_RUNNING_FAIL_RESULT, backupLogDTO.getBackupType().getName(), e.getI18nMessage());
        } catch (Exception e) {
            LOGGER.error("停止备份任务失败", e);
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STOP_RUNNING_FAIL_LOG, backupLogDTO.getBackupType().getName(), e.getMessage());
            return DefaultBatchTaskItemResult.fail(RCDC_SERVER_BACKUP_STOP_RUNNING_FAIL_RESULT, backupLogDTO.getBackupType().getName(), e.getMessage());
        }
    }

    private PageQueryResponse<CbbBackupLogDTO> getBackupLogDTO(BatchTaskItem batchTaskItem) throws BusinessException {
        final PageQueryBuilderFactory.RequestBuilder requestBuilder = factory.newRequestBuilderForTop1000();
        requestBuilder.eq("backupId", batchTaskItem.getItemID());
        final PageQueryResponse<CbbBackupLogDTO> response = backupLogAPI.pageQuery(requestBuilder.build());
        if (ArrayUtils.isEmpty(response.getItemArr())) {
            throw new BusinessException(RCDC_SERVER_BACKUP_STOP_RUNNING_TASK_NOT_EXIST);
        }
        return response;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STOP_RUNNING_SUCCESS_RESULT)
                    .msgArgs(new String[] {}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STOP_RUNNING_FAIL_RESULT_DESC)
                    .msgArgs(new String[] {}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }
}
