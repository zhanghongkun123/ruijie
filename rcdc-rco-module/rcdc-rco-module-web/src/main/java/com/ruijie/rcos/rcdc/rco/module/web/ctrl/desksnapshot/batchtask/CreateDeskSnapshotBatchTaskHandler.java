package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskSnapshotAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskSnapshotUserType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSnapshotAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SnapshotManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.DesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.usersnapshot.CreateDeskSnapshotTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.DeskSnapshotBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 管理员WEB创建桌面快照任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月11日
 *
 * @author luojianmo
 */
public class CreateDeskSnapshotBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDeskSnapshotBatchTaskHandler.class);

    private boolean isBatch = true;

    String desktopType = Constants.CLOUD_DESKTOP;

    private SnapshotManageAPI snapshotManageAPI;

    private CbbVDIDeskSnapshotAPI cbbVDIDeskSnapshotAPI;

    private String snapshotName;

    private DeskSnapshotAPI deskSnapshotAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private StateMachineFactory stateMachineFactory;

    private UUID deskId;

    private SessionContext sessionContext;

    private IacAdminMgmtAPI baseAdminMgmtAPI;

    private CloudDesktopWebService cloudDesktopWebService;

    public CreateDeskSnapshotBatchTaskHandler(SnapshotManageAPI snapshotManageAPI, CbbVDIDeskSnapshotAPI cbbVDIDeskSnapshotAPI,
                                              UserDesktopMgmtAPI userDesktopMgmtAPI,
                                              Iterator<? extends BatchTaskItem> iterator,
                                              BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(cbbVDIDeskSnapshotAPI, "the cbbVDIDeskSnapshotAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");
        Assert.notNull(snapshotManageAPI, "the snapshotManageAPI is null.");
        this.snapshotManageAPI = snapshotManageAPI;
        this.cbbVDIDeskSnapshotAPI = cbbVDIDeskSnapshotAPI;
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }

    public void setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        this.cloudDesktopWebService = cloudDesktopWebService;
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public void setDeskSnapshotAPI(DeskSnapshotAPI deskSnapshotAPI) {
        this.deskSnapshotAPI = deskSnapshotAPI;
    }

    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public void setBaseAdminMgmtAPI(IacAdminMgmtAPI baseAdminMgmtAPI) {
        this.baseAdminMgmtAPI = baseAdminMgmtAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        deskId = taskItem.getItemID();

        try {
            cloudDesktopWebService.checkThirdPartyDesktop(deskId, UserBusinessKey.RCDC_RCO_CREATE_DESKTOP_SNAPSHOT_THIRD_PARTY);
            UUID userId = this.sessionContext.getUserId();
            CbbDeskSnapshotUserType userType = CbbDeskSnapshotUserType.ADMIN;

            try {
                baseAdminMgmtAPI.getAdmin(userId);
            } catch (Exception e) {
                userType = CbbDeskSnapshotUserType.USER;
            }

            UUID taskId = snapshotManageAPI.createDeskSnapshotByBatchTask(new CreateDeskSnapshotTaskDTO(deskId, snapshotName, userId, userType));

            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();
            //处理待变更镜像模板
            userDesktopMgmtAPI.doWaitUpdateDesktopImage(new DesktopImageUpdateDTO(deskId));

            snapshotManageAPI.saveOpLog(false, new CloudDesktopDetailDTO(),
                    LocaleI18nResolver.resolve(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_CREATE_SUCCESS_LOG, snapshotName, desktopType));
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_CREATE_SUCCESS_LOG).msgArgs(snapshotName, desktopType).build();
        } catch (BusinessException e) {
            if (StringUtils.isEmpty(snapshotName)) {
                snapshotManageAPI.saveOpLog(false, new CloudDesktopDetailDTO(),
                        LocaleI18nResolver.resolve(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_CREATE_FAIL_LOG_NAME_NULL,
                                e.getI18nMessage(), desktopType));
                throw new BusinessException(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_CREATE_FAIL_LOG_NAME_NULL,
                        e, e.getI18nMessage(), desktopType);
            } else {
                snapshotManageAPI.saveOpLog(false, new CloudDesktopDetailDTO(),
                        LocaleI18nResolver.resolve(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_CREATE_FAIL_LOG, snapshotName,
                                e.getI18nMessage(), desktopType));
                throw new BusinessException(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_CREATE_FAIL_LOG, e, snapshotName,
                        e.getI18nMessage(), desktopType);
            }
        }

    }


    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        // 批量创建桌面快照
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_CREATE_SUCCESS_RESULT);
        }

        // 创建单条桌面快照
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_CREATE_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[]{snapshotName, desktopType}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            if (StringUtils.isEmpty(snapshotName)) {

                return DefaultBatchTaskFinishResult.builder().msgKey(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_CREATE_SINGLE_FAIL_RESULT_NAME_NULL)
                        .msgArgs(new String[]{deskId.toString(), desktopType}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_CREATE_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[]{snapshotName, desktopType}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        }
    }

}
