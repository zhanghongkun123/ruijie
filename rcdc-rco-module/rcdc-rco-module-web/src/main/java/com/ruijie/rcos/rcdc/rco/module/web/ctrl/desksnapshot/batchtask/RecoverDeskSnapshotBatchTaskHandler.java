package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskSnapshotAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.SnapshotManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.DesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.DeskSnapshotBusinessKey;
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
 * Description: 恢复桌面快照任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月11日
 *
 * @author luojianmo
 */
public class RecoverDeskSnapshotBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecoverDeskSnapshotBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private boolean isBatch = true;

    private String desktopType = Constants.CLOUD_DESKTOP;

    private SnapshotManageAPI snapshotManageAPI;

    private CbbVDIDeskSnapshotAPI cbbVDIDeskSnapshotAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private String snapshotName;

    private StateMachineFactory stateMachineFactory;

    private UUID deskSnapshotId;


    public RecoverDeskSnapshotBatchTaskHandler(SnapshotManageAPI snapshotManageAPI, CbbVDIDeskSnapshotAPI cbbVDIDeskSnapshotAPI,
                                               UserDesktopMgmtAPI userDesktopMgmtAPI,
                                               Iterator<? extends BatchTaskItem> iterator,
                                               BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(cbbVDIDeskSnapshotAPI, "the cbbVDIDeskSnapshotAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");
        Assert.notNull(snapshotManageAPI, "the snapshotManageAPI is null.");
        this.snapshotManageAPI = snapshotManageAPI;
        this.cbbVDIDeskSnapshotAPI = cbbVDIDeskSnapshotAPI;
        this.auditLogAPI = auditLogAPI;
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }


    public String getSnapshotName() {
        return snapshotName;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }

    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        deskSnapshotId = taskItem.getItemID();
        try {
            CbbDeskSnapshotDTO cbbDeskSnapshotDTO = findDeskSnapshotInfoById(deskSnapshotId);
            snapshotName = cbbDeskSnapshotDTO.getName();
            UUID taskId = snapshotManageAPI.recoverDeskSnapshotByBatchTask(deskSnapshotId, false, false);
            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();
            LOGGER.info("恢复云桌面【{}】快照【{}】成功", cbbDeskSnapshotDTO.getDeskId(), snapshotName);
            snapshotManageAPI.saveOpLog(false, new CloudDesktopDetailDTO(),
                    LocaleI18nResolver.resolve(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_RECOVER_SUCCESS_LOG, snapshotName, desktopType));

            //处理待变更镜像模板
            userDesktopMgmtAPI.doWaitUpdateDesktopImage(new DesktopImageUpdateDTO(cbbDeskSnapshotDTO.getDeskId()));

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_RECOVER_SUCCESS_LOG).msgArgs(snapshotName, desktopType).build();
        } catch (BusinessException e) {
            if (StringUtils.isEmpty(snapshotName)) {
                LOGGER.error("恢复云桌面快照【{}】失败", deskSnapshotId, e);
                snapshotManageAPI.saveOpLog(false, new CloudDesktopDetailDTO(),
                        LocaleI18nResolver.resolve(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_RECOVER_FAIL_LOG_NAME_NULL,
                                e.getI18nMessage(), desktopType));
                throw new BusinessException(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_RECOVER_FAIL_LOG_NAME_NULL, e,
                        e.getI18nMessage(), desktopType);
            } else {
                LOGGER.error("恢复云桌面快照【{}】失败", snapshotName, e);
                snapshotManageAPI.saveOpLog(false, new CloudDesktopDetailDTO(),
                        LocaleI18nResolver.resolve(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_RECOVER_FAIL_LOG, snapshotName,
                                e.getI18nMessage(), desktopType));
                throw new BusinessException(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_RECOVER_FAIL_LOG, e, snapshotName,
                        e.getI18nMessage(), desktopType);
            }
        }

    }

    private CbbDeskSnapshotDTO findDeskSnapshotInfoById(UUID deskSnapshotId) throws BusinessException {
        CbbDeskSnapshotDTO cbbDeskSnapshotDTO = snapshotManageAPI.findDeskSnapshotInfoById(deskSnapshotId);
        if (cbbDeskSnapshotDTO == null) {
            throw new BusinessException(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_IS_NULL, deskSnapshotId.toString());
        }
        return cbbDeskSnapshotDTO;
    }


    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        // 恢复创建桌面快照
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_RECOVER_SUCCESS_RESULT);
        }

        // 恢复单条桌面快照
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_RECOVER_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[]{snapshotName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            if (StringUtils.isEmpty(snapshotName)) {

                return DefaultBatchTaskFinishResult.builder().msgKey(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_RECOVER_SINGLE_FAIL_RESULT_NAME_NULL)
                        .msgArgs(new String[]{deskSnapshotId.toString()}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_RECOVER_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[]{snapshotName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        }
    }

}
