package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
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
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月16日
 * 
 * @author Ghang
 */
public class DeleteRecycleBinBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteRecycleBinBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserRecycleBinMgmtAPI recycleBinMgmtAPI;

    private String deskName;

    private String desktopType = Constants.CLOUD_DESKTOP;

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    private Boolean shouldOnlyDeleteDataFromDb;

    private String prefix;

    public DeleteRecycleBinBatchTaskHandler(UserRecycleBinMgmtAPI recycleBinMgmtAPI, BaseAuditLogAPI auditLogAPI,
                                            Iterator<? extends BatchTaskItem> iterator, Boolean shouldOnlyDeleteDataFromDb) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.recycleBinMgmtAPI = recycleBinMgmtAPI;
        this.shouldOnlyDeleteDataFromDb = shouldOnlyDeleteDataFromDb;
        this.prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);
    }


    public DeleteRecycleBinBatchTaskHandler(UserRecycleBinMgmtAPI recycleBinMgmtAPI, BaseAuditLogAPI auditLogAPI,
                                            Iterator<? extends BatchTaskItem> iterator, String deskName, Boolean shouldOnlyDeleteDataFromDb) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.recycleBinMgmtAPI = recycleBinMgmtAPI;
        this.deskName = deskName;
        this.shouldOnlyDeleteDataFromDb = shouldOnlyDeleteDataFromDb;
        this.prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);
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
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_SINGLE_SUC)
                        .msgArgs(new String[] {deskName, desktopType, prefix}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_SINGLE_FAIL)
                        .msgArgs(new String[] {deskName, desktopType, prefix}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            if (desktopType.equals(Constants.APP_CLOUD_DESKTOP)) {
                return buildDefaultFinishResult(successCount, failCount, RcaBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_APP_BATCH_RESULT);
            } else {
                return WebBatchTaskUtils.buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_BATCH_RESULT, prefix);
            }
        }
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        return deleteRecyleBin(taskItem.getItemID());
    }

    private DefaultBatchTaskItemResult deleteRecyleBin(UUID id) throws BusinessException {
        String logName;
        if (deskName != null) {
            logName = deskName;
        } else {
            logName = obtainDeskName(id);
        }
        try {
            if (Boolean.TRUE.equals(shouldOnlyDeleteDataFromDb)) {
                recycleBinMgmtAPI.deleteDeskFromDb(id);
            } else {
                recycleBinMgmtAPI.delete(new IdRequest(id));
            }
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_SUC_LOG, logName, desktopType, prefix);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_ITEM_SUC_DESC).msgArgs(new String[] {logName, desktopType, prefix})
                    .build();
        } catch (BusinessException e) {
            LOGGER.error("删除回收站云桌面失败，云桌面id=" + id.toString(), e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_FAIL_LOG, logName, e.getI18nMessage(), desktopType, prefix);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_ITEM_FAIL_DESC, e, logName, e.getI18nMessage(), desktopType, prefix);
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
