package com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.CloseImageTemplateWebRequest;
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
 * Description: CloneImageTemplateHandlerFactory Description
 * Copyright: Copyright (c) 2017
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/04/29
 *
 * @author chixin
 */
@Service
public class StopImageTemplateHandlerFactory {

    protected static final Logger LOGGER = LoggerFactory.getLogger(StopImageTemplateHandlerFactory.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 克隆镜像handler构造函数
     *
     * @param batchTaskItem 批处理入参
     * @param webRequest web请求
     * @return 返回handler
     */
    public StopImageTemplateHandler createHandler(BatchTaskItem batchTaskItem,
            CloseImageTemplateWebRequest webRequest) {
        Assert.notNull(batchTaskItem, "batchTaskItem is null");
        Assert.notNull(webRequest, "webRequest is null");
        return new StopImageTemplateHandler(batchTaskItem, webRequest, auditLogAPI);
    }

    /**
     * 克隆镜像
     * <br>
     * Description: Function Description <br>
     * Copyright: Copyright (c) 2017 <br>
     * Company: Ruijie Co., Ltd. <br>
     * Create Time: 2019年4月29日 <br>
     *
     * @author chixin
     */
    private class StopImageTemplateHandler extends AbstractSingleTaskHandler {

        private CloseImageTemplateWebRequest webRequest;

        private BaseAuditLogAPI auditLogAPI;

        private String imageName = "";

        private StopImageTemplateHandler(BatchTaskItem batchTaskItem, CloseImageTemplateWebRequest webRequest, BaseAuditLogAPI auditLogAPI) {
            super(batchTaskItem);
            this.webRequest = webRequest;
            this.auditLogAPI = auditLogAPI;
        }

        @Override
        public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
            String errorMsg = "";
            try {
                imageName = cbbImageTemplateMgmtAPI.getImageTemplateDetail(webRequest.getId()).getImageName();
                cbbImageTemplateMgmtAPI.closeVm(webRequest.getId(), Boolean.TRUE);
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STOP_VM_SUCCESS_LOG, imageName);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STOP_VM_SUCCESS_LOG).msgArgs(imageName).build();
            } catch (BusinessException e) {
                LOGGER.error("关闭镜像出错", e);
                errorMsg = e.getI18nMessage();
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STOP_VM_FAIL_LOG, e, imageName, e.getI18nMessage());
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STOP_VM_FAIL_LOG, e, imageName, errorMsg);
            }
        }

        @Override
        public BatchTaskFinishResult onFinish(int successCount, int failCount) {
            if (failCount == 0) {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_STOP_TASK_SUCCESS).msgArgs(new String[] {imageName}).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_STOP_TASK_FAIL).msgArgs(new String[] {imageName}).build();
            }
        }
    }

}
