package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.batchtask;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.SnapshotManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.DeskSnapshotBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import java.util.Iterator;
import java.util.UUID;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Description: 删除桌面快照批量任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月11日
 *
 * @author luojianmo
 */
public class DeleteDeskSnapshotBatchTaskHandler extends AbstractBatchTaskHandler {

    private boolean isBatch = true;

    private SnapshotManageAPI snapshotManageAPI;

    private StateMachineFactory stateMachineFactory;

    private String snapshotName = "";

    private UUID snapshotId = null;

    private String desktopType = Constants.CLOUD_DESKTOP;

    public DeleteDeskSnapshotBatchTaskHandler(SnapshotManageAPI snapshotManageAPI, Iterator<? extends BatchTaskItem> iterator) {
        super(iterator);
        Assert.notNull(snapshotManageAPI, "the snapshotManageAPI is null.");
        this.snapshotManageAPI = snapshotManageAPI;
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        UUID itemID = taskItem.getItemID();
        String name = "";
        snapshotId = taskItem.getItemID();
        snapshotName = "";
        try {
            CbbDeskSnapshotDTO snapshotDTO = snapshotManageAPI.findDeskSnapshotInfoById(itemID);
            snapshotName = snapshotDTO.getName();
            name = snapshotDTO.getName();
            UUID taskId = snapshotManageAPI.deleteDeskSnapshotByBatchTask(itemID);
            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();
            snapshotManageAPI.saveOpLog(false, new CloudDesktopDetailDTO(),
                    LocaleI18nResolver.resolve(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_DELETE_SUCCESS_LOG, name, desktopType));
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_DELETE_SUCCESS_LOG).msgArgs(name, desktopType).build();
        } catch (BusinessException e) {
            if (StringUtils.isEmpty(name)) {
                snapshotManageAPI.saveOpLog(false, new CloudDesktopDetailDTO(),
                        LocaleI18nResolver.resolve(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_DELETE_FAIL_LOG_NAME_NULL,
                                e.getI18nMessage(), desktopType));
                throw new BusinessException(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_DELETE_FAIL_LOG_NAME_NULL, e,
                        e.getI18nMessage(), desktopType);
            } else {
                snapshotManageAPI.saveOpLog(false, new CloudDesktopDetailDTO(),
                        LocaleI18nResolver.resolve(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_DELETE_FAIL_LOG, name,
                                e.getI18nMessage(), desktopType));
                throw new BusinessException(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_DELETE_FAIL_LOG, e, name,
                        e.getI18nMessage(), desktopType);
            }
        }

    }



    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        // 批量删除桌面快照
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_DELETE_SUCCESS_RESULT);
        }

        // 删除单条桌面快照
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_DELETE_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {snapshotName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            if (StringUtils.isEmpty(snapshotName)) {
                return DefaultBatchTaskFinishResult.builder().msgKey(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_DELETE_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[] {snapshotId.toString()}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_DELETE_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[] {snapshotName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        }
    }

}
