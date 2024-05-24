package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ControlStateEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.PartType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextName;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: 从端准备镜像同步接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/20
 *
 * @author zhiweiHong
 */
public class SlavePrepareImageSyncRequest implements Serializable {

    @NotNull
    private UUID unifiedManageDataId;

    @NotNull
    private UUID taskId;

    @NotBlank
    @TextName
    private String imageTemplateName;

    /**
     * 使用ISO、上传预制镜像时，字段为空
     **/
    @NotNull
    private CbbOsType osType;

    @Nullable
    private String osVersion;


    @NotNull
    private CbbImageType imageType;

    /**
     * 镜像文件分区格式
     */
    @NotNull
    private PartType partType;

    @NotNull
    private VgpuType vgpuType = VgpuType.QXL;

    @Nullable
    private String vgpuExtraInfo = Constants.VGPU_EXTRA_INFO_DEFAULT;

    @Nullable
    private String vgpuExtraInfoHistory = Constants.VGPU_EXTRA_INFO_HISTORY_DEFAULT;

    @Nullable
    private Boolean hasInstallScsiController;

    /**
     * 是否开启硬件虚拟仿真
     */
    @Nullable
    private Boolean enableVirtualEmulation = Boolean.FALSE;

    @Nullable
    private Boolean enablePersonalDesk;

    @Nullable
    private Boolean enableRecoverableDesk;

    @Nullable
    private Integer vmCpuCoreCount;


    /**
     * 镜像虚机磁盘大小（系统盘）
     */
    @Nullable
    private Integer vmDiskSize;


    @NotNull
    private Integer vmMemorySize;

    /**
     * 是否升级到支持双屏的镜像
     **/
    @Nullable
    private Boolean supportDoubleScreen;


    @NotNull
    private String guestToolVersion;

    /**
     * 存储驱动版本
     */
    @Nullable
    private String storageDriverVersion;

    @NotNull
    private ImageTemplateGuestToolState guestToolState;

    @Nullable
    private Boolean resettingDesk;

    @Nullable
    private Boolean onClone;

    @Nullable
    private String note;

    @Nullable
    private ImageVmHost imageVmHost;

    @Nullable
    private Boolean enableNested;

    @Nullable
    private Boolean enableSoftwareDecode;

    @Nullable
    private Integer loaderPatitionNumber;

    @Nullable
    private String loaderPath;

    @Nullable
    private FtpUploadState ftpUploadState;

    @Nullable
    private ControlStateEnum controlState;


    @NotNull
    private Integer snapshotNum;

    @NotNull
    private CbbImageTemplateCreateMode createMode;

    @Nullable
    private String osProType;

    @Nullable
    private Boolean supportApplicationDelivery;

    @Nullable
    private Boolean supportGoldenImage;


    /**
     * 镜像容量大小，首次镜像编辑时为空
     **/
    @NotNull
    private Integer systemDiskSize;



    /**
     * 镜像加域结果
     */
    @Nullable
    private Boolean ad;


    /**
     * 镜像用途：桌面镜像/应用镜像
     */
    @NotNull
    private ImageUsageTypeEnum imageUsage = ImageUsageTypeEnum.DESK;


    /**
     * uam客户端版本
     */
    @Nullable
    private String uamVersion;

    @Nullable
    private Boolean enableUamAgent = false;

    @Nullable
    private CbbImageTemplateSnapshotDTO publishSnapshot;

    @NotBlank
    private String computerName;

    @Nullable
    private CbbCpuArchType cpuArch;

    @NotNull
    private String diskController;

    public UUID getUnifiedManageDataId() {
        return unifiedManageDataId;
    }

