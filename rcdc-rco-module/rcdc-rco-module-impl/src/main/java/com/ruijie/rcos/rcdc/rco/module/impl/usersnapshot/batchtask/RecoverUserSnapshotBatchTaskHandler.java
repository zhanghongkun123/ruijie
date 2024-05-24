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
import java.util.Map;
import java.util.UUID;

/**
 * Description: 恢复桌面快照任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月11日
 *
 * @author luojianmo
 */
public class RecoverUserSnapshotBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecoverUserSnapshotBatchTaskHandler.class);

    private boolean isBatch = true;

    private Map<UUID, Boolean> snapshotShutdownTypeMap;

    private SnapshotManageAPI snapshotManageAPI;

    private String snapshotName;

    private StateMachineFactory stateMachineFactory;

    private UUID deskSnapshotId;

    private CloudDesktopDetailDTO cloudDesktopDetailDTO;

    private UserSnapshotOperationListener userSnapshotOperationListener;

    private String terminalId;

    private UUID deskId;



    public RecoverUserSnapshotBatchTaskHandler(SnapshotManageAPI snapshotManageAPI, Iterator<? extends BatchTaskItem> iterator) {
        super(iterator);
        Assert.notNull(snapshotManageAPI, "the snapshotManageAPI is null.");
        this.snapshotManageAPI = snapshotManageAPI;
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }


    public void setSnapshotShutdownTypeMap(Map<UUID, Boolean> snapshotShutdownTypeMap) {
        this.snapshotShutdownTypeMap = snapshotShutdownTypeMap;
    }


    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }


    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        deskSnapshotId = taskItem.getItemID();
        try {
            CbbDeskSnapshotDTO cbbDeskSnapshotDTO = snapshotManageAPI.findDeskSnapshotInfoById(deskSnapshotId);
            snapshotName = cbbDeskSnapshotDTO.getName();
            cloudDesktopDetailDTO = snapshotManageAPI.getDesktopDetailById(cbbDeskSnapshotDTO.getDeskId());

            UUID taskId = snapshotManageAPI.recoverDeskSnapshotByBatchTask(deskSnapshotId, true, snapshotShutdownTypeMap.get(deskSnapshotId));

            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();
            LOGGER.info("用户自定义恢复云桌面【{}】快照【{}】成功", cbbDeskSnapshotDTO.getDeskId(), snapshotName);
            String logDetail = LocaleI18nResolver.resolve(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_SUCCESS_LOG,
                    cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), snapshotName);
            snapshotManageAPI.saveOpLog(true, cloudDesktopDetailDTO, logDetail);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_SUCCESS_LOG)
                    .msgArgs(new String[] {cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), snapshotName}).build();
        } catch (BusinessException e) {
            if (StringUtils.isEmpty(snapshotName)) {
                LOGGER.error("用户自定义恢复云桌面快照【{}】失败", deskSnapshotId, e);
                String errorLogDetail = LocaleI18nResolver.resolve(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_FAIL_LOG_NAME_NULL,
                        cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), e.getI18nMessage());
                snapshotManageAPI.saveOpLog(true, cloudDesktopDetailDTO, errorLogDetail);
                if (userSnapshotOperationListener != null) {
                    userSnapshotOperationListener.onException(EstSubActionCode.EST_SNAPSHOT_RECOVER, terminalId, deskId, errorLogDetail);
                }
                throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_FAIL_LOG_NAME_NULL, e,
                        cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), e.getI18nMessage());
            } else {
                LOGGER.error("用户自定义恢复云桌面快照【{}】失败", snapshotName, e);
                String errorLogDetail = LocaleI18nResolver.resolve(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_FAIL_LOG,
                        cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), snapshotName, e.getI18nMessage());
                snapshotManageAPI.saveOpLog(true, cloudDesktopDetailDTO, errorLogDetail);
                if (userSnapshotOperationListener != null) {
                    userSnapshotOperationListener.onException(EstSubActionCode.EST_SNAPSHOT_RECOVER, terminalId, deskId, errorLogDetail);
                }
                throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_FAIL_LOG, e, snapshotName, e.getI18nMessage());
            }
        }

    }



    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        // 恢复创建桌面快照
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_SUCCESS_RESULT);
        }

        // 恢复单条桌面快照
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), snapshotName})
                    .batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            if (StringUtils.isEmpty(snapshotName)) {

                return DefaultBatchTaskFinishResult.builder().msgKey(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_SINGLE_FAIL_RESULT_NAME_NULL)
                        .msgArgs(
                                new String[] {cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), deskSnapshotId.toString()})
                        .batchTaskStatus(BatchTaskStatus.FAILURE).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_SINGLE_FAIL_RESULT)
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
