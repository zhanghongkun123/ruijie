package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageDiskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageDiskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.request.CreateAndRunVmForEditImageHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.service.DiskBaseInfoMgmtWebService;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 编辑启动镜像任务处理器
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年2月1日
 *
 * @author wjp
 */
public class CreateAndRunVmForEditImageHandler extends AbstractRunVmForEditImageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateAndRunVmForEditImageHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private CmsUpgradeAPI cmsUpgradeAPI;

    private final CbbUpdateImageTemplateDTO cbbUpdateImageTemplateDTO;

    private final CbbConfigVmForEditImageTemplateDTO vmConfig;

    private String imageName;

    private DiskBaseInfoMgmtWebService webService;

    public CreateAndRunVmForEditImageHandler(CreateAndRunVmForEditImageHandlerRequest request) {
        super(request.getBatchTaskItemIterator(), request.getCmsUpgradeAPI(), request.getCbbImageDriverMgmtAPI());
        this.auditLogAPI = request.getAuditLogAPI();
        this.cbbImageTemplateMgmtAPI = request.getCbbImageTemplateMgmtAPI();
        this.cmsUpgradeAPI = request.getCmsUpgradeAPI();
        this.cbbUpdateImageTemplateDTO = request.getCbbUpdateImageTemplateDTO();
        this.vmConfig = request.getVmConfig();
        this.imageName = request.getCbbUpdateImageTemplateDTO().getImageTemplateName();
        this.webService = request.getWebService();
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        try {
            // 编辑镜像模板
            cbbImageTemplateMgmtAPI.updateImageTemplate(cbbUpdateImageTemplateDTO);
            cbbImageTemplateMgmtAPI.configVmForEditImageTemplate(vmConfig);

            // 启动镜像模板
            CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(taskItem.getItemID());
            imageName = cbbImageTemplateDetailDTO.getImageName();
            CbbImageTemplateStartDTO imageTemplateStartDTO = new CbbImageTemplateStartDTO();
            imageTemplateStartDTO.setImageId(taskItem.getItemID());
            imageTemplateStartDTO.setCdromList(wrapperCdromDTO(cbbImageTemplateDetailDTO));
            imageTemplateStartDTO.setNeedExactMatchVgpu(true);

            /**
             * 此处是因为，前端在已经选择一个磁盘(可以是新建磁盘，也可以是选择一个已有的osFile时候)，
             * 再次编辑（从已发布再次编辑，或者未安装GT再次编辑）
             * 选择一个新的磁盘模版（osFile）的时候，无法将原先的磁盘Id置为空,导致后端无法判断新增或者更新磁盘的行为
             * 强烈不建议，部分逻辑由前端主导，又把一部分逻辑写到后端的行为
             * 建议以后找机会得改掉
             */
            resetImageDiskIdIfNeed(vmConfig, imageTemplateStartDTO.getImageId());


            imageTemplateStartDTO.setImageDiskList(vmConfig.getImageDiskList());

            cbbImageTemplateMgmtAPI.createAndRunVmForEditImageTemplate(imageTemplateStartDTO);
            notifyIfVDITemplate(cbbImageTemplateDetailDTO);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_START_VM_IMAGETEMPLATE_SUCCESS_LOG, imageName);

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_START_VM_IMAGETEMPLATE_SUCCESS).msgArgs(imageName).build();
        } catch (BusinessException e) {
            LOGGER.error("启动虚机出错", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_START_VM_IMAGETEMPLATE_FAIL_LOG, e, imageName, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_START_VM_IMAGETEMPLATE_FAIL, e, imageName, e.getI18nMessage());
        }
    }

    private void notifyIfVDITemplate(CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO) {
        if (cbbImageTemplateDetailDTO.getCbbImageType() == CbbImageType.VDI) {
            webService.remoteUpdateImageDiskDescInfo(cbbImageTemplateDetailDTO.getPlatformId(), cbbImageTemplateDetailDTO.getId(), cbbImageTemplateDetailDTO.getImageName());
        }
    }

    private void resetImageDiskIdIfNeed(CbbConfigVmForEditImageTemplateDTO vmConfig, UUID imageId) throws BusinessException {

        Assert.notNull(imageId, "image id must not null");

        if (vmConfig == null) {
            return;
        }
        if (CollectionUtils.isEmpty(vmConfig.getImageDiskList())) {
            return;
        }

        // 获取旧的数据盘信息
        List<CbbImageDiskInfoDTO> oldImageDiskInfoList = cbbImageTemplateMgmtAPI.getAvailableImageDiskInfoList(imageId);
        if (CollectionUtils.isEmpty(oldImageDiskInfoList)) {
            return;
        }


        // 旧的ID和数据盘信息的映射
        Map<UUID, CbbImageDiskInfoDTO> oldImageDiskIdMapping = oldImageDiskInfoList//
                .stream()//
                .filter(disk -> disk.getImageDiskType() == CbbImageDiskType.DATA && //
                        disk.getImageDiskState() == CbbImageDiskState.AVAILABLE)//
                .collect(Collectors.toMap(CbbImageDiskInfoDTO::getId, Function.identity()));//

        if (MapUtils.isEmpty(oldImageDiskIdMapping)) {
            return;
        }
        vmConfig.getImageDiskList()//
                .stream()//
                .filter(disk -> disk.getImageDiskType() == CbbImageDiskType.DATA)//
                .forEach(newDisk -> { //
                    innerSetDiskIdNull(imageId, oldImageDiskIdMapping, newDisk);
                });
    }

    private void innerSetDiskIdNull(UUID imageId, Map<UUID, CbbImageDiskInfoDTO> oldImageDiskIdMapping, CbbImageDiskDTO newDisk) {
        if (newDisk.getId() == null) {
            return;
        }
        CbbImageDiskInfoDTO oldDiskInfo = oldImageDiskIdMapping.get(newDisk.getId());

        if (oldDiskInfo == null) {
            // 旧的磁盘不存在，新的磁盘传了一个旧的id，把id置空
            LOGGER.info("【镜像D盘变更磁盘ID为空】,镜像id:[{}],旧的磁盘不存在，旧的磁盘id:[{}]存在,将磁盘id置空", imageId, newDisk.getId(), newDisk.getOsIsoFileId());
            newDisk.setId(null);

        } else {
            if (newDisk.getOsIsoFileId() == null && oldDiskInfo.getOsIsoFileId() == null) {
                return;
            }

            if (newDisk.getOsIsoFileId() != null && oldDiskInfo.getOsIsoFileId() != null
                    && newDisk.getOsIsoFileId().equals(oldDiskInfo.getOsIsoFileId())) {
                return;
            }

            // 下面的代码只是为了在日志中记录用户选择的是哪一种情况，可以概括为：newDisk.setId(null)
            // 旧的磁盘存在，旧的选择了一块磁盘模版osFileId，新的磁盘没有选择已有的磁盘模版：
            if (newDisk.getOsIsoFileId() == null && oldDiskInfo.getOsIsoFileId() != null) {
                LOGGER.info("【镜像D盘变更磁盘ID为空】,镜像id:[{}],旧的磁盘id:[{}]存在,旧的选择了一个已有的模版:" + //
                        "osFileId:[{}],新的磁盘没有选择已有的磁盘模版，将磁盘id置空", //
                        imageId, newDisk.getId(), oldDiskInfo.getOsIsoFileId());//

                newDisk.setId(null);
                return;
            }
            // 旧的磁盘存在,旧的没有选择模版，但是新的选择了一个磁盘模版：
            if (newDisk.getOsIsoFileId() != null && oldDiskInfo.getOsIsoFileId() == null) {
                LOGGER.info("【镜像D盘变更磁盘ID为空】,镜像id:[{}],旧的磁盘id:[{}]存在,旧的没有选择一个已有的模版，" + //
                        "但是新的选择了一个磁盘模版,osFileId:[{}]，将磁盘id置空", imageId, newDisk.getId(), newDisk.getOsIsoFileId());
                newDisk.setId(null);
                return;
            }
            // 旧的磁盘存在，选择已有的磁盘模版，新的选择的的磁盘模版与旧的不同：
            if (!oldDiskInfo.getOsIsoFileId().toString().equals(newDisk.getOsIsoFileId().toString())) {
                LOGGER.info("【镜像D盘变更磁盘ID为空】,镜像id:[{}],旧的磁盘id:[{}]存在，旧的选择了已有的磁盘模版osFileId[{}]，新的选择的的磁盘模版" + //
                        "osFileId:[{}]与旧的不同，将磁盘id置空", imageId, newDisk.getId(), oldDiskInfo.getOsIsoFileId(), newDisk.getOsIsoFileId());
                newDisk.setId(null);
            }
        }
    }


    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CREATE_VM_TASK_SUCCESS).msgArgs(new String[] {imageName}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CREATE_VM_TASK_FAIL).msgArgs(new String[] {imageName}).build();
        }
    }
}
