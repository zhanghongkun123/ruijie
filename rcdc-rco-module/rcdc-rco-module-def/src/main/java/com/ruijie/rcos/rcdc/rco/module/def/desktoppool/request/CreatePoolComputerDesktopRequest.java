package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopRole;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 桌面池下Computer云桌面创建Request
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/30
 *
 * @author zqj
 */
public class CreatePoolComputerDesktopRequest {

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
     ** 云桌面关联策略模板ID
     */
    @NotNull
    private UUID strategyId;


    @Nullable
    private UUID softwareStrategyId;

    @Nullable
    private UUID userProfileStrategyId;

    @Nullable
    private String agentVersion;

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

    private String osName;

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

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
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

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    @Nullable
    public String getAgentVersion() {
        return agentVersion;
    }

    public void setAgentVersion(@Nullable String agentVersion) {
        this.agentVersion = agentVersion;
    }
}
