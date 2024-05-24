package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestDetailAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTargetAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTaskAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbAppTestTargetDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbEnterAppTestDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppTestDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppTestDetailDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppResourceTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.ProgressStatusEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.service.AppCenterHelper;
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
 * Description: 完成单桌面测试
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/13
 *
 * @author zhiweiHong
 */
public class EnterDesktopTestBatchTaskHandler extends AbstractBatchTaskHandler {

    private CbbUamAppTestTaskAPI testTaskAPI;

    private CbbUamAppTestAPI testAPI;

    private CbbUamAppTestDetailAPI cbbUamAppTestDetailAPI;

    private CbbUamAppTestTargetAPI cbbUamAppTestTargetAPI;

    private UserDesktopMgmtAPI desktopAPI;

    private BaseAuditLogAPI auditLogAPI;

    private boolean isSingleTask;

    private String testName;

    private String testDesktopName;

    private AppCenterHelper appCenterHelper;

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterDesktopTestBatchTaskHandler.class);


    public EnterDesktopTestBatchTaskHandler(Collection<EnterTestDesktopTaskItem> batchTaskItemList) {
        super(batchTaskItemList);
        this.testTaskAPI = SpringBeanHelper.getBean(CbbUamAppTestTaskAPI.class);
        this.testAPI = SpringBeanHelper.getBean(CbbUamAppTestAPI.class);
        this.desktopAPI = SpringBeanHelper.getBean(UserDesktopMgmtAPI.class);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.appCenterHelper = SpringBeanHelper.getBean(AppCenterHelper.class);
        this.cbbUamAppTestDetailAPI = SpringBeanHelper.getBean(CbbUamAppTestDetailAPI.class);
        this.cbbUamAppTestTargetAPI = SpringBeanHelper.getBean(CbbUamAppTestTargetAPI.class);
        this.isSingleTask = batchTaskItemList.size() == 1;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");

        EnterTestDesktopTaskItem taskItem = (EnterTestDesktopTaskItem) batchTaskItem;

        UUID testId = taskItem.getTestId();
        UUID deskId = taskItem.getDesktopId();
        CbbEnterAppTestDTO request = new CbbEnterAppTestDTO();
        request.setTestId(testId);
        request.setResourceId(taskItem.getDesktopId());
        request.setResourceType(AppResourceTypeEnum.CLOUD_DESKTOP);
        request.setAdminId(taskItem.getAdminId());
        String templateTestName = testId.toString();
        String templateTestDesktopName = deskId.toString();
        try {
            CbbUamAppTestDTO uamAppTestInfo = testAPI.getUamAppTestInfo(testId);
            templateTestName = uamAppTestInfo.getName();
            CloudDesktopDetailDTO deskDetail = desktopAPI.getDesktopDetailById(deskId);
            templateTestDesktopName = deskDetail.getDesktopName();
            appCenterHelper.checkTestingDesk(deskId);
            request.setUserType(deskDetail.getUserType() == null ? IacUserTypeEnum.VISITOR : IacUserTypeEnum.valueOf(deskDetail.getUserType()));
            testTaskAPI.startAppTestAPI(request);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_APPCENTER_TEST_ENTER_TEST_DESKTOP_ITEM_SUCCESS_LOG, templateTestName, templateTestDesktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(BusinessKey.RCDC_RCO_APPCENTER_TEST_ENTER_TEST_DESKTOP_ITEM_SUCCESS_LOG)
                    .msgArgs(templateTestName, templateTestDesktopName).build();
        } catch (BusinessException e) {
            LOGGER.error("进入测试异常", e);
            saveFailInfo(testId, deskId, e.getI18nMessage());
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_APPCENTER_TEST_ENTER_TEST_DESKTOP_ITEM_FAIL_LOG, templateTestName, templateTestDesktopName,
                    e.getI18nMessage());
            throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_TEST_ENTER_TEST_DESKTOP_ITEM_FAIL_LOG, e, templateTestName,
                    templateTestDesktopName, e.getI18nMessage());
        } finally {
            testName = templateTestName;
            testDesktopName = templateTestDesktopName;
        }
    }

    /**
     * 保存失败信息
     */
    private void saveFailInfo(UUID testId, UUID resourceId, String errorMsg) {
        final CbbAppTestTargetDTO cbbAppTestTargetDTO = cbbUamAppTestTargetAPI.findByTestIdAndResourceId(testId, resourceId);
        if (cbbAppTestTargetDTO == null) {
            LOGGER.info("未找到测试任务[{}]下的测试桌面[{}]，无需更新", testId, resourceId);
            return;
        }
        cbbUamAppTestTargetAPI.updateStateAndReason(cbbAppTestTargetDTO.getId(), DesktopTestStateEnum.COMPLETED, errorMsg);
        final List<CbbUamAppTestDetailDTO> testDetailDTOList = cbbUamAppTestDetailAPI.findByTestIdAndResourceId(testId, resourceId);
        if (CollectionUtils.isEmpty(testDetailDTOList)) {
            LOGGER.info("未找到测试任务[{}]和测试桌面[{}]的应用详情，无需更新", testId, resourceId);
            return;
        }
        // 一个测试桌面只有一个应用磁盘
        final CbbUamAppTestDetailDTO detailDTO = testDetailDTOList.get(0);
        cbbUamAppTestDetailAPI.updateState(detailDTO.getId(), ProgressStatusEnum.DELIVERY_FAIL);
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (isSingleTask) {
            if (failCount == 0) {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                        .msgKey(BusinessKey.RCDC_RCO_APPCENTER_TEST_ENTER_TEST_DESKTOP_ITEM_SUCCESS_LOG)
                        .msgArgs(new String[] {testName, testDesktopName}).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                        .msgKey(BusinessKey.RCDC_RCO_APPCENTER_TEST_ENTER_TEST_DESKTOP_TASK_FAIL).msgArgs(new String[] {testName, testDesktopName})
                        .build();
            }
        }
        return buildDefaultFinishResult(successCount, failCount, BusinessKey.RCDC_RCO_APPCENTER_TEST_BATCH_ENTER_TEST_RESULT);
    }

}
