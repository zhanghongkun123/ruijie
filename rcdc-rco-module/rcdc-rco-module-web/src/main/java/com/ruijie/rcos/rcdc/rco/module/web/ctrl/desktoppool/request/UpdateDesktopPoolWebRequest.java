package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbLoadBalanceStrategyEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.VdiDesktopConfigVO;
import com.ruijie.rcos.sk.base.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 更新桌面池WebRequest
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 14:39
 *
 * @author linke
 */
public class UpdateDesktopPoolWebRequest {

    /**
     * 桌面池Id
     */
    @ApiModelProperty(value = "桌面池Id", required = true)
    @NotNull
    private UUID id;

    /**
     * 桌面池名称
     */
    @ApiModelProperty(value = "桌面池名称", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String name;

    /**
     * 维持预启动数,数量不能超过desktopNum
     */
    @ApiModelProperty(value = "维持预启动数", required = true)
    @Nullable
    @Range(min = "0", max = "1000")
    private Integer preStartDesktopNum;

    /**
     * 空闲桌面自动回收时间（分钟）
     **/
    @ApiModelProperty(value = "空闲桌面自动回收时间（分钟）")
    @Nullable
    @Range(min = "0", max = "99999999")
    private Integer idleDesktopRecover;

    @Nullable
    @TextMedium
    private String description;

    @ApiModelProperty(value = "VDI桌面配置")
    @Nullable
    private VdiDesktopConfigVO vdiDesktopConfig;

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

    /**
     * 会话类型：单会话、多会话
     */
    @ApiModelProperty(value = "会话类型", required = true)
    @Nullable
    private CbbDesktopSessionType sessionType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public Integer getPreStartDesktopNum() {
        return preStartDesktopNum;
    }

    public void setPreStartDesktopNum(@Nullable Integer preStartDesktopNum) {
        this.preStartDesktopNum = preStartDesktopNum;
    }

    @Nullable
    public Integer getIdleDesktopRecover() {
        return idleDesktopRecover;
    }

    public void setIdleDesktopRecover(@Nullable Integer idleDesktopRecover) {
        this.idleDesktopRecover = idleDesktopRecover;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public VdiDesktopConfigVO getVdiDesktopConfig() {
        return vdiDesktopConfig;
    }

    public void setVdiDesktopConfig(@Nullable VdiDesktopConfigVO vdiDesktopConfig) {
        this.vdiDesktopConfig = vdiDesktopConfig;
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

    @Nullable
    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(@Nullable CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }
}
