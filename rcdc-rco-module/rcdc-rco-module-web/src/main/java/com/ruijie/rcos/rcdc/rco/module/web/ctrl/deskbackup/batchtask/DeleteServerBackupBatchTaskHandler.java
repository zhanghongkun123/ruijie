package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbBackupDetailAPI;
import com.ruijie.rcos.rcdc.backup.module.def.dto.CbbBackupDetailDTO;
import com.ruijie.rcos.rcdc.backup.module.def.enums.BackupStateEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.util.DateUtil;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月22日
 *
 * @author zhanghongkun
 */

public class DeleteServerBackupBatchTaskHandler extends AbstractBatchTaskHandler {


    private CbbBackupDetailAPI cbbBackupDetailAPI;

    private BaseAuditLogAPI auditLogAPI;


    public DeleteServerBackupBatchTaskHandler(CbbBackupDetailAPI cbbBackupDetailAPI, Iterator<? extends BatchTaskItem> iterator,
            BaseAuditLogAPI auditLogAPI) {

        super(iterator);
        Assert.notNull(cbbBackupDetailAPI, "the cbbBackupDetailAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");

        this.cbbBackupDetailAPI = cbbBackupDetailAPI;
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "BatchTaskItem can not be null");

        UUID resourceId = batchTaskItem.getItemID();
        // 以创建时间排序，从最早的历史记录开始删除
        List<CbbBackupDetailDTO> cbbBackupDetailDTOList = cbbBackupDetailAPI.getByResourceId(resourceId);
        List<CbbBackupDetailDTO> dtoList =
                cbbBackupDetailDTOList.stream().filter(dto -> dto.getBackupState() == BackupStateEnum.DONE).collect(Collectors.toList());
        // 资源名称各历史记录相同
        String name = CollectionUtils.isEmpty(dtoList) ? String.valueOf(resourceId) : dtoList.get(0).getName();
        try {
            for (CbbBackupDetailDTO cbbBackupDetailDTO : dtoList) {
                UUID detailId = cbbBackupDetailDTO.getId();
                Date backupTime = cbbBackupDetailDTO.getCreateTime();
                final String backupFormatTime = DateUtil.formatDate(backupTime, DateUtil.YYYY_MM_DD_HH24MISS);
                cbbBackupDetailAPI.deleteBackupDetail(detailId);
                auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_RESOURCE_DELETE_SUCCESS_LOG, name, backupFormatTime);
            }
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_RESOURCE_DELETE_SUCCESS).msgArgs(new String[] {name}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_RESOURCE_DELETE_FAIL_LOG, name, e.getI18nMessage());
            throw new BusinessException(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_DETAIL_DELETE_FAIL, e, name, e.getI18nMessage());
        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, DeskBackupBusinessKey.RCDC_SERVER_BACKUP_DELETE_BATCH_SUCCESS_RESULT);
    }

}
