package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/3
 *
 * @author zhiweiHong
 */
public class PublishImageTemplateSnapshotDTO {


    @NotNull
    private UUID unifiedManageDataId;


    @Nullable
    private UUID id;

    @Nullable
    private Date createTime;

    /**
     * 引用计数
     **/
    @Nullable
    private Integer refCount;

    @Nullable
    private String changeLog;


    @Nullable
    private Integer personalDeskCount;

    @Nullable
    private Integer recoverableDeskCount;

    @Nullable
    private Integer applicationDeliveryDeskCount;


    /**
     * 首次镜像编辑时为空
     **/
    @NotNull
    private Integer systemDiskSize;

    @Nullable
    private String serverCpuTypeInfo;

    @Nullable
    private String name;

    @Nullable
    private String driverInfo;

    @Nullable
    private String guesttoolVersion;

    /**
     * 存储驱动版本
     */
    @Nullable
    private String storageDriverVersion;

    @Nullable
    private String remark;

    @NotNull
    private VgpuType vgpuType = VgpuType.QXL;

    @Nullable
    private String vgpuExtraInfo = Constants.VGPU_EXTRA_INFO_DEFAULT;

    @Nullable
    private String vgpuExtraInfoHistory = Constants.VGPU_EXTRA_INFO_HISTORY_DEFAULT;

    @Nullable
    private Boolean hasInstallScsiController;

    @Nullable
    private Boolean enableVirtualEmulation;

    /**
     * 镜像加域结果
     */
    @Nullable
    private Boolean ad;

    @Nullable
    private String advancedConfig = Constants.BEFORE_EDIT_ADVANCED_CONFIG;

    @Nullable
    private String osVersion;

    public String getAdvancedConfig() {
        return advancedConfig;
    }

    public void setAdvancedConfig(String advancedConfig) {
        this.advancedConfig = advancedConfig;
    }

    public String getStorageDriverVersion() {
        return storageDriverVersion;
    }

    public void setStorageDriverVersion(String storageDriverVersion) {
        this.storageDriverVersion = storageDriverVersion;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(String driverInfo) {
        this.driverInfo = driverInfo;
    }

    public String getGuesttoolVersion() {
        return guesttoolVersion;
    }

    public void setGuesttoolVersion(String guesttoolVersion) {
        this.guesttoolVersion = guesttoolVersion;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getRefCount() {
        return refCount;
    }

    public void setRefCount(Integer refCount) {
        this.refCount = refCount;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }


    public Integer getPersonalDeskCount() {
        return personalDeskCount;
    }

    public void setPersonalDeskCount(Integer personalDeskCount) {
        this.personalDeskCount = personalDeskCount;
    }

    public Integer getRecoverableDeskCount() {
        return recoverableDeskCount;
    }

    public void setRecoverableDeskCount(Integer recoverableDeskCount) {
        this.recoverableDeskCount = recoverableDeskCount;
    }

    public Integer getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(Integer systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    public Integer getApplicationDeliveryDeskCount() {
        return applicationDeliveryDeskCount;
    }

    public void setApplicationDeliveryDeskCount(Integer applicationDeliveryDeskCount) {
        this.applicationDeliveryDeskCount = applicationDeliveryDeskCount;
    }

    public String getServerCpuTypeInfo() {
        return serverCpuTypeInfo;
    }

    public void setServerCpuTypeInfo(String serverCpuTypeInfo) {
        this.serverCpuTypeInfo = serverCpuTypeInfo;
    }


    public VgpuType getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(VgpuType vgpuType) {
        this.vgpuType = vgpuType;
    }

    public String getVgpuExtraInfo() {
        return vgpuExtraInfo;
    }

    public void setVgpuExtraInfo(String vgpuExtraInfo) {
        this.vgpuExtraInfo = vgpuExtraInfo;
    }

    public String getVgpuExtraInfoHistory() {
        return vgpuExtraInfoHistory;
    }

    public void setVgpuExtraInfoHistory(String vgpuExtraInfoHistory) {
        this.vgpuExtraInfoHistory = vgpuExtraInfoHistory;
    }

    public Boolean getHasInstallScsiController() {
        return hasInstallScsiController;
    }

    public void setHasInstallScsiController(Boolean hasInstallScsiController) {
        this.hasInstallScsiController = hasInstallScsiController;
    }

    public Boolean getEnableVirtualEmulation() {
        return enableVirtualEmulation;
    }

    public void setEnableVirtualEmulation(Boolean enableVirtualEmulation) {
        this.enableVirtualEmulation = enableVirtualEmulation;
    }

    @Nullable
    public Boolean getAd() {
        return ad;
    }

    public void setAd(@Nullable Boolean ad) {
        this.ad = ad;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }


    public UUID getUnifiedManageDataId() {
        return unifiedManageDataId;
    }

    public void setUnifiedManageDataId(UUID unifiedManageDataId) {
        this.unifiedManageDataId = unifiedManageDataId;
    }
}
