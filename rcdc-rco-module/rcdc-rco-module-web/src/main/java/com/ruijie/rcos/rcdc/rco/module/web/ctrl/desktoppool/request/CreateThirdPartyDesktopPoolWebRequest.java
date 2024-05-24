package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbLoadBalanceStrategyEnum;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: ThirdParty桌面池创建web请求
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/31
 *
 * @author zqj
 */
public class CreateThirdPartyDesktopPoolWebRequest implements WebRequest {

    /**
     * 桌面池名称
     */
    @ApiModelProperty(value = "桌面池名称", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String name;

    /**
     * 桌面池类型
     **/
    @ApiModelProperty(value = "桌面池类型", required = true)
    @NotNull
    private CbbDesktopPoolType poolType;

    /**
     * 桌面会话类型：单会话（SINGLE）、多会话（MULTI）
     */
    @ApiModelProperty(value = "桌面会话类型：单会话（SINGLE）、多会话（MULTI）", required = true)
    @NotNull
    private CbbDesktopSessionType sessionType;

    /**
     * 桌面池类型：STATIC、DYNAMIC
     */
    @ApiModelProperty(value = "单会话，必填桌面池类型：STATIC、DYNAMIC", required = true)
    @Nullable
    private CbbDesktopPoolModel poolModel;

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
     * 空闲桌面自动回收时间（分钟）
     **/
    @ApiModelProperty(value = "空闲桌面自动回收时间（分钟）")
    @Nullable
    @Range(min = "0", max = "99999999")
    private Integer idleDesktopRecover;

    @ApiModelProperty(value = "描述")
    @Nullable
    @TextMedium
    private String description;


    @ApiModelProperty(value = "云桌面策略ID", required = true)
    @NotNull
    private UUID strategyId;

    @ApiModelProperty(value = "软件策略ID")
    @Nullable
    private UUID softwareStrategyId;

    @ApiModelProperty(value = "UPM策略ID")
    @Nullable
    private UUID userProfileStrategyId;

    /**
     * PC终端数组Id
     */
    @ApiModelProperty(value = "PC终端数组Id")
    @Nullable
    private UUID[] computerIdArr;

    /**
     * PC终端组
     */
    @ApiModelProperty(value = "PC终端组")
    @Nullable
    private UUID[] terminalGroupIdArr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Nullable
    public CbbDesktopPoolModel getPoolModel() {
        return poolModel;
    }

    public void setPoolModel(@Nullable CbbDesktopPoolModel poolModel) {
        this.poolModel = poolModel;
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

    public void setSoftwareStrategyId(UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }

    @Nullable
    public UUID getUserProfileStrategyId() {
        return userProfileStrategyId;
    }

    public void setUserProfileStrategyId(@Nullable UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }

    public CbbDesktopPoolType getPoolType() {
        return poolType;
    }

    public void setPoolType(CbbDesktopPoolType poolType) {
        this.poolType = poolType;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    @Nullable
    public UUID[] getComputerIdArr() {
        return computerIdArr;
    }

    public void setComputerIdArr(@Nullable UUID[] computerIdArr) {
        this.computerIdArr = computerIdArr;
    }

    @Nullable
    public UUID[] getTerminalGroupIdArr() {
        return terminalGroupIdArr;
    }

    public void setTerminalGroupIdArr(@Nullable UUID[] terminalGroupIdArr) {
        this.terminalGroupIdArr = terminalGroupIdArr;
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

    public Integer getMaxSession() {
        return maxSession;
    }

    public void setMaxSession(Integer maxSession) {
        this.maxSession = maxSession;
    }
}
