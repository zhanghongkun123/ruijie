package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbDeskAppDiskRestoreAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoUserDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.web.service.AppCenterHelper;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/29 11:53
 *
 * @author coderLee23
 */
public class RevertDesktopAppDiskBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevertDesktopAppDiskBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private boolean isSingleTask;


    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private CbbDeskAppDiskRestoreAPI cbbDeskAppDiskRestoreAPI;

    private String tempDeskName;

    private AppCenterHelper appCenterHelper;

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    private CloudDesktopWebService cloudDesktopWebService;

    public RevertDesktopAppDiskBatchTaskHandler(Collection<DefaultBatchTaskItem> batchTaskItemList) {
        super(batchTaskItemList);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.cbbDeskAppDiskRestoreAPI = SpringBeanHelper.getBean(CbbDeskAppDiskRestoreAPI.class);
        this.userDesktopMgmtAPI = SpringBeanHelper.getBean(UserDesktopMgmtAPI.class);
        this.isSingleTask = batchTaskItemList.size() == 1;
        this.appCenterHelper = SpringBeanHelper.getBean(AppCenterHelper.class);
        this.cbbVDIDeskMgmtAPI = SpringBeanHelper.getBean(CbbVDIDeskMgmtAPI.class);
        this.cbbVDIDeskDiskAPI = SpringBeanHelper.getBean(CbbVDIDeskDiskAPI.class);
        this.cloudDesktopWebService = SpringBeanHelper.getBean(CloudDesktopWebService.class);

    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");
        UUID deskId = batchTaskItem.getItemID();
        tempDeskName = deskId.toString();
        try {
            final RcoUserDesktopDTO rcoUserDesk = userDesktopMgmtAPI.findByDesktopId(deskId);
            tempDeskName = rcoUserDesk.getDesktopName();
            cloudDesktopWebService.checkThirdPartyDesktop(deskId, BusinessKey.RCDC_RCO_APPCENTER_BATCH_RESTORE_APP_DISK_THIRD_PARTY);
            appCenterHelper.checkTestingDesk(deskId);
            cbbDeskAppDiskRestoreAPI.restoreAppDisk(deskId);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_APPCENTER_RESTORE_APP_DISK_ITEM_SUCCESS_LOG, tempDeskName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(BusinessKey.RCDC_RCO_APPCENTER_RESTORE_APP_DISK_ITEM_SUCCESS_LOG).msgArgs(tempDeskName).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_APPCENTER_RESTORE_APP_DISK_ITEM_FAIL_LOG, e, tempDeskName, e.getI18nMessage());
            throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_RESTORE_APP_DISK_ITEM_FAIL_LOG, e, tempDeskName, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (isSingleTask) {
            if (failCount == 0) {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                        .msgKey(BusinessKey.RCDC_RCO_APPCENTER_RESTORE_APP_DISK_ITEM_SUCCESS_LOG).msgArgs(new String[] {tempDeskName}).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                        .msgKey(BusinessKey.RCDC_RCO_APPCENTER_RESTORE_APP_DISK_SINGLE_TASK_FAIL).msgArgs(new String[] {tempDeskName}).build();
            }
        }
        return buildDefaultFinishResult(successCount, failCount, BusinessKey.RCDC_RCO_APPCENTER_BATCH_RESTORE_APP_DISK_RESULT);
    }
}
