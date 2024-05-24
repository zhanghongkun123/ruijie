package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTargetAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTaskAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbAppTestTargetDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppTestDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.test.complete.CbbUamCompleteDeskTestDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.test.complete.CbbUamCompleteTestDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.CompleteTestTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
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
public class CompleteDesktopTestBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompleteDesktopTestBatchTaskHandler.class);

    private CbbUamAppTestTaskAPI testTaskAPI;

    private CbbUamAppTestAPI testAPI;

    private CbbDeskMgmtAPI deskMgmtAPI;


    private CbbUamAppTestTargetAPI cbbUamAppTestTargetAPI;

    private static final String DEFAULT_TEST_NAME = "errorTask";

    private BaseAuditLogAPI auditLogAPI;

    private CbbUamAppTestAPI cbbUamAppTestAPI;

    private CompleteTestTaskDTO completeTestTaskDTO;

    public CompleteDesktopTestBatchTaskHandler(Collection<CompleteDesktopTaskItem> batchTaskItemList, CompleteTestTaskDTO completeTestTaskDTO) {
        super(batchTaskItemList);
        this.testTaskAPI = SpringBeanHelper.getBean(CbbUamAppTestTaskAPI.class);
        this.testAPI = SpringBeanHelper.getBean(CbbUamAppTestAPI.class);
        this.deskMgmtAPI = SpringBeanHelper.getBean(CbbDeskMgmtAPI.class);
        this.cbbUamAppTestTargetAPI = SpringBeanHelper.getBean(CbbUamAppTestTargetAPI.class);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.cbbUamAppTestAPI = SpringBeanHelper.getBean(CbbUamAppTestAPI.class);
        this.completeTestTaskDTO = completeTestTaskDTO;
    }


    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");

        CompleteDesktopTaskItem taskItem = (CompleteDesktopTaskItem) batchTaskItem;
        UUID tempTestId = taskItem.getTestId();
        CbbUamCompleteDeskTestDTO request = new CbbUamCompleteDeskTestDTO();
        request.setTestId(tempTestId);
        request.setState(DesktopTestStateEnum.COMPLETED);
        request.setDesktopId(taskItem.getDesktopId());
        request.setReason(completeTestTaskDTO.getReason());
        String testName = String.valueOf(tempTestId);
        String deskName = String.valueOf(taskItem.getDesktopId());
        try {
            CbbDeskDTO deskInfo = deskMgmtAPI.getDeskById(request.getDesktopId());
            deskName = deskInfo.getName();
            CbbUamAppTestDTO uamAppTestInfo = testAPI.getUamAppTestInfo(tempTestId);
            testName = uamAppTestInfo.getName();
            final List<CbbAppTestTargetDTO> testTargetDTOList = cbbUamAppTestTargetAPI.findByResourceIdAndInState(taskItem.getDesktopId(),
                    Collections.singletonList(DesktopTestStateEnum.TESTING));
            if (CollectionUtils.isEmpty(testTargetDTOList)) {
                throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_TEST_COMPLETE_FAIL_DESKTOP_NOT_TESTING, deskName);
            }
            CbbAppTestTargetDTO testTargetDTO = testTargetDTOList.get(0);
            request.setTestId(testTargetDTO.getTestId());
            request.setTargetId(testTargetDTO.getId());
            testTaskAPI.completeTest(request);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_APPCENTER_TEST_COMPLETE_DESKTOP_ITEM_SUCCESS_LOG, testName, deskName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(BusinessKey.RCDC_RCO_APPCENTER_TEST_COMPLETE_DESKTOP_ITEM_SUCCESS_LOG).msgArgs(testName, deskName).build();
        } catch (BusinessException e) {
            LOGGER.error("完成测试异常", e);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_APPCENTER_TEST_COMPLETE_DESKTOP_ITEM_FAIL_LOG, testName, deskName, e.getI18nMessage());
            throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_TEST_COMPLETE_DESKTOP_ITEM_FAIL_LOG, e, testName, deskName,
                    e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0 && completeTestTaskDTO.getBatchDesktop()) {
            CbbUamCompleteTestDTO request = new CbbUamCompleteTestDTO();
            request.setState(completeTestTaskDTO.getState());
            request.setTestId(completeTestTaskDTO.getTestId());
            request.setReason(completeTestTaskDTO.getReason());
            try {
                cbbUamAppTestAPI.completeTest(request);
            } catch (BusinessException e) {
                LOGGER.info("保存测试任务[{}]信息失败,失败原因", completeTestTaskDTO.getTestId(), e.getI18nMessage());
            }
        }
        return buildDefaultFinishResult(successCount, failCount, BusinessKey.RCDC_RCO_APPCENTER_TEST_BATCH_COMPLETE_RESULT);
    }


}
