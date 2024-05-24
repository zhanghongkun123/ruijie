package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.VgpuExtraInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.vo.NetworkVO;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/3/29 <br>
 *
 * @author yyz
 */
public class ConfigVmForEditImageTemplateWebRequest implements WebRequest {

    @ApiModelProperty(value = "", required = true)
    @Nullable
    private UUID id;

    @ApiModelProperty(value = "CPU核数", required = true)
    @NotNull
    private Integer cpu;

    @ApiModelProperty(value = "网络策略", required = true)
    @NotNull
    private NetworkVO network;

    @ApiModelProperty(value = "系统盘", required = true)
    @NotNull
    @Range(min = "20", max = "2048")
    private Integer systemDisk;

    @ApiModelProperty(value = "内存", required = true)
    @NotNull
    private Double memory;

    @ApiModelProperty(value = "vGPU类型", required = false)
    @Nullable
    private String vgpuType;

    @ApiModelProperty(value = "显存配置", required = false)
    @Nullable
    private VgpuExtraInfoDTO vgpuExtraInfo = new VgpuExtraInfoDTO();

    @ApiModelProperty(value = "启用嵌套虚拟化", required = false)
    @Nullable
    private Boolean enableNested;

    @ApiModelProperty(value = "计算机名称", required = true)
    @NotBlank
    @Size(min = 1, max = 15)
    private String computerName;

    @ApiModelProperty(value = "运行集群")
    @Nullable
    private IdLabelEntry cluster;

    @ApiModelProperty(value = "存储池")
    @Nullable
    private IdLabelEntry storagePool;

    @Nullable
    @ApiModelProperty(value = "磁盘列表,当前不包含系统盘")
    private List<CbbImageDiskDTO> imageDiskList;

    @ApiModelProperty(value = "虚拟机运行集群")
    @Nullable
    private IdLabelEntry vmCluster;

    @ApiModelProperty(value = "虚拟机存储池")
    @Nullable
    private IdLabelEntry vmStoragePool;

    @ApiModelProperty(value = "驱动包列表")
    @Nullable
    @Size(max = 1000)
    private List<UUID> imageDriverList = new ArrayList<>();


    @ApiModelProperty(value = "创建镜像")
    @Nullable
    private UUID platformId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public NetworkVO getNetwork() {
        return network;
    }

    public void setNetwork(NetworkVO network) {
        this.network = network;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public Double getMemory() {
        return memory;
    }

    public void setMemory(Double memory) {
        this.memory = memory;
    }

    @Nullable
    public String getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(@Nullable String vgpuType) {
        this.vgpuType = vgpuType;
    }

    @Nullable
    public VgpuExtraInfoDTO getVgpuExtraInfo() {
        return vgpuExtraInfo;
    }

    public void setVgpuExtraInfo(@Nullable VgpuExtraInfoDTO vgpuExtraInfo) {
        this.vgpuExtraInfo = vgpuExtraInfo;
    }

    @Nullable
    public Boolean getEnableNested() {
        return enableNested;
    }

    public void setEnableNested(@Nullable Boolean enableNested) {
        this.enableNested = enableNested;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    @Nullable
    public IdLabelEntry getCluster() {
        return cluster;
    }

    public void setCluster(@Nullable IdLabelEntry cluster) {
        this.cluster = cluster;
    }

    @Nullable
    public IdLabelEntry getStoragePool() {
        return storagePool;
    }

    public void setStoragePool(@Nullable IdLabelEntry storagePool) {
        this.storagePool = storagePool;
    }

    @Nullable
    public List<CbbImageDiskDTO> getImageDiskList() {
        return imageDiskList;
    }

    public void setImageDiskList(@Nullable List<CbbImageDiskDTO> imageDiskList) {
        this.imageDiskList = imageDiskList;
    }


    @Nullable
    public IdLabelEntry getVmCluster() {
        return vmCluster;
    }

    public void setVmCluster(@Nullable IdLabelEntry vmCluster) {
        this.vmCluster = vmCluster;
    }

    @Nullable
    public IdLabelEntry getVmStoragePool() {
        return vmStoragePool;
    }

    public void setVmStoragePool(@Nullable IdLabelEntry vmStoragePool) {
        this.vmStoragePool = vmStoragePool;
    }

    @Nullable
    public List<UUID> getImageDriverList() {
        return imageDriverList;
    }

    public void setImageDriverList(@Nullable List<UUID> imageDriverList) {
        this.imageDriverList = imageDriverList;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }
}
