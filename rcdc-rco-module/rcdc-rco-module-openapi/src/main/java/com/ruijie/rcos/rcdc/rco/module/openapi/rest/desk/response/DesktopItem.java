package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.response;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskRegisterState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;

import java.util.UUID;

/**
 * Description: 分页查询结果条目
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/24 20:54
 *
 * @author lyb
 */
public class DesktopItem {

    private static final double G_TO_M = 1 << 10; // 字节

    public DesktopItem(CloudDesktopDTO dto) {
        setId(dto.getId());
        setComputerName(dto.getComputerName());
        setCpuCores(dto.getCpu());
        setCreateTime(dto.getCreateTime() == null ? null : dto.getCreateTime().getTime());
        setDeskType(dto.getDesktopType());
        setDeskIp(dto.getDesktopIp());
        setDeskName(dto.getDesktopName());
        setDeskCategory(dto.getDesktopCategory());
        setDeskState(dto.getDesktopState());
        setDataDisk(dto.getPersonDisk());
        setSystemDisk(dto.getSystemDisk());
        setMemory(dto.getMemory() != null ? gb2Mb(dto.getMemory()) : null);
        setEnableCustom(dto.getEnableCustom());
        setImageName(dto.getImageName());
        setLatestLoginTime(dto.getLatestLoginTime() == null ? null : dto.getLatestLoginTime().getTime());
        setNeedRefreshStrategy(dto.isNeedRefreshStrategy());
        setPhysicalServerIp(dto.getPhysicalServerIp());
        setRemark(dto.getRemark());
        setTerminalIp(dto.getTerminalIp());
        setUserName(dto.getUserName());
        setUserType(dto.getUserType());
        setVgpuType(dto.getVgpuType());
        setClusterId(dto.getClusterId());
        setClusterName(dto.getClusterName());
        setDesktopPoolType(dto.getDesktopPoolType());
        setPlatformId(dto.getPlatformId());
        setPlatformName(dto.getPlatformName());
        setRegisterState(dto.getRegisterState());
        setRegisterMessage(dto.getRegisterMessage());
    }

    public UUID id;

    /**
     * 电脑名称
     */
    public String computerName;

    /**
     * cpu核数
     */
    public Integer cpuCores;

    /**
     * 创建时间
     */
    public Long createTime;

    /**
     * 虚机类型，枚举值VDI
     */
    public String deskType;

    /**
     * 云桌面ip
     */
    public String deskIp;

    /**
     * 云桌面名称
     */
    public String deskName;

    /**
     * 云桌面状态
     */
    public String deskState;

    /**
     * 云桌面分类，枚举值
     * PERSONAL 个性，
     * APP_LAYER  应用分发，
     * RECOVERABLE  还原
     */
    public String deskCategory;

    /**
     * 是否独立配置
     */
    public Boolean enableCustom;

    /**
     * 镜像名称
     */
    public String imageName;

    /**
     * 最近登录时间
     */
    public Long latestLoginTime;

    /**
     * 内存
     */
    public Integer memory;

    /**
     * 是否需要刷新策略
     */
    public Boolean needRefreshStrategy;

    /**
     * 个人数据盘
     */
    public Integer dataDisk;

    /**
     * 物理服务器ip
     */
    public String physicalServerIp;

    /**
     * 云桌面标签
     */
    public String remark;

    /**
     * 系统盘
     */
    public Integer systemDisk;

    /**
     * 终端ip
     */
    public String terminalIp;

    /**
     * 用户名
     */
    public String userName;

    /**
     * 用户类型
     */
    public String userType;

    /**
     * gpu虚拟化类型
     */
    public VgpuType vgpuType;

    /**
     * 计算集群ID
     */
    private UUID clusterId;

    /**
     * 计算集群名称
     */
    private String clusterName;

    /**
     * 桌面池类型
     **/
    private String desktopPoolType;

    /**
     * 平台ID
     */
    private UUID platformId;

    /**
     * 云平台名称
     */
    private String platformName;

    /**
     * 注册状态
     */
    private CbbDeskRegisterState registerState;

    /**
     * 注册消息
     */
    private String registerMessage;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public Integer getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(Integer cpuCores) {
        this.cpuCores = cpuCores;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getDeskType() {
        return deskType;
    }

    public void setDeskType(String deskType) {
        this.deskType = deskType;
    }

    public String getDeskIp() {
        return deskIp;
    }

    public void setDeskIp(String deskIp) {
        this.deskIp = deskIp;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getDeskState() {
        return deskState;
    }

    public void setDeskState(String deskState) {
        this.deskState = deskState;
    }

    public String getDeskCategory() {
        return deskCategory;
    }

    public void setDeskCategory(String deskCategory) {
        this.deskCategory = deskCategory;
    }

    public Boolean getEnableCustom() {
        return enableCustom;
    }

    public void setEnableCustom(Boolean enableCustom) {
        this.enableCustom = enableCustom;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Long getLatestLoginTime() {
        return latestLoginTime;
    }

    public void setLatestLoginTime(Long latestLoginTime) {
        this.latestLoginTime = latestLoginTime;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Boolean getNeedRefreshStrategy() {
        return needRefreshStrategy;
    }

    public void setNeedRefreshStrategy(Boolean needRefreshStrategy) {
        this.needRefreshStrategy = needRefreshStrategy;
    }

    public Integer getDataDisk() {
        return dataDisk;
    }

    public void setDataDisk(Integer dataDisk) {
        this.dataDisk = dataDisk;
    }

    public String getPhysicalServerIp() {
        return physicalServerIp;
    }

    public void setPhysicalServerIp(String physicalServerIp) {
        this.physicalServerIp = physicalServerIp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public VgpuType getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(VgpuType vgpuType) {
        this.vgpuType = vgpuType;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(String desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public CbbDeskRegisterState getRegisterState() {
        return registerState;
    }

    public void setRegisterState(CbbDeskRegisterState registerState) {
        this.registerState = registerState;
    }

    public String getRegisterMessage() {
        return registerMessage;
    }

    public void setRegisterMessage(String registerMessage) {
        this.registerMessage = registerMessage;
    }

    /**
     * Gb 转成 Mb
     *
     * @param size 单位 G
     * @return Mb大小
     */
    public static int gb2Mb(double size) {
        return (int) (size * G_TO_M);
    }
}
