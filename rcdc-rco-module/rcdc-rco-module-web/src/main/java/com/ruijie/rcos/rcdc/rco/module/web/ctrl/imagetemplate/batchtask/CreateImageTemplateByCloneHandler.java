package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.batchtask;

import java.util.Objects;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktopspec.utils.DeskSpecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbConfigVmForEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbCreateImageTemplateByCloneExistDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AdminDataPermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateAdminDataPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.ConfigVmForEditImageTemplateWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.dto.CreateImageTemplateByCloneDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.request.CreateImageTemplateByCloneHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.util.CapacityUnitUtils;
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
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * Description: 复制镜像任务处理器
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年4月1日
 *
 * @author wjp
 */
public class CreateImageTemplateByCloneHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateImageTemplateByCloneHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private CmsUpgradeAPI cmsUpgradeAPI;

    private AdminDataPermissionAPI adminDataPermissionAPI;

    private String imageName = "";

    public CreateImageTemplateByCloneHandler(CreateImageTemplateByCloneHandlerRequest request) {
        super(request.getBatchTaskItemIterator());
        this.auditLogAPI = request.getAuditLogAPI();
        this.cbbImageTemplateMgmtAPI = request.getCbbImageTemplateMgmtAPI();
        this.cmsUpgradeAPI = request.getCmsUpgradeAPI();
        this.adminDataPermissionAPI = request.getAdminDataPermissionAPI();
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        CreateImageTemplateByCloneBatchTaskItem item = (CreateImageTemplateByCloneBatchTaskItem) batchTaskItem;
        UUID newImageTemplateId = item.getItemID();
        CreateImageTemplateByCloneDTO createImageTemplateByCloneDTO = item.getCreateImageTemplateByCloneDTO();
        ConfigVmForEditImageTemplateWebRequest configVmForEditImageTemplateWebRequest = createImageTemplateByCloneDTO.getAdvancedConfig();
        String localImageName = createImageTemplateByCloneDTO.getImageName();
        CreateAdminDataPermissionRequest createAdminDataPermissionRequest = new CreateAdminDataPermissionRequest();
        AdminDataPermissionDTO dto = new AdminDataPermissionDTO();
        // 提前 这样可以显示出来相关的镜像 管理员ID
        dto.setAdminId(createImageTemplateByCloneDTO.getAdminId());
        // 数据
        dto.setPermissionDataId(String.valueOf(newImageTemplateId));
        // 数据类型
        dto.setPermissionDataType(AdminDataPermissionType.IMAGE);
        createAdminDataPermissionRequest.setAdminDataPermissionDTO(dto);

        try {
            // 检查VOI模板复制: 限制ISO类型未发布的模板复制
            cbbImageTemplateMgmtAPI.checkIDVTemplateFileWhenClone(createImageTemplateByCloneDTO.getId());

            // 创建管理员与镜像的数据权限管理关联关系
            adminDataPermissionAPI.createAdminGroupPermission(createAdminDataPermissionRequest);

            final CbbCreateImageTemplateByCloneExistDTO cbbCreateImageTemplateByCloneExistDTO = new CbbCreateImageTemplateByCloneExistDTO();
            cbbCreateImageTemplateByCloneExistDTO.setOldImageTemplateId(createImageTemplateByCloneDTO.getId());
            cbbCreateImageTemplateByCloneExistDTO.setNewImageTemplateName(localImageName);
            cbbCreateImageTemplateByCloneExistDTO.setNewImageTemplateId(newImageTemplateId);
            cbbCreateImageTemplateByCloneExistDTO.setNote(createImageTemplateByCloneDTO.getNote());
            cbbCreateImageTemplateByCloneExistDTO.setCbbImageType(createImageTemplateByCloneDTO.getCbbImageType());
            cbbCreateImageTemplateByCloneExistDTO.setEnableMultipleVersion(createImageTemplateByCloneDTO.getEnableMultipleVersion());
            cbbCreateImageTemplateByCloneExistDTO.setImageUsage(createImageTemplateByCloneDTO.getImageUsage());

            CbbConfigVmForEditImageTemplateDTO editImageTemplateDTO = buildCbbConfigVmForEditImageTemplateDTO(configVmForEditImageTemplateWebRequest);
            editImageTemplateDTO.setImageTemplateId(newImageTemplateId);
            cbbCreateImageTemplateByCloneExistDTO.setAdvancedConfig(editImageTemplateDTO);
            cbbImageTemplateMgmtAPI.createImageTemplateByCloneExist(cbbCreateImageTemplateByCloneExistDTO);

            String currentCmLauncherVersion = cmsUpgradeAPI.getCmLauncherVersionFromDb(createImageTemplateByCloneDTO.getId());
            if (StringUtils.isNotBlank(currentCmLauncherVersion)) {
                cmsUpgradeAPI.saveCmsIsoRecord(newImageTemplateId, currentCmLauncherVersion);
            }
            String currentUwsLauncherVersion = cmsUpgradeAPI.getUwsLauncherVersionFromDb(createImageTemplateByCloneDTO.getId());
            if (StringUtils.isNotBlank(currentUwsLauncherVersion )) {
                cmsUpgradeAPI.saveUwsIsoRecord(newImageTemplateId, currentUwsLauncherVersion);
            }

            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CLONE_IMAGETEMPLATE_SUCCESS_LOG, localImageName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CLONE_IMAGETEMPLATE_SUCCESS).msgArgs(localImageName).build();
        } catch (BusinessException e) {
            LOGGER.error("复制镜像出错", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CLONE_IMAGETEMPLATE_FAIL_LOG, localImageName, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CLONE_IMAGETEMPLATE_FAIL, e, localImageName, e.getI18nMessage());
        } finally {
            imageName = localImageName;
        }
    }
    
    private CbbConfigVmForEditImageTemplateDTO buildCbbConfigVmForEditImageTemplateDTO(ConfigVmForEditImageTemplateWebRequest request) {
        CbbConfigVmForEditImageTemplateDTO cbbConfigVmForEditImageTemplateDTO = new CbbConfigVmForEditImageTemplateDTO();
        cbbConfigVmForEditImageTemplateDTO.setCpuCoreCount(request.getCpu());
        cbbConfigVmForEditImageTemplateDTO.setDeskNetworkId(request.getNetwork().getId());
        cbbConfigVmForEditImageTemplateDTO.setDiskSize(request.getSystemDisk());
        cbbConfigVmForEditImageTemplateDTO.setExpectIp(request.getNetwork().getIp());
        cbbConfigVmForEditImageTemplateDTO.setMemorySize(CapacityUnitUtils.gb2Mb(request.getMemory()));
        cbbConfigVmForEditImageTemplateDTO.setVgpuInfoDTO(DeskSpecUtils.buildVGpuInfoDTO(request.getVgpuType(), request.getVgpuExtraInfo()));
        cbbConfigVmForEditImageTemplateDTO.setEnableNested(request.getEnableNested());
        cbbConfigVmForEditImageTemplateDTO.setComputerName(request.getComputerName());
        cbbConfigVmForEditImageTemplateDTO.setPlatformId(request.getPlatformId());
        IdLabelEntry cluster = request.getCluster();
        if (Objects.nonNull(cluster)) {
            cbbConfigVmForEditImageTemplateDTO.setClusterId(cluster.getId());
        }
        IdLabelEntry storagePool = request.getStoragePool();
        if (Objects.nonNull(storagePool)) {
            cbbConfigVmForEditImageTemplateDTO.setStoragePoolId(storagePool.getId());
        }

        //开启D盘
        cbbConfigVmForEditImageTemplateDTO.setImageDiskList(request.getImageDiskList());

        if (!CollectionUtils.isEmpty(request.getImageDriverList())) {
            cbbConfigVmForEditImageTemplateDTO.setDriverIdArr(request.getImageDriverList().toArray(new UUID[0]));
        }
        return cbbConfigVmForEditImageTemplateDTO;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_CLONE_TASK_SUCCESS).msgArgs(new String[] {imageName}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_CLONE_TASK_FAIL).msgArgs(new String[] {imageName}).build();
        }
    }
}
