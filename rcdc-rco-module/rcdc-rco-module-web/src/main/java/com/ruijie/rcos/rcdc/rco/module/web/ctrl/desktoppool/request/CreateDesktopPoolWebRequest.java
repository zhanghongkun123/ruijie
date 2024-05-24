package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbLoadBalanceStrategyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.request.DeskSpecRequest;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 桌面池创建web请求
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/19 10:56
 *
 * @author yanlin
 */
public class CreateDesktopPoolWebRequest implements WebRequest {

    /**
     * 桌面池名称
     */
    @ApiModelProperty(value = "桌面池名称", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String name;

    /**
     * 云桌面名称前缀,为null时采用桌面池名称
     */
    @ApiModelProperty(value = "云桌面名称前缀,为null时采用桌面池名称")
    @Nullable
    @TextShort
    @TextName
    private String desktopNamePrefix;

    /**
     * 桌面池类型：STATIC、DYNAMIC
     */
    @ApiModelProperty(value = "单会话，必填桌面池类型：STATIC、DYNAMIC", required = true)
    @Nullable
    private CbbDesktopPoolModel poolModel;

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

    @ApiModelProperty(value = "镜像模板ID", required = true)
    @NotNull
    private UUID imageTemplateId;

    @ApiModelProperty(value = "云桌面策略ID", required = true)
    @NotNull
    private UUID strategyId;

    @ApiModelProperty(value = "网络策略ID", required = true)
    @NotNull
    private UUID networkId;

    @ApiModelProperty(value = "软件策略ID")
    @Nullable
    private UUID softwareStrategyId;

    @ApiModelProperty(value = "UPM策略ID")
    @Nullable
    private UUID userProfileStrategyId;

    /**
     ** 运行集群ID
     */
    @ApiModelProperty(value = "运行集群ID", required = true)
    @NotNull
    private UUID clusterId;

    /**
     * 云平台ID
     */
    @ApiModelProperty(value = "云平台ID", required = true)
    @NotNull
    private UUID platformId;

    /**
     * 池中桌面数量
     */
    @ApiModelProperty(value = "池中桌面数量", required = true)
    @NotNull
    @Range(min = "1", max = "1000")
    private Integer desktopNum;

    /**
     * 维持预启动数,数量不能超过desktopNum
     */
    @ApiModelProperty(value = "维持预启动数", required = false)
    @Nullable
    @Range(min = "0", max = "1000")
    private Integer preStartDesktopNum;

    @ApiModelProperty(value = "桌面规格", required = false)
    @Nullable
    private DeskSpecRequest deskSpec;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDesktopNamePrefix() {
        return desktopNamePrefix;
    }

    public void setDesktopNamePrefix(@Nullable String desktopNamePrefix) {
        this.desktopNamePrefix = desktopNamePrefix;
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

    public Integer getDesktopNum() {
        return desktopNum;
    }

    public void setDesktopNum(Integer desktopNum) {
        this.desktopNum = desktopNum;
    }

    @Nullable
    public Integer getPreStartDesktopNum() {
        return preStartDesktopNum;
    }

    public void setPreStartDesktopNum(@Nullable Integer preStartDesktopNum) {
        this.preStartDesktopNum = preStartDesktopNum;
    }

    @Nullable
    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(@Nullable UUID clusterId) {
        this.clusterId = clusterId;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    @Nullable
    public DeskSpecRequest getDeskSpec() {
        return deskSpec;
    }

    public void setDeskSpec(@Nullable DeskSpecRequest deskSpec) {
        this.deskSpec = deskSpec;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
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

    public CbbDesktopPoolType getPoolType() {
        return poolType;
    }

    public void setPoolType(CbbDesktopPoolType poolType) {
        this.poolType = poolType;
    }
}
