package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopRole;
import com.ruijie.rcos.rcdc.task.ext.module.def.dto.WrapBatchTaskItemDTO;
import com.ruijie.rcos.sk.base.annotation.IPv4Address;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * 创建云桌面数据请求参数
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 * 
 * @author  artom
 */
public class CreateCloudDesktopRequest extends WrapBatchTaskItemDTO implements Request {
    
    /**
     * *云桌面名称
     */
    @Nullable
    private String desktopName;

    /**
     * *云桌面关联镜像模板ID
     */
    @NotNull
    private UUID desktopImageId;

    /**
     ** 云桌面关联策略模板ID
     */
    @NotNull
    private UUID strategyId;

    /**
     * *用户ID
     */
    @NotNull
    private UUID userId;
    
    /**
     ** 云桌面关联网络模板ID
     */
    @NotNull
    private UUID networkId;
    
    /**
     ** 云桌面静态IP
     */
    @IPv4Address
    @Nullable
    private String desktopIp;

    /**
     * 云桌面角色，普通或vip
     */
    @NotNull
    private DesktopRole desktopRole = DesktopRole.NORMAL;

    @Nullable
    private UUID customTaskId;

    @Nullable
    private UUID softwareStrategyId;

    @Nullable
    private UUID userProfileStrategyId;

    /**
     ** 运行集群ID
     */
    @Nullable
    private UUID clusterId;

    @Nullable
    private UUID platformId;

    /**
     * 规格
     */
    @NotNull
    private CbbDeskSpecDTO deskSpec;

    @Nullable
    public UUID getSoftwareStrategyId() {
        return softwareStrategyId;
    }

    public void setSoftwareStrategyId(@Nullable UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public UUID getDesktopImageId() {
        return desktopImageId;
    }

    public void setDesktopImageId(UUID desktopImageId) {
        this.desktopImageId = desktopImageId;
    }

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    public String getDesktopIp() {
        return desktopIp;
    }

    public void setDesktopIp(String desktopIp) {
        this.desktopIp = desktopIp;
    }

    public DesktopRole getDesktopRole() {
        return desktopRole;
    }

    public void setDesktopRole(DesktopRole desktopRole) {
        this.desktopRole = desktopRole;
    }

    @Nullable
    public UUID getCustomTaskId() {
        return customTaskId;
    }

    public void setCustomTaskId(@Nullable UUID customTaskId) {
        this.customTaskId = customTaskId;
    }

    @Nullable
    public UUID getUserProfileStrategyId() {
        return userProfileStrategyId;
    }

    public void setUserProfileStrategyId(@Nullable UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }

    @Nullable
    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(@Nullable UUID clusterId) {
        this.clusterId = clusterId;
    }

    public CbbDeskSpecDTO getDeskSpec() {
        return deskSpec;
    }

    public void setDeskSpec(CbbDeskSpecDTO deskSpec) {
        this.deskSpec = deskSpec;
    }

    @Nullable
    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(@Nullable UUID platformId) {
        this.platformId = platformId;
    }
}
