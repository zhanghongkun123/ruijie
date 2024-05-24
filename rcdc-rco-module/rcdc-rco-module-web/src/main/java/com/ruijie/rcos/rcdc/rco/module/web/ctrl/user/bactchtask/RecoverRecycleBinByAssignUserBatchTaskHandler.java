package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
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

/**
 * 
 * Description: 指定用户回收站恢复批量任务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年6月16日
 *
 * @author zhuangchenwu
 */
public class RecoverRecycleBinByAssignUserBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecoverRecycleBinByAssignUserBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserRecycleBinMgmtAPI recycleBinMgmtAPI;

    private String deskName;

    private UUID assignUserId;

    private String assignUserName;

    String desktopType = Constants.CLOUD_DESKTOP;

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    public RecoverRecycleBinByAssignUserBatchTaskHandler(UserRecycleBinMgmtAPI recycleBinMgmtAPI, Iterator<? extends BatchTaskItem> iterator,
                                                         BaseAuditLogAPI auditLogAPI, String deskName) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.recycleBinMgmtAPI = recycleBinMgmtAPI;
        this.deskName = deskName;
    }

    public void setCbbVDIDeskMgmtAPI(CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI) {
        this.cbbVDIDeskMgmtAPI = cbbVDIDeskMgmtAPI;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }

    /**
     * 设置指定的用户id
     * 
     * @param assignUserId 指定用户id
     * @return this
     */
    public RecoverRecycleBinByAssignUserBatchTaskHandler setAssignUserId(UUID assignUserId) {
        Assert.notNull(assignUserId, "assignUserId must not be null");
        this.assignUserId = assignUserId;
        return this;
    }

    /**
     * 设置指定的用户id的用户名
     * 
     * @param assignUserName 指定用户id的用户名
     * @return this
     */
    public RecoverRecycleBinByAssignUserBatchTaskHandler setAssignUserName(String assignUserName) {
        Assert.hasText(assignUserName, "assignUserName must not be null");
        this.assignUserName = assignUserName;
        return this;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_SINGLE_SUC)
                    .msgArgs(new String[] {assignUserName, desktopType, deskName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_SINGLE_FAIL)
                    .msgArgs(new String[] {assignUserName, desktopType, deskName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        return recover(taskItem.getItemID());
    }

    private DefaultBatchTaskItemResult recover(UUID deskId) throws BusinessException {
        String oldDeskName;
        if (deskName != null) {
            oldDeskName = deskName;
        } else {
            oldDeskName = obtainDeskName(deskId);
        }

        try {
            recycleBinMgmtAPI.recover(deskId, assignUserId);
            // 获取恢复成后的新的桌面名称
            String newDeskName = obtainDeskName(deskId);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_SUC_LOG, assignUserName, desktopType,
                    oldDeskName, newDeskName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_ITEM_SUC_DESC)
                    .msgArgs(new String[] {assignUserName, oldDeskName, newDeskName, desktopType}).build();
        } catch (BusinessException e) {
            LOGGER.error("指定用户恢复回收站云桌面失败，云桌面id[" + deskId.toString() + "]，指定的用户id[" + assignUserId + "]", e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_FAIL_LOG, assignUserName, oldDeskName,
                    e.getI18nMessage(), desktopType);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_ITEM_FAIL_DESC, e, assignUserName, oldDeskName,
                    e.getI18nMessage(), desktopType);
        }
    }

    private String obtainDeskName(UUID deskId) {
        String resultName = deskId.toString();
        try {
            CbbDeskDTO deskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(deskId);
            resultName = deskDTO.getName();
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面数据失败，桌面id[{}]", deskId, e);
        }
        return resultName;
    }
}
