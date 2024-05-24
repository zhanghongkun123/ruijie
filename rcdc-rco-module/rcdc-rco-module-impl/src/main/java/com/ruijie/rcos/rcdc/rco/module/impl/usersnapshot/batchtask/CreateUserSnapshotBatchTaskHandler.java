package com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.batchtask;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskSnapshotUserType;
import com.ruijie.rcos.rcdc.rco.module.def.api.SnapshotManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.usersnapshot.CreateDeskSnapshotTaskDTO;
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
 * Description: 用户创建桌面快照任务处理器
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月31日
 *
 * @author lihengjing
 */
public class CreateUserSnapshotBatchTaskHandler extends AbstractBatchTaskHandler {

    private boolean isBatch = true;

    private String deskSnapshotName;

    private UUID deskId;

    private CloudDesktopDetailDTO cloudDesktopDetailDTO;

    private SnapshotManageAPI snapshotManageAPI;

    private StateMachineFactory stateMachineFactory;

    private UUID userId;

    private CbbDeskSnapshotUserType userType;

    private UserSnapshotOperationListener userSnapshotOperationListener;

    private String terminalId;


    public CreateUserSnapshotBatchTaskHandler(SnapshotManageAPI snapshotManageAPI, Iterator<? extends BatchTaskItem> iterator) {
        super(iterator);
        Assert.notNull(snapshotManageAPI, "the snapshotManageAPI is null.");
        this.snapshotManageAPI = snapshotManageAPI;
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    public String getDeskSnapshotName() {
        return deskSnapshotName;
    }

    public void setDeskSnapshotName(String deskSnapshotName) {
        this.deskSnapshotName = deskSnapshotName;
    }

    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setUserType(CbbDeskSnapshotUserType userType) {
        this.userType = userType;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserSnapshotBatchTaskHandler.class);

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        deskId = taskItem.getItemID();
        cloudDesktopDetailDTO = snapshotManageAPI.getDesktopDetailById(deskId);

        try {
            UUID taskId = snapshotManageAPI.createDeskSnapshotByBatchTask(
                    new CreateDeskSnapshotTaskDTO(deskId, deskSnapshotName, this.userId, this.userType));
            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();
            // 记录云桌面日志
            String logDetail = LocaleI18nResolver.resolve(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_SUCCESS_LOG,
                    cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), deskSnapshotName);
            snapshotManageAPI.saveOpLog(true, cloudDesktopDetailDTO, logDetail);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_SUCCESS_LOG)
                    .msgArgs(new String[] {cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), deskSnapshotName}).build();
        } catch (BusinessException e) {
            if (StringUtils.isEmpty(deskSnapshotName)) {
                String errorLogDetail = LocaleI18nResolver.resolve(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_FAIL_LOG_NAME_NULL,
                        cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), e.getI18nMessage());
                snapshotManageAPI.saveOpLog(true, cloudDesktopDetailDTO, errorLogDetail);
                if (userSnapshotOperationListener != null) {
                    userSnapshotOperationListener.onException(EstSubActionCode.EST_SNAPSHOT_CREATE, terminalId, deskId, errorLogDetail);
                }
                throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_FAIL_LOG_NAME_NULL, e,
                        cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), e.getI18nMessage());
            } else {
                String errorLogDetail = LocaleI18nResolver.resolve(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_FAIL_LOG,
                        cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), deskSnapshotName, e.getI18nMessage());
                snapshotManageAPI.saveOpLog(true, cloudDesktopDetailDTO, errorLogDetail);
                if (userSnapshotOperationListener != null) {
                    userSnapshotOperationListener.onException(EstSubActionCode.EST_SNAPSHOT_CREATE, terminalId, deskId, errorLogDetail);
                }
                throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_FAIL_LOG, e, cloudDesktopDetailDTO.getUserName(),
                        cloudDesktopDetailDTO.getDesktopName(), deskSnapshotName, e.getI18nMessage());
            }
        }

    }



    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        // 批量创建桌面快照
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_SUCCESS_RESULT);
        }

        // 创建单条桌面快照
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), deskSnapshotName})
                    .batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            if (StringUtils.isEmpty(deskSnapshotName)) {

                return DefaultBatchTaskFinishResult.builder().msgKey(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_SINGLE_FAIL_RESULT_NAME_NULL)
                        .msgArgs(new String[] {cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName()})
                        .batchTaskStatus(BatchTaskStatus.FAILURE).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_CREATE_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[] {cloudDesktopDetailDTO.getUserName(), cloudDesktopDetailDTO.getDesktopName(), deskSnapshotName})
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

}
