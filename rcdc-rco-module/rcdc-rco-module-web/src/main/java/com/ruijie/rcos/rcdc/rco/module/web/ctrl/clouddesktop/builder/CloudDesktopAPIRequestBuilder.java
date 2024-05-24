package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.builder;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbConfigVmForEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbCreateImageTemplateByCloneExistDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbCreateImageTemplateByOsFileDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbUpdateImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.osfile.CbbImportOsFileDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.CloneImageTemplateWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.ConfigVmForEditImageTemplateWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.UpdateImageTemplateWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop.request.CreateImageTemplateByOsFileRequest;
import com.ruijie.rcos.rcdc.rco.module.web.util.CapacityUnitUtils;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.UUID;

/**
 * 封装所有WebRequest到APIRequest的DTO转换
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019年3月30日 <br>
 * 
 * @author Lbd
 */
public class CloudDesktopAPIRequestBuilder {
    private CloudDesktopAPIRequestBuilder() {
        //
    }

    /**
     * 构建请求参数
     * 
     * @param chunkUploadFile 文件
     * @return ImportOsFileRequest 请求
     */
    public static CbbImportOsFileDTO buildImportOsFileRequest(ChunkUploadFile chunkUploadFile) {
        Assert.notNull(chunkUploadFile, "chunkUploadFile is not null");
        final CbbImportOsFileDTO request = new CbbImportOsFileDTO();
        request.setOsFileId(UUID.randomUUID());
        request.setOsFileMD5(chunkUploadFile.getFileMD5());
        request.setOsFileName(chunkUploadFile.getFileName());
        request.setOsFilePath(chunkUploadFile.getFilePath());
        request.setOsFileSize(chunkUploadFile.getFileSize());
        JSONObject customData = chunkUploadFile.getCustomData();
        String note = customData.getString("note");
        request.setNote(note);
        return request;
    }

    /**
     * 构建请求参数
     * 
     * @param webRequest 请求参数
     * @return 结果
     */
    public static CbbConfigVmForEditImageTemplateDTO buildConfigVmForEditImageTemplateRequest(ConfigVmForEditImageTemplateWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest is not null");

        final CbbConfigVmForEditImageTemplateDTO request = new CbbConfigVmForEditImageTemplateDTO();
        request.setCpuCoreCount(webRequest.getCpu());
        request.setDeskNetworkId(webRequest.getNetwork().getId());
        request.setDiskSize(webRequest.getSystemDisk());
        request.setExpectIp(webRequest.getNetwork().getIp());
        request.setImageTemplateId(webRequest.getId());
        request.setMemorySize(CapacityUnitUtils.gb2Mb(webRequest.getMemory()));
        request.setVgpuInfoDTO(buildVgpuInfoDTO(webRequest));
        request.setEnableNested(webRequest.getEnableNested());
        IdLabelEntry cluster = webRequest.getCluster();
        if (Objects.nonNull(cluster)) {
            request.setClusterId(cluster.getId());
        }
        IdLabelEntry storagePool = webRequest.getStoragePool();
        if (Objects.nonNull(storagePool)) {
            request.setStoragePoolId(storagePool.getId());
        }
        // 设置虚拟机的信息
        request.setVmClusterId(buildVmClusterIdAndStoragePoolId(cluster, webRequest.getVmCluster()));
        request.setVmStoragePoolId(buildVmClusterIdAndStoragePoolId(storagePool, webRequest.getVmStoragePool()));
        request.setComputerName(webRequest.getComputerName());
        //是否开启数据D盘：
        request.setImageDiskList(webRequest.getImageDiskList());
        if (!CollectionUtils.isEmpty(webRequest.getImageDriverList())) {
            request.setDriverIdArr(webRequest.getImageDriverList().toArray(new UUID[0]));
        }
        request.setPlatformId(webRequest.getPlatformId());
        return request;
    }
    
    private static UUID buildVmClusterIdAndStoragePoolId(IdLabelEntry imageEntry, IdLabelEntry vmEntry) {
        // 前端有传虚拟机信息，则用前端传的
        if (Objects.nonNull(vmEntry)) {
            return vmEntry.getId();
        }
        // 前端没传虚拟机信息而且镜像存在，则用镜像的
        if (Objects.nonNull(imageEntry)) {
            return imageEntry.getId();
        }
        // 都没有，则为空
        return null;
    }


    private static VgpuInfoDTO buildVgpuInfoDTO(ConfigVmForEditImageTemplateWebRequest webRequest) {
        if (Objects.isNull(webRequest.getVgpuType()) || VgpuType.QXL.name().equals(webRequest.getVgpuType())
                || Objects.isNull(webRequest.getVgpuExtraInfo())) {
            return new VgpuInfoDTO();
        }
        VgpuExtraInfo info = new VgpuExtraInfo();
        BeanUtils.copyProperties(webRequest.getVgpuExtraInfo(), info);
        return new VgpuInfoDTO(VgpuType.valueOf(webRequest.getVgpuType()), info);
    }

    /**
     * 构建请求参数
     *
     * @param webRequest 请求参数
     * @return 结果
     */
    public static CbbCreateImageTemplateByCloneExistDTO buildCreateImageTemplateByCloneExistRequest(CloneImageTemplateWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest is not null");

        final CbbCreateImageTemplateByCloneExistDTO request = new CbbCreateImageTemplateByCloneExistDTO();
        request.setOldImageTemplateId(webRequest.getId());
        request.setNewImageTemplateName(webRequest.getImageName());
        request.setNewImageTemplateId(UUID.randomUUID());
        request.setNote(webRequest.getNote());
        request.setCbbImageType(webRequest.getCbbImageType());
        return request;
    }

    /**
     * 构建请求参数
     * 
     * @param webRequest 请求参数
     * @return 结果
     */
    public static CbbCreateImageTemplateByOsFileDTO buildCreateImageTemplateByIsoFileRequest(CreateImageTemplateByOsFileRequest webRequest) {
        Assert.notNull(webRequest, "webRequest is not null");

        final CbbCreateImageTemplateByOsFileDTO request = new CbbCreateImageTemplateByOsFileDTO();
        request.setNewImageTemplateId(UUID.randomUUID());
        request.setNewImageTemplateName(webRequest.getImageName());
        request.setOsIsoFileId(webRequest.getIsoImageFileId().getId());
        request.setNote(webRequest.getNote());
        request.setCbbOsType(webRequest.getImageSystemType());
        request.setCbbImageType(webRequest.getCbbImageType());
        request.setEnableMultipleVersion(webRequest.getEnableMultipleVersion());
        request.setPlatformId(webRequest.getAdvancedConfig().getPlatformId());
        request.setImageUsage(webRequest.getImageUsage());
        return request;
    }

    /**
     * 构建请求参数
     *
     * @param webRequest 请求参数
     * @return 结果
     */
    public static CbbUpdateImageTemplateDTO buildUpdateImageTemplateRequest(UpdateImageTemplateWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest is not null");

        final CbbUpdateImageTemplateDTO request = new CbbUpdateImageTemplateDTO();
        request.setImageTemplateId(webRequest.getId());
        request.setImageTemplateName(webRequest.getImageName());
        request.setCbbOsType(webRequest.getImageSystemType());
        request.setNote(webRequest.getNote());
        request.setImageUsage(webRequest.getImageUsage());
        return request;
    }


}
