package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbLoadBalanceStrategyEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/5
 *
 * @author zqj
 */
public class UpdateLoadBalanceRequest {

    /**
     * 桌面池Id
     */
    @NotNull
    private UUID id;


    /**
     * 负载均衡计算策略
     */
    @Nullable
    private CbbLoadBalanceStrategyEnum loadBalanceStrategy;

    /**
     * CPU利用率上限
     */
    @Nullable
    @Range(min = "50", max = "100")
    private Integer cpuUsage;

    /**
     * 内存利用率上限
     */
    @Nullable
    @Range(min = "50", max = "100")
    private Integer memoryUsage;

    /**
     * 系统盘利用率上限
     */
    @Nullable
    @Range(min = "50", max = "100")
    private Integer systemDiskUsage;


    /**
     * 最大会话数：0~不做限制
     */
    @Nullable
    @Range(min = "0", max = "99999999")
    private Integer maxSession;

    private Boolean hasMaxSessionChange;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Nullable
    public CbbLoadBalanceStrategyEnum getLoadBalanceStrategy() {
        return loadBalanceStrategy;
    }

    public void setLoadBalanceStrategy(@Nullable CbbLoadBalanceStrategyEnum loadBalanceStrategy) {
        this.loadBalanceStrategy = loadBalanceStrategy;
    }

    @Nullable
    public Integer getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(@Nullable Integer cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    @Nullable
    public Integer getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(@Nullable Integer memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    @Nullable
    public Integer getSystemDiskUsage() {
        return systemDiskUsage;
    }

    public void setSystemDiskUsage(@Nullable Integer systemDiskUsage) {
        this.systemDiskUsage = systemDiskUsage;
    }

    @Nullable
    public Integer getMaxSession() {
        return maxSession;
    }

    public void setMaxSession(@Nullable Integer maxSession) {
        this.maxSession = maxSession;
    }

    public Boolean getHasMaxSessionChange() {
        return hasMaxSessionChange;
    }

    public void setHasMaxSessionChange(Boolean hasMaxSessionChange) {
        this.hasMaxSessionChange = hasMaxSessionChange;
    }
}
