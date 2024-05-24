package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbPushInstallPackageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSoftMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSoftDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionFileManageAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractSingleTaskHandler;
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
 * Description: 删除单条安装包handler
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年4月08日
 * 
 * @author nt
 */
public class DeleteSingleDeskSoftBatchHandler extends AbstractSingleTaskHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteSingleDeskSoftBatchHandler.class);


    private BaseAuditLogAPI auditLogAPI;

    private CbbDeskSoftMgmtAPI deskSoftMgmtAPI;

    private CbbPushInstallPackageMgmtAPI cbbPushInstallPackageMgmtAPI;

    private FileDistributionFileManageAPI fileDistributionFileManageAPI;

    private String softName = "";

    public DeleteSingleDeskSoftBatchHandler(BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
                                            CbbDeskSoftMgmtAPI deskSoftMgmtAPI, CbbPushInstallPackageMgmtAPI cbbPushInstallPackageMgmtAPI,
                                            FileDistributionFileManageAPI fileDistributionFileManageAPI) {
        super(batchTaskItem);
        this.auditLogAPI = auditLogAPI;
        this.deskSoftMgmtAPI = deskSoftMgmtAPI;
        this.cbbPushInstallPackageMgmtAPI = cbbPushInstallPackageMgmtAPI;
        this.fileDistributionFileManageAPI = fileDistributionFileManageAPI;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_ITEM_SUCCESS_RESULT).msgArgs(new String[] {softName}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_ITEM_FAIL_RESULT).msgArgs(new String[] {softName}).build();
        }
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "item is null");
        UUID softAppId = item.getItemID();
        String logName = softAppId.toString();
        try {
            CbbDeskSoftDTO deskSoftDTO = deskSoftMgmtAPI.getDeskSoft(softAppId);
            logName = deskSoftDTO.getFileName();

            Boolean existsUsed = cbbPushInstallPackageMgmtAPI.existsUsed(softAppId);
            if (Boolean.TRUE.equals(existsUsed)) {
                LOGGER.info("软件安装包[{}]被推送安装包使用无法删除", softAppId);
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_BATCH_DESKSOFT_SOFT_EXISTS_USED_BY_UAM_APP, logName);
            }

            fileDistributionFileManageAPI.existsUsed(softAppId);

            deskSoftMgmtAPI.deleteDeskSoft(softAppId);

            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_ITEM_SUCCESS_DESC, logName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_ITEM_SUCCESS_DESC).msgArgs(new String[] {logName}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_ITEM_FAIL_DESC, e, logName, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_ITEM_FAIL_DESC, e, logName, e.getI18nMessage());
        } finally {
            softName = logName;
        }

    }


}