    public void setUnifiedManageDataId(UUID unifiedManageDataId) {
        this.unifiedManageDataId = unifiedManageDataId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public CbbImageType getImageType() {
        return imageType;
    }

    public void setImageType(CbbImageType imageType) {
        this.imageType = imageType;
    }

    public PartType getPartType() {
        return partType;
    }

    public void setPartType(PartType partType) {
        this.partType = partType;
    }

    public VgpuType getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(VgpuType vgpuType) {
        this.vgpuType = vgpuType;
    }

    @Nullable
    public String getVgpuExtraInfo() {
        return vgpuExtraInfo;
    }

    public void setVgpuExtraInfo(@Nullable String vgpuExtraInfo) {
        this.vgpuExtraInfo = vgpuExtraInfo;
    }

    @Nullable
    public String getVgpuExtraInfoHistory() {
        return vgpuExtraInfoHistory;
    }

    public void setVgpuExtraInfoHistory(@Nullable String vgpuExtraInfoHistory) {
        this.vgpuExtraInfoHistory = vgpuExtraInfoHistory;
    }

    @Nullable
    public Boolean getHasInstallScsiController() {
        return hasInstallScsiController;
    }

    public void setHasInstallScsiController(@Nullable Boolean hasInstallScsiController) {
        this.hasInstallScsiController = hasInstallScsiController;
    }

    @Nullable
    public Boolean getEnableVirtualEmulation() {
        return enableVirtualEmulation;
    }

    public void setEnableVirtualEmulation(@Nullable Boolean enableVirtualEmulation) {
        this.enableVirtualEmulation = enableVirtualEmulation;
    }

    @Nullable
    public Boolean getEnablePersonalDesk() {
        return enablePersonalDesk;
    }

    public void setEnablePersonalDesk(@Nullable Boolean enablePersonalDesk) {
        this.enablePersonalDesk = enablePersonalDesk;
    }

    @Nullable
    public Boolean getEnableRecoverableDesk() {
        return enableRecoverableDesk;
    }

    public void setEnableRecoverableDesk(@Nullable Boolean enableRecoverableDesk) {
        this.enableRecoverableDesk = enableRecoverableDesk;
    }

    @Nullable
    public Integer getVmCpuCoreCount() {
        return vmCpuCoreCount;
    }

    public void setVmCpuCoreCount(@Nullable Integer vmCpuCoreCount) {
        this.vmCpuCoreCount = vmCpuCoreCount;
    }

    @Nullable
    public Integer getVmDiskSize() {
        return vmDiskSize;
    }

    public void setVmDiskSize(@Nullable Integer vmDiskSize) {
        this.vmDiskSize = vmDiskSize;
    }

    public Integer getVmMemorySize() {
        return vmMemorySize;
    }

    public void setVmMemorySize(Integer vmMemorySize) {
        this.vmMemorySize = vmMemorySize;
    }

    @Nullable
    public Boolean getSupportDoubleScreen() {
        return supportDoubleScreen;
    }

    public void setSupportDoubleScreen(@Nullable Boolean supportDoubleScreen) {
        this.supportDoubleScreen = supportDoubleScreen;
    }

    public String getGuestToolVersion() {
        return guestToolVersion;
    }

    public void setGuestToolVersion(String guestToolVersion) {
        this.guestToolVersion = guestToolVersion;
    }

    @Nullable
    public String getStorageDriverVersion() {
        return storageDriverVersion;
    }

    public void setStorageDriverVersion(@Nullable String storageDriverVersion) {
        this.storageDriverVersion = storageDriverVersion;
    }

    public ImageTemplateGuestToolState getGuestToolState() {
        return guestToolState;
    }

    public void setGuestToolState(ImageTemplateGuestToolState guestToolState) {
        this.guestToolState = guestToolState;
    }

    @Nullable
    public Boolean getResettingDesk() {
        return resettingDesk;
    }

    public void setResettingDesk(@Nullable Boolean resettingDesk) {
        this.resettingDesk = resettingDesk;
    }

    @Nullable
    public Boolean getOnClone() {
        return onClone;
    }

    public void setOnClone(@Nullable Boolean onClone) {
        this.onClone = onClone;
    }

    @Nullable
    public String getNote() {
        return note;
    }

    public void setNote(@Nullable String note) {
        this.note = note;
    }

    public ImageVmHost getImageVmHost() {
        return imageVmHost;
    }

    public void setImageVmHost(ImageVmHost imageVmHost) {
        this.imageVmHost = imageVmHost;
    }

    @Nullable
    public Boolean getEnableNested() {
        return enableNested;
    }

    public void setEnableNested(@Nullable Boolean enableNested) {
        this.enableNested = enableNested;
    }

    @Nullable
    public Boolean getEnableSoftwareDecode() {
        return enableSoftwareDecode;
    }

    public void setEnableSoftwareDecode(@Nullable Boolean enableSoftwareDecode) {
        this.enableSoftwareDecode = enableSoftwareDecode;
    }

    @Nullable
    public Integer getLoaderPatitionNumber() {
        return loaderPatitionNumber;
    }

    public void setLoaderPatitionNumber(@Nullable Integer loaderPatitionNumber) {
        this.loaderPatitionNumber = loaderPatitionNumber;
    }

    @Nullable
    public String getLoaderPath() {
        return loaderPath;
    }

    public void setLoaderPath(@Nullable String loaderPath) {
        this.loaderPath = loaderPath;
    }

    @Nullable
    public FtpUploadState getFtpUploadState() {
        return ftpUploadState;
    }

    public void setFtpUploadState(@Nullable FtpUploadState ftpUploadState) {
        this.ftpUploadState = ftpUploadState;
    }

    public ControlStateEnum getControlState() {
        return controlState;
    }

    public void setControlState(ControlStateEnum controlState) {
        this.controlState = controlState;
    }

    public Integer getSnapshotNum() {
        return snapshotNum;
    }

    public void setSnapshotNum(Integer snapshotNum) {
        this.snapshotNum = snapshotNum;
    }

    public CbbImageTemplateCreateMode getCreateMode() {
        return createMode;
    }

    public void setCreateMode(CbbImageTemplateCreateMode createMode) {
        this.createMode = createMode;
    }

    @Nullable
    public String getOsProType() {
        return osProType;
    }

    public void setOsProType(@Nullable String osProType) {
        this.osProType = osProType;
    }

    @Nullable
    public Boolean getSupportApplicationDelivery() {
        return supportApplicationDelivery;
    }

    public void setSupportApplicationDelivery(@Nullable Boolean supportApplicationDelivery) {
        this.supportApplicationDelivery = supportApplicationDelivery;
    }

    @Nullable
    public Boolean getSupportGoldenImage() {
        return supportGoldenImage;
    }

    public void setSupportGoldenImage(@Nullable Boolean supportGoldenImage) {
        this.supportGoldenImage = supportGoldenImage;
    }

    public Integer getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(Integer systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    @Nullable
    public Boolean getAd() {
        return ad;
    }

    public void setAd(@Nullable Boolean ad) {
        this.ad = ad;
    }

    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }

    public String getUamVersion() {
        return uamVersion;
    }

    public void setUamVersion(String uamVersion) {
        this.uamVersion = uamVersion;
    }

    public Boolean getEnableUamAgent() {
        return enableUamAgent;
    }

    public void setEnableUamAgent(Boolean enableUamAgent) {
        this.enableUamAgent = enableUamAgent;
    }

    @Nullable
    public CbbImageTemplateSnapshotDTO getPublishSnapshot() {
        return publishSnapshot;
    }

    public void setPublishSnapshot(@Nullable CbbImageTemplateSnapshotDTO publishSnapshot) {
        this.publishSnapshot = publishSnapshot;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    @Nullable
    public CbbCpuArchType getCpuArch() {
        return cpuArch;
    }

    public void setCpuArch(@Nullable CbbCpuArchType cpuArch) {
        this.cpuArch = cpuArch;
    }

    public void setDiskController(String diskController) {
        this.diskController = diskController;
    }

    public String getDiskController() {
        return diskController;
    }
}
