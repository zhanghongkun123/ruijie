package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.task.ext.module.def.dto.WrapBatchTaskItemDTO;
import com.ruijie.rcos.sk.base.annotation.IPv4Address;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * 创建ThirdParty云桌面数据请求参数
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time:
 *
 * @author  zqj
 */
public class CreateThirdPartyDesktopRequest extends WrapBatchTaskItemDTO implements Request {

    @Nullable
    private UUID deskId;

    /**
     * *云桌面名称
     */
    @Nullable
    private String desktopName;

    /**
     * 桌面池Id
     */
    @Nullable
    private UUID poolId;

    @Nullable
    private String poolName;

    /**
     ** 云桌面关联策略模板ID
     */
    @NotNull
    private UUID strategyId;

    /**
     * *用户ID
     */
    @Nullable
    private UUID userId;

    /**
     ** 云桌面静态IP
     */
    @IPv4Address
    @Nullable
    private String desktopIp;


    @Nullable
    private UUID customTaskId;

    @Nullable
    private UUID softwareStrategyId;

    @Nullable
    private UUID userProfileStrategyId;

    /**
     * 云桌面-桌面池类型 独享云桌面或共享云桌面
     */
    @NotNull
    private DesktopPoolType poolDeskType;

    private String osName;

    @Nullable
    private String agentVersion;

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

    public String getDesktopIp() {
        return desktopIp;
    }

    public void setDesktopIp(String desktopIp) {
        this.desktopIp = desktopIp;
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
    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(@Nullable UUID deskId) {
        this.deskId = deskId;
    }

    @Nullable
    public UUID getPoolId() {
        return poolId;
    }

    public void setPoolId(@Nullable UUID poolId) {
        this.poolId = poolId;
    }

    @Nullable
    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(@Nullable String poolName) {
        this.poolName = poolName;
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
