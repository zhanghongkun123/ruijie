package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.dto.RestDeskCreateExtraDiskDTO;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
public class CreateVDIDesktopRequest {

    @Nullable
    private String id;

    @NotBlank
    private String userName;

    @NotNull
    private UUID imageId;

    @NotNull
    private UUID deskStrategyId;

    @NotNull
    private UUID networkId;

    /**
     * * 运行集群ID
     */
    @Nullable
    private UUID clusterId;

    /**
     * 存储池ID
     */
    @Nullable
    private UUID storagePoolId;
    
    @Nullable
    private UUID platformId;

    @Nullable
    @Range(
            min = "1",
            max = "32"
    )
    private Integer cpuCores;

    @Nullable
    @Range(
            min = "1024",
            max = "262144"
    )
    private Integer memory;

    @Nullable
    @Range(
            min = "20",
            max = "2048"
    )
    private Integer systemDisk;

    @Nullable
    @Range(max = "2048")
    private Integer dataDisk;

    @Nullable
    private UUID dataDiskStoragePoolId;

    @Nullable
    private String vgpuModel;

    @Nullable
    private RestDeskCreateExtraDiskDTO[] extraDiskArr;

    @Nullable
    private Boolean enableHyperVisorImprove = true;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getDeskStrategyId() {
        return deskStrategyId;
    }

    public void setDeskStrategyId(UUID deskStrategyId) {
        this.deskStrategyId = deskStrategyId;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Nullable
    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(@Nullable UUID clusterId) {
        this.clusterId = clusterId;
    }

    @Nullable
    public UUID getStoragePoolId() {
        return storagePoolId;
    }

    public void setStoragePoolId(@Nullable UUID storagePoolId) {
        this.storagePoolId = storagePoolId;
    }

    @Nullable
    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(@Nullable UUID platformId) {
        this.platformId = platformId;
    }

    @Nullable
    public Integer getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(@Nullable Integer cpuCores) {
        this.cpuCores = cpuCores;
    }

    @Nullable
    public Integer getMemory() {
        return memory;
    }

    public void setMemory(@Nullable Integer memory) {
        this.memory = memory;
    }

    @Nullable
    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(@Nullable Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    @Nullable
    public Integer getDataDisk() {
        return dataDisk;
    }

    public void setDataDisk(@Nullable Integer dataDisk) {
        this.dataDisk = dataDisk;
    }

    @Nullable
    public UUID getDataDiskStoragePoolId() {
        return dataDiskStoragePoolId;
    }

    public void setDataDiskStoragePoolId(@Nullable UUID dataDiskStoragePoolId) {
        this.dataDiskStoragePoolId = dataDiskStoragePoolId;
    }

    @Nullable
    public String getVgpuModel() {
        return vgpuModel;
    }

    public void setVgpuModel(@Nullable String vgpuModel) {
        this.vgpuModel = vgpuModel;
    }

    @Nullable
    public RestDeskCreateExtraDiskDTO[] getExtraDiskArr() {
        return extraDiskArr;
    }

    public void setExtraDiskArr(@Nullable RestDeskCreateExtraDiskDTO[] extraDiskArr) {
        this.extraDiskArr = extraDiskArr;
    }

    @Nullable
    public Boolean getEnableHyperVisorImprove() {
        return enableHyperVisorImprove;
    }

    public void setEnableHyperVisorImprove(@Nullable Boolean enableHyperVisorImprove) {
        this.enableHyperVisorImprove = enableHyperVisorImprove;
    }
}
