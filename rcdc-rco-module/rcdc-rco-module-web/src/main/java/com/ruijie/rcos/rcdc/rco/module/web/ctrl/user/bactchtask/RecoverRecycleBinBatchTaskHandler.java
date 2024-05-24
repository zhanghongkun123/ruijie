package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * 
 * Description: 回收站恢复批量任务
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月17日
 * 
 * @author Ghang
 */
public class RecoverRecycleBinBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecoverRecycleBinBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserRecycleBinMgmtAPI recycleBinMgmtAPI;

    private String deskName;

    String desktopType = Constants.CLOUD_DESKTOP;

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    public RecoverRecycleBinBatchTaskHandler(UserRecycleBinMgmtAPI recycleBinMgmtAPI, Iterator<? extends BatchTaskItem> iterator,
                                             BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.recycleBinMgmtAPI = recycleBinMgmtAPI;
    }

    public RecoverRecycleBinBatchTaskHandler(UserRecycleBinMgmtAPI recycleBinMgmtAPI, Iterator<? extends BatchTaskItem> iterator,
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

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (deskName != null) {
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_SINGLE_SUC)
                        .msgArgs(new String[] {desktopType, deskName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_SINGLE_FAIL)
                        .msgArgs(new String[] {desktopType, deskName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            if (desktopType.equals(Constants.APP_CLOUD_DESKTOP)) {
                return buildDefaultFinishResult(successCount, failCount, RcaBusinessKey.RCDC_RCO_APP_RECYCLEBIN_RECOVER_BATCH_RESULT);
            } else {
                return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_BATCH_RESULT);
            }
        }
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        return recover(taskItem.getItemID());
    }

    private DefaultBatchTaskItemResult recover(UUID deskId) throws BusinessException {
        String logName;
        if (deskName != null) {
            logName = deskName;
        } else {
            logName = obtainDeskName(deskId);
        }

        try {
            recycleBinMgmtAPI.recover(deskId, null);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_SUC_LOG, desktopType, logName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ITEM_SUC_DESC).msgArgs(new String[] {desktopType, logName}).build();
        } catch (BusinessException e) {
            LOGGER.error("恢复回收站云桌面失败，云桌面id=" + deskId.toString(), e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_FAIL_LOG, desktopType, logName, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ITEM_FAIL_DESC, e, desktopType, logName, e.getI18nMessage());
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
