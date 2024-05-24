package com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.platform.CloudPlatformManageBaseRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.CloudPlatformBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.request.CloudPlatformManageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
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
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

/**
 * Description: 添加云平台批处理任务
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/8
 *
 * @author WuShengQiang
 */
public class AddCloudPlatformBatchTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddCloudPlatformBatchTaskHandler.class);

    private CloudPlatformManageWebRequest request;

    private CloudPlatformManageAPI cloudPlatformManageAPI;

    private BaseAuditLogAPI auditLogAPI;

    public AddCloudPlatformBatchTaskHandler(CloudPlatformManageWebRequest request, BatchTaskItem batchTaskItem) {
        super(batchTaskItem);
        this.request = request;
        this.cloudPlatformManageAPI = SpringBeanHelper.getBean(CloudPlatformManageAPI.class);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        CloudPlatformManageBaseRequest cloudPlatformManageBaseRequest = new CloudPlatformManageBaseRequest();
        BeanUtils.copyProperties(request, cloudPlatformManageBaseRequest);
        try {
            cloudPlatformManageAPI.add(cloudPlatformManageBaseRequest);
            auditLogAPI.recordLog(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_ADD_TASK_SUCCESS, request.getName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_ADD_TASK_SUCCESS).msgArgs(request.getName()).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_ADD_FAIL, e, request.getName(), e.getI18nMessage());
            throw new BusinessException(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_ADD_FAIL, e, request.getName(), e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_ADD_TASK_SUCCESS).msgArgs(new String[]{request.getName()}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_ADD_TASK_FAIL).msgArgs(new String[]{request.getName()}).build();
        }
    }

}
