package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbLoadBalanceStrategyEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: ThirdParty桌面池编辑web请求
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/31
 *
 * @author zqj
 */
public class UpdateLoadBalanceWebRequest implements WebRequest {

    /**
     * 桌面池Id
     */
    @ApiModelProperty(value = "桌面池Id", required = true)
    @NotNull
    private UUID id;


    /**
     * 负载均衡计算策略
     */
    @ApiModelProperty(value = "负载均衡计算策略", required = true)
    @Nullable
    private CbbLoadBalanceStrategyEnum loadBalanceStrategy;

    /**
     * CPU利用率上限
     */
    @Nullable
    @ApiModelProperty(value = "CPU利用率上限")
    @Range(min = "50", max = "100")
    private Integer cpuUsage;

    /**
     * 内存利用率上限
     */
    @Nullable
    @ApiModelProperty(value = "内存利用率上限")
    @Range(min = "50", max = "100")
    private Integer memoryUsage;

    /**
     * 系统盘利用率上限
     */
    @Nullable
    @ApiModelProperty(value = "内存利用率上限")
    @Range(min = "50", max = "100")
    private Integer systemDiskUsage;


    /**
     * 最大会话数：0~不做限制
     */
    @ApiModelProperty(value = "最大会话数：0~不做限制", required = true)
    @Nullable
    @Range(min = "0", max = "99999999")
    private Integer maxSession;


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
}
