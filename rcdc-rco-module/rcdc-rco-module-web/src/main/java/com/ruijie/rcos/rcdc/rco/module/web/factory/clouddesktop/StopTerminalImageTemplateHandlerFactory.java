package com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbCloseTerminalVmDTO;
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
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/20
 *
 * @author songxiang
 */
@Service
public class StopTerminalImageTemplateHandlerFactory {

    protected static final Logger LOGGER = LoggerFactory.getLogger(StartTerminalImageTemplateHandlerFactory.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 克隆镜像handler构造函数
     *
     * @param batchTaskItem 批处理入参
     * @param request web请求
     * @return 返回handler
     */
    public StopTerminalImageTemplateHandler createHandler(BatchTaskItem batchTaskItem, CbbCloseTerminalVmDTO request) {
        Assert.notNull(batchTaskItem, "batchTaskItem is null");
        Assert.notNull(request, "request is null");
        return new StopTerminalImageTemplateHandler(batchTaskItem, request, auditLogAPI);
    }

    /**
     * Description:
     * Copyright: Copyright (c) 2019
     * Company: Ruijie Co., Ltd.
     * Create Time: 2020/2/20
     *
     * @author songxiang
     */
    class StopTerminalImageTemplateHandler extends AbstractSingleTaskHandler {

        private CbbCloseTerminalVmDTO request;

        private BaseAuditLogAPI auditLogAPI;

        private String errorMsg = "";

        private String imageName = "";

        private StopTerminalImageTemplateHandler(BatchTaskItem batchTaskItem, CbbCloseTerminalVmDTO request, BaseAuditLogAPI auditLogAPI) {
            super(batchTaskItem);
            this.request = request;
            this.auditLogAPI = auditLogAPI;
        }

        @Override
        public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
            try {
                imageName = cbbImageTemplateMgmtAPI.getImageTemplateDetail(request.getImageTemplateId()).getImageName();
                cbbImageTemplateMgmtAPI.closeTerminalVm(request);
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CLOSE_TERMINAL_VM_IMAGETEMPLATE_SUCCESS, imageName);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CLOSE_TERMINAL_VM_IMAGETEMPLATE_SUCCESS).msgArgs(imageName).build();
            } catch (BusinessException e) {
                LOGGER.error("关闭终端镜像出错", e);
                errorMsg = e.getI18nMessage();
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CLOSE_TERMINAL_VM_IMAGETEMPLATE_FAIL, e, imageName,
                        e.getI18nMessage());
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CLOSE_TERMINAL_VM_IMAGETEMPLATE_FAIL, e, imageName, errorMsg);
            }
        }

        @Override
        public BatchTaskFinishResult onFinish(int successCount, int failCount) {
            if (failCount == 0) {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CLOSE_TERMINAL_VM_IMAGETEMPLATE_SUCCESS).msgArgs(new String[] {imageName})
                        .build();
            } else {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CLOSE_TERMINAL_VM_IMAGETEMPLATE_FAIL)
                        .msgArgs(new String[] {imageName, errorMsg}).build();
            }
        }
    }

}
