package com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.batchtask;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.SnapshotManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.EstSubActionCode;
import com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.UserSnapshotBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 删除桌面快照批量任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月11日
 *
 * @author luojianmo
 */
public class DeleteUserSnapshotBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserSnapshotBatchTaskHandler.class);

    private boolean isBatch = true;

    private SnapshotManageAPI snapshotManageAPI;

    private StateMachineFactory stateMachineFactory;

    private String snapshotName = "";

    private UUID snapshotId = null;

    private CloudDesktopDetailDTO cloudDesktopDetailDTO;

    private UserSnapshotOperationListener userSnapshotOperationListener;

    private String terminalId;

    private UUID deskId;


    public DeleteUserSnapshotBatchTaskHandler(SnapshotManageAPI snapshotManageAPI, Iterator<? extends BatchTaskItem> iterator) {
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

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        UUID itemID = taskItem.getItemID();
        String name = "";
        snapshotId = taskItem.getItemID();
        snapshotName = "";
        try {
            CbbDeskSnapshotDTO snapshotDTO = snapshotManageAPI.findDeskSnapshotInfoById(itemID);
            name = snapshotDTO.getName();
            cloudDesktopDetailDTO = snapshotManageAPI.getDesktopDetailById(snapshotDTO.getDeskId());

            UUID taskId = snapshotManageAPI.deleteDeskSnapshotByBatchTask(itemID);
            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();
            // 记录云桌面日志
            String logDetail = LocaleI18nResolver.resolve(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_SUCCESS_LOG,
                    cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), name);
            snapshotManageAPI.saveOpLog(true, cloudDesktopDetailDTO, logDetail);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_SUCCESS_LOG)
                    .msgArgs(new String[] {cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), name}).build();
        } catch (BusinessException e) {
            if (StringUtils.isEmpty(name)) {
                String errorLogDetail = LocaleI18nResolver.resolve(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_FAIL_LOG_NAME_NULL,
                        cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), e.getI18nMessage());
                snapshotManageAPI.saveOpLog(true, cloudDesktopDetailDTO, errorLogDetail);
                if (userSnapshotOperationListener != null) {
                    userSnapshotOperationListener.onException(EstSubActionCode.EST_SNAPSHOT_DELETE, terminalId, deskId, errorLogDetail);
                }
                throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_FAIL_LOG_NAME_NULL, e,
                        cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), snapshotId + e.getI18nMessage());
            } else {
                String errorLogDetail = LocaleI18nResolver.resolve(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_FAIL_LOG,
                        cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), name, e.getI18nMessage());
                snapshotManageAPI.saveOpLog(true, cloudDesktopDetailDTO, errorLogDetail);
                if (userSnapshotOperationListener != null) {
                    userSnapshotOperationListener.onException(EstSubActionCode.EST_SNAPSHOT_DELETE, terminalId, deskId, errorLogDetail);
                }
                throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_FAIL_LOG, e, cloudDesktopDetailDTO.getUserName(),
                        cloudDesktopDetailDTO.getDesktopName(), snapshotName, e.getI18nMessage());
            }
        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        // 批量删除桌面快照
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_SUCCESS_RESULT);
        }

        // 删除单条桌面快照
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), snapshotName})
                    .batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            if (StringUtils.isEmpty(snapshotName)) {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_FAIL_LOG_NAME_NULL).msgArgs(
                        new String[] {cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName()
                                , snapshotId.toString() + "快照名为空"})
                        .batchTaskStatus(BatchTaskStatus.FAILURE).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_DELETE_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[] {cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), snapshotName})
                        .batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        }
    }

    public UserSnapshotOperationListener getUserSnapshotOperationListener() {
        return userSnapshotOperationListener;
    }

    public void setUserSnapshotOperationListener(UserSnapshotOperationListener userSnapshotOperationListener) {
        this.userSnapshotOperationListener = userSnapshotOperationListener;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

}
