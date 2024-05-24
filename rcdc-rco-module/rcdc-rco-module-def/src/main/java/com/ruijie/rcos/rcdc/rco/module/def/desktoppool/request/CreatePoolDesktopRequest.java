package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopRole;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 桌面池下云桌面创建Request
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/03 17:16
 *
 * @author linke
 */
public class CreatePoolDesktopRequest {

    /**
     * 云桌面id
     */
    @NotNull
    private UUID desktopId;

    /**
     * *云桌面名称
     */
    @NotBlank
    private String desktopName;

    /**
     * 桌面池Id
     */
    @NotNull
    private UUID poolId;

    @NotBlank
    private String poolName;

    /**
     * *云桌面关联镜像模板ID
     */
    @NotNull
    private UUID imageTemplateId;

    /**
     ** 云桌面关联策略模板ID
     */
    @NotNull
    private UUID strategyId;

    /**
     ** 云桌面关联网络模板ID
     */
    @NotNull
    private UUID networkId;

    @Nullable
    private UUID softwareStrategyId;

    @Nullable
    private UUID userProfileStrategyId;

    /**
     * 云桌面角色，普通或vip
     */
    @NotNull
    private DesktopRole desktopRole = DesktopRole.NORMAL;

    /**
     * 云桌面-桌面池类型 独享云桌面或共享云桌面
     */
    @NotNull
    private DesktopPoolType poolDeskType;

    /**
     * 计算集群位置
     */
    @NotNull
    private UUID clusterId;

    @NotNull
    private UUID platformId;

    /**
     * 规格
     */
    @NotNull
    private CbbDeskSpecDTO deskSpec;

    @Nullable
    private CbbDesktopSessionType sessionType;


    @Nullable
    private BatchTaskItem item;

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public UUID getPoolId() {
        return poolId;
    }

    public void setPoolId(UUID poolId) {
        this.poolId = poolId;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    @Nullable
    public UUID getSoftwareStrategyId() {
        return softwareStrategyId;
    }

    public void setSoftwareStrategyId(@Nullable UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }

    @Nullable
    public UUID getUserProfileStrategyId() {
        return userProfileStrategyId;
    }

    public void setUserProfileStrategyId(@Nullable UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }

    public DesktopRole getDesktopRole() {
        return desktopRole;
    }

    public void setDesktopRole(DesktopRole desktopRole) {
        this.desktopRole = desktopRole;
    }

    public DesktopPoolType getPoolDeskType() {
        return poolDeskType;
    }

    public void setPoolDeskType(DesktopPoolType poolDeskType) {
        this.poolDeskType = poolDeskType;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    public CbbDeskSpecDTO getDeskSpec() {
        return deskSpec;
    }

    public void setDeskSpec(CbbDeskSpecDTO deskSpec) {
        this.deskSpec = deskSpec;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    @Nullable
    public BatchTaskItem getItem() {
        return item;
    }

    public void setItem(@Nullable BatchTaskItem item) {
        this.item = item;
    }
}
