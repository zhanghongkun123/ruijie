package com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbConfigVmForEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbCreateImageTemplateByOsFileDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AdminDataPermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateAdminDataPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.builder.CloudDesktopAPIRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop.request.CreateImageTemplateByOsFileRequest;
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
 * Description: 创建镜像模板handler
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/26 10:18
 *
 * @author conghaifeng
 */
@Service
public class CreateImageTemplateHandlerFactory {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CreateImageTemplateHandlerFactory.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    /**
     * 创建镜像handler构造函数
     *
     * @param batchTaskItem 批处理入参
     * @param auditLogAPI 操作日志入参
     * @param createImageTemplateByOsFileRequest web请求
     * @return 返回handler
     */
    public CreateImageHandlerHandler createHandler(BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
            CreateImageTemplateByOsFileRequest createImageTemplateByOsFileRequest) {
        Assert.notNull(batchTaskItem, "batchTaskItem is null");
        Assert.notNull(auditLogAPI, "auditLogAPI is null");
        Assert.notNull(createImageTemplateByOsFileRequest, "webRequest is null");
        return new CreateImageHandlerHandler(batchTaskItem, createImageTemplateByOsFileRequest, auditLogAPI);
    }

    /**
     * Description:创建镜像模板
     * Copyright: Copyright (c) 2020
     * Company: Ruijie Co., Ltd.
     * Create Time: 2020/3/26 10:18
     *
     * @author conghaifeng
     */
    private class CreateImageHandlerHandler extends AbstractSingleTaskHandler {

        private CreateImageTemplateByOsFileRequest createImageTemplateByOsFileRequest;

        private BaseAuditLogAPI auditLogAPI;

        private String imageName = "";

        private CreateImageHandlerHandler(BatchTaskItem batchTaskItem, CreateImageTemplateByOsFileRequest createImageTemplateByOsFileRequest,
                                          BaseAuditLogAPI auditLogAPI) {
            super(batchTaskItem);
            this.createImageTemplateByOsFileRequest = createImageTemplateByOsFileRequest;
            this.auditLogAPI = auditLogAPI;
        }

        @Override
        public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
            try {
                imageName = createImageTemplateByOsFileRequest.getImageName();
                final CbbCreateImageTemplateByOsFileDTO request =
                        CloudDesktopAPIRequestBuilder.buildCreateImageTemplateByIsoFileRequest(createImageTemplateByOsFileRequest);
                final CbbConfigVmForEditImageTemplateDTO vmConfig = CloudDesktopAPIRequestBuilder
                        .buildConfigVmForEditImageTemplateRequest(createImageTemplateByOsFileRequest.getAdvancedConfig());
                vmConfig.setImageTemplateId(request.getNewImageTemplateId());
                request.setAdvancedConfig(vmConfig);

                // 由于创建的时间等待长 放上面创建镜像数据权限可以直接查看
                createImagePermission(createImageTemplateByOsFileRequest, request);
                // 办公场景默认生成快照
                request.setEnableCreateSnapshot(Boolean.TRUE);

                cbbImageTemplateMgmtAPI.createImageTemplateByOsFile(request);


                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_CREATE_SUCCESS_LOG,
                        createImageTemplateByOsFileRequest.getImageName());
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_CREATE_SUCCESS).msgArgs(imageName).build();
            } catch (BusinessException e) {
                LOGGER.error("创建镜像出错", e);
                String errorMsg = e.getI18nMessage();
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_CREATE_FAIL_LOG, e,
                        createImageTemplateByOsFileRequest.getImageName(), e.getI18nMessage());
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_CREATE_FAIL, e, imageName, errorMsg);
            }
        }

        @Override
        public BatchTaskFinishResult onFinish(int successCount, int failCount) {
            if (failCount == 0) {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_CREATE_TASK_SUCCESS).msgArgs(new String[] {imageName}).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_CREATE_TASK_FAIL).msgArgs(new String[] {imageName}).build();
            }
        }
    }

    /**
     * 创建镜像权限
     * 
     * @param createImageTemplateByOsFileRequest
     * @param request
     */
    private void createImagePermission(CreateImageTemplateByOsFileRequest createImageTemplateByOsFileRequest,
            CbbCreateImageTemplateByOsFileDTO request) {
        CreateAdminDataPermissionRequest createAdminDataPermissionRequest = new CreateAdminDataPermissionRequest();
        AdminDataPermissionDTO dto = new AdminDataPermissionDTO();
        // 管理员ID
        dto.setAdminId(createImageTemplateByOsFileRequest.getAdminId());
        // 数据
        dto.setPermissionDataId(String.valueOf(request.getNewImageTemplateId()));
        // 数据类型
        dto.setPermissionDataType(AdminDataPermissionType.IMAGE);
        createAdminDataPermissionRequest.setAdminDataPermissionDTO(dto);
        // 创建管理员与镜像的数据权限管理关联关系
        adminDataPermissionAPI.createAdminGroupPermission(createAdminDataPermissionRequest);
    }
}
