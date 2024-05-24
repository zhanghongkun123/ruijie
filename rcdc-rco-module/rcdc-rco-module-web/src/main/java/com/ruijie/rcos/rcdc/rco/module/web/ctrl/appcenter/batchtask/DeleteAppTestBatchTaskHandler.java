package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import java.util.Collection;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppTestDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年01月04日
 *
 * @author zhk
 */
public class DeleteAppTestBatchTaskHandler extends AbstractBatchTaskHandler {

    private BaseAuditLogAPI auditLogAPI;

    private boolean isSingleTask;

    private CbbUamAppTestAPI cbbUamAppTestAPI;


    private GeneralPermissionHelper generalPermissionHelper;

    private String testName;

    private SessionContext sessionContext;

    public DeleteAppTestBatchTaskHandler(Collection<? extends BatchTaskItem> batchTaskItemList, SessionContext sessionContext) {
        super(batchTaskItemList);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.cbbUamAppTestAPI = SpringBeanHelper.getBean(CbbUamAppTestAPI.class);
        this.generalPermissionHelper = SpringBeanHelper.getBean(GeneralPermissionHelper.class);
        this.sessionContext = sessionContext;
        this.isSingleTask = batchTaskItemList.size() == 1;

    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem is not null");
        UUID testId = batchTaskItem.getItemID();
        final CbbUamAppTestDTO cbbUamAppTestDTO = cbbUamAppTestAPI.getUamAppTestInfo(testId);

        String tmpTestName = cbbUamAppTestDTO.getName();
        try {
            generalPermissionHelper.checkPermission(sessionContext, cbbUamAppTestDTO.getId(), AdminDataPermissionType.DELIVERY_TEST);
            cbbUamAppTestAPI.deleteAppTestById(testId);
            generalPermissionHelper.deletePermission(testId, AdminDataPermissionType.DELIVERY_TEST);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_APPCENTER_TEST_DELETE_ITEM_SUCCESS_LOG, tmpTestName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(BusinessKey.RCDC_RCO_APPCENTER_TEST_DELETE_ITEM_SUCCESS_LOG).msgArgs(new String[] {tmpTestName}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_APPCENTER_TEST_DELETE_ITEM_FAIL_LOG, tmpTestName, e.getI18nMessage());
            throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_TEST_DELETE_ITEM_FAIL_LOG, e, tmpTestName, e.getI18nMessage());
        } finally {
            testName = tmpTestName;
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (isSingleTask) {
            if (failCount == 0) {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                        .msgKey(BusinessKey.RCDC_RCO_APPCENTER_TEST_DELETE_ITEM_SUCCESS_LOG).msgArgs(new String[] {testName}).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                        .msgKey(BusinessKey.RCDC_RCO_APPCENTER_TEST_DELETE_TASK_FAIL).msgArgs(new String[] {testName}).build();
            }
        }
        return buildDefaultFinishResult(successCount, failCount, BusinessKey.RCDC_RCO_APPCENTER_TEST_BATCH_DELETE_RESULT);
    }
}
