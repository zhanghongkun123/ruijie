package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.ExtraDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.request.DeskSpecRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/18
 *
 * @author Jarman
 */
public class VdiDesktopConfigVO extends DesktopConfigBaseVO {

    @NotNull
    @ApiModelProperty(value = "网络配置",required = true)
    private NetworkConfigVO network;

    /**
     * 软件管控策略ID
     */
    @Nullable
    private IdLabelEntry softwareStrategy;


    /**
     * 访客用户允许设置桌面个数
     */
    @Nullable
    @ApiModelProperty(value = "云桌面个数")
    private Integer visitorDesktopNum;

    /**
     * 镜像版本信息
     */
    @Nullable
    private ImageEditionVO imageEdition;

    /**
     * 用户配置策略
     */
    @Nullable
    @ApiModelProperty(value = "用户配置策略")
    private IdLabelEntry userProfileStrategy;

    /**
     * 运行集群ID
     */
    @NotNull
    @ApiModelProperty(value = "运行位置", required = true)
    private IdLabelEntry cluster;

    @NotNull
    @ApiModelProperty(value = "云平台", required = true)
    private IdLabelEntry cloudPlatform;

    @ApiModelProperty(value = "CPU核数", required = true)
    @NotNull
    @Range(min = "1", max = "32")
    private Integer cpu;

    @ApiModelProperty(value = "内存（GB）", required = true)
    @NotNull
    private Double memory;

    @ApiModelProperty(value = "系统盘（GB）", required = true)
    @NotNull
    @Range(min = "20", max = "2048")
    private Integer systemDisk;

    @NotNull
    @ApiModelProperty(value = "系统盘存储位置", required = true)
    private IdLabelEntry systemDiskStoragePool;

    @ApiModelProperty(value = "个人盘（GB）", required = true)
    @Nullable
    @Range(max = "2048")
    private Integer personalDisk;

    @ApiModelProperty(value = "个人盘存储位置", required = true)
    @Nullable
    private IdLabelEntry personDiskStoragePool;

    @ApiModelProperty(value = "vGPU类型")
    @Nullable
    private VgpuType vgpuType;

    @ApiModelProperty(value = "vGPU配置信息")
    @Nullable
    private VgpuExtraInfo vgpuExtraInfo;

    /**
     * 是否配置开启虚机特性提升，默认开启
     * true：默认配置开启虚机性能提升，不加-hypervisor参数
     * false：代表开启-hypervisor，显示的去除虚机的虚拟化属性,对虚机的性能优化效果将失效
     */
    @ApiModelProperty("是否开启hyperV性能提升，默认开启")
    @Nullable
    private Boolean enableHyperVisorImprove = true;

    @ApiModelProperty(value = "用户额外盘", required = true)
    @Nullable
    private ExtraDiskDTO[] extraDiskArr;

    /**
     * 获取DeskSpecDTO
     *
     * @return DeskSpecDTO
     */
    public DeskSpecRequest toDeskSpec() {
        DeskSpecRequest deskSpecDTO = new DeskSpecRequest();
        deskSpecDTO.setCpu(this.getCpu());
        deskSpecDTO.setMemory(this.getMemory());
        deskSpecDTO.setSystemDisk(this.getSystemDisk());
        deskSpecDTO.setSystemDiskStoragePool(systemDiskStoragePool);
        deskSpecDTO.setPersonalDisk(this.getPersonalDisk());
        deskSpecDTO.setPersonDiskStoragePool(personDiskStoragePool);
        deskSpecDTO.setEnableHyperVisorImprove(this.getEnableHyperVisorImprove());
        deskSpecDTO.setVgpuType(this.getVgpuType());
        deskSpecDTO.setVgpuExtraInfo(this.getVgpuExtraInfo());
        deskSpecDTO.setExtraDiskArr(this.getExtraDiskArr());
        return deskSpecDTO;
    }

    public NetworkConfigVO getNetwork() {
        return network;
    }

    public void setNetwork(NetworkConfigVO network) {
        this.network = network;
    }

    @Nullable
    public Integer getVisitorDesktopNum() {
        return visitorDesktopNum;
    }

    public void setVisitorDesktopNum(@Nullable Integer visitorDesktopNum) {
        this.visitorDesktopNum = visitorDesktopNum;
    }

    @Nullable
    public ImageEditionVO getImageEdition() {
        return imageEdition;
    }

    public void setImageEdition(@Nullable ImageEditionVO imageEdition) {
        this.imageEdition = imageEdition;
    }

    @Nullable
    public IdLabelEntry getSoftwareStrategy() {
        return softwareStrategy;
    }

    public void setSoftwareStrategy(@Nullable IdLabelEntry softwareStrategy) {
        this.softwareStrategy = softwareStrategy;
    }

    @Nullable
    public IdLabelEntry getUserProfileStrategy() {
        return userProfileStrategy;
    }

    public void setUserProfileStrategy(@Nullable IdLabelEntry userProfileStrategy) {
        this.userProfileStrategy = userProfileStrategy;
    }

    public IdLabelEntry getCluster() {
        return cluster;
    }

    public void setCluster(IdLabelEntry cluster) {
        this.cluster = cluster;
    }

    public IdLabelEntry getSystemDiskStoragePool() {
        return systemDiskStoragePool;
    }

    public void setSystemDiskStoragePool(IdLabelEntry systemDiskStoragePool) {
        this.systemDiskStoragePool = systemDiskStoragePool;
    }

    public IdLabelEntry getCloudPlatform() {
        return cloudPlatform;
    }

    public void setCloudPlatform(IdLabelEntry cloudPlatform) {
        this.cloudPlatform = cloudPlatform;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Double getMemory() {
        return memory;
    }

    public void setMemory(Double memory) {
        this.memory = memory;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public Integer getPersonalDisk() {
        return personalDisk;
    }

    public void setPersonalDisk(Integer personalDisk) {
        this.personalDisk = personalDisk;
    }

    public IdLabelEntry getPersonDiskStoragePool() {
        return personDiskStoragePool;
    }

    public void setPersonDiskStoragePool(IdLabelEntry personDiskStoragePool) {
        this.personDiskStoragePool = personDiskStoragePool;
    }

    @Nullable
    public VgpuType getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(@Nullable VgpuType vgpuType) {
        this.vgpuType = vgpuType;
    }

    @Nullable
    public VgpuExtraInfo getVgpuExtraInfo() {
        return vgpuExtraInfo;
    }

    public void setVgpuExtraInfo(@Nullable VgpuExtraInfo vgpuExtraInfo) {
        this.vgpuExtraInfo = vgpuExtraInfo;
    }

    @Nullable
    public Boolean getEnableHyperVisorImprove() {
        return enableHyperVisorImprove;
    }

    public void setEnableHyperVisorImprove(@Nullable Boolean enableHyperVisorImprove) {
        this.enableHyperVisorImprove = enableHyperVisorImprove;
    }

    @Nullable
    public ExtraDiskDTO[] getExtraDiskArr() {
        return extraDiskArr;
    }

    public void setExtraDiskArr(@Nullable ExtraDiskDTO[] extraDiskArr) {
        this.extraDiskArr = extraDiskArr;
    }
}
