package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import java.util.Collection;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTargetAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTaskAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UamAppTestAPI;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.AppTestDesktopInfoDTO;
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
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/29 11:53
 *
 * @author coderLee23
 */
public class DeleteAppTestDesktopBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteAppTestDesktopBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbUamAppTestTaskAPI testTaskAPI;

    private CbbUamAppTestTargetAPI  cbbUamAppTestTargetAPI;

    private boolean isSingleTask;

    private UamAppTestAPI uamAppTestAPI;

    private String testName;

    private String testDesktopName;

    public DeleteAppTestDesktopBatchTaskHandler(Collection<DeleteAppTestDesktopTaskItem> batchTaskItemList) {
        super(batchTaskItemList);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.testTaskAPI = SpringBeanHelper.getBean(CbbUamAppTestTaskAPI.class);
        this.uamAppTestAPI = SpringBeanHelper.getBean(UamAppTestAPI.class);
        this.cbbUamAppTestTargetAPI = SpringBeanHelper.getBean(CbbUamAppTestTargetAPI.class);
        this.isSingleTask = batchTaskItemList.size() == 1;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");
        UUID deskId = batchTaskItem.getItemID();
        testDesktopName = deskId.toString();
        DeleteAppTestDesktopTaskItem taskItem = (DeleteAppTestDesktopTaskItem) batchTaskItem;
        UUID testId = taskItem.getTestId();
        testName = testId.toString();
        try {
            final AppTestDesktopInfoDTO appTestDesktopInfoDTO = uamAppTestAPI.findByAppTestIdAndDeskId(testId, deskId);
            if (appTestDesktopInfoDTO == null) {
                throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_TEST_DESKTOP_NOT_EXITS);
            }
            testDesktopName = appTestDesktopInfoDTO.getDesktopName();
            testName = appTestDesktopInfoDTO.getTestName();
            if (appTestDesktopInfoDTO.getTestState() != DesktopTestStateEnum.COMPLETED) {
                throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_TEST_DESKTOP_STATE_NOT_SUPPORT_DELETE);
            }
            cbbUamAppTestTargetAPI.deleteAppTestTarget(testId, deskId);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_APPCENTER_TEST_DESKTOP_DELETE_ITEM_SUCCESS_LOG, testName, testDesktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(BusinessKey.RCDC_RCO_APPCENTER_TEST_DESKTOP_DELETE_ITEM_SUCCESS_LOG).msgArgs(testName, testDesktopName).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_APPCENTER_TEST_DESKTOP_DELETE_ITEM_FAIL_LOG, e, testName, testDesktopName, e.getI18nMessage());
            throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_TEST_DESKTOP_DELETE_ITEM_FAIL_LOG, e, testName, testDesktopName,
                    e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (isSingleTask) {
            if (failCount == 0) {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                        .msgKey(BusinessKey.RCDC_RCO_APPCENTER_TEST_DESKTOP_DELETE_ITEM_SUCCESS_LOG).msgArgs(new String[] {testName, testDesktopName})
                        .build();
            } else {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                        .msgKey(BusinessKey.RCDC_RCO_APPCENTER_TEST_BATCH_DESKTOP_DELETE_FAIL).msgArgs(new String[] {testName, testDesktopName})
                        .build();
            }
        }
        return buildDefaultFinishResult(successCount, failCount, BusinessKey.RCDC_RCO_APPCENTER_TEST_BATCH_DESKTOP_DELETE_RESULT);
    }
}
