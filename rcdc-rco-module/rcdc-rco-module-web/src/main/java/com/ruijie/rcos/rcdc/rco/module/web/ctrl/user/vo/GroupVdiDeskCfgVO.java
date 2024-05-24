package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfoSupport;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.ExtraDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.imagetemplate.ImageTemplateVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.strategy.DeskStrategyVO;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-2-14
 *
 * @author artom
 */
public class GroupVdiDeskCfgVO {
    
    private ImageTemplateVO image;
    
    private DeskStrategyVO strategy;
    
    private CbbDeskNetworkDetailDTO network;

    private UserProfileStrategyDTO userProfileStrategy;

    /**
     * 软件管控策略
     */
    private SoftwareStrategyDTO softwareStrategy;

    private IdLabelEntry cluster;

    private IdLabelEntry cloudPlatform;

    private Integer cpu;

    private Double memory;

    private Integer systemDisk;

    private IdLabelEntry systemDiskStoragePool;

    private Integer personalDisk;

    private IdLabelEntry personDiskStoragePool;

    private VgpuType vgpuType;

    private VgpuExtraInfoSupport vgpuExtraInfo;

    private Boolean enableHyperVisorImprove = true;

    private ExtraDiskDTO[] extraDiskArr;

    public ImageTemplateVO getImage() {
        return image;
    }

    public void setImage(ImageTemplateVO image) {
        this.image = image;
    }

    public DeskStrategyVO getStrategy() {
        return strategy;
    }

    public void setStrategy(DeskStrategyVO strategy) {
        this.strategy = strategy;
    }

    public CbbDeskNetworkDetailDTO getNetwork() {
        return network;
    }

    public void setNetwork(CbbDeskNetworkDetailDTO network) {
        this.network = network;
    }

    public SoftwareStrategyDTO getSoftwareStrategy() {
        return softwareStrategy;
    }

    public void setSoftwareStrategy(SoftwareStrategyDTO softwareStrategy) {
        this.softwareStrategy = softwareStrategy;
    }

    public UserProfileStrategyDTO getUserProfileStrategy() {
        return userProfileStrategy;
    }

    public void setUserProfileStrategy(UserProfileStrategyDTO userProfileStrategy) {
        this.userProfileStrategy = userProfileStrategy;
    }

    public IdLabelEntry getCluster() {
        return cluster;
    }

    public void setCluster(IdLabelEntry cluster) {
        this.cluster = cluster;
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

    public IdLabelEntry getSystemDiskStoragePool() {
        return systemDiskStoragePool;
    }

    public void setSystemDiskStoragePool(IdLabelEntry systemDiskStoragePool) {
        this.systemDiskStoragePool = systemDiskStoragePool;
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

    public VgpuType getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(VgpuType vgpuType) {
        this.vgpuType = vgpuType;
    }

    public VgpuExtraInfoSupport getVgpuExtraInfo() {
        return vgpuExtraInfo;
    }

    public void setVgpuExtraInfo(VgpuExtraInfoSupport vgpuExtraInfo) {
        this.vgpuExtraInfo = vgpuExtraInfo;
    }

    public Boolean getEnableHyperVisorImprove() {
        return enableHyperVisorImprove;
    }

    public void setEnableHyperVisorImprove(Boolean enableHyperVisorImprove) {
        this.enableHyperVisorImprove = enableHyperVisorImprove;
    }

    public ExtraDiskDTO[] getExtraDiskArr() {
        return extraDiskArr;
    }

    public void setExtraDiskArr(ExtraDiskDTO[] extraDiskArr) {
        this.extraDiskArr = extraDiskArr;
    }
}
