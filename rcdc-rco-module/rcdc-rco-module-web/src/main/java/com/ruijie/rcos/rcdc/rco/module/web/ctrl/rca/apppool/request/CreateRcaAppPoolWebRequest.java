package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request;

import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.request.DeskSpecRequest;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 创建融合应用池入参
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月26日
 *
 * @author zhengjingyong
 */
public class CreateRcaAppPoolWebRequest implements WebRequest {

    @ApiModelProperty(value = "应用池名",required = true)
    @TextShort
    @TextName
    @NotBlank
    private String name;

    @ApiModelProperty(value = "池描述信息")
    @TextMedium
    @Nullable
    private String description;

    @ApiModelProperty(value = "池类型",required = true)
    @NotNull
    private RcaEnum.PoolType poolType;

    @ApiModelProperty(value = "主机来源",required = true)
    @NotNull
    private RcaEnum.HostSourceType hostSourceType;

    @ApiModelProperty(value = "会话类型",required = true)
    @NotNull
    private RcaEnum.HostSessionType sessionType;

    @ApiModelProperty(value = "会话保持时间")
    @Nullable
    private Integer sessionHoldTime;

    @ApiModelProperty(value = "会话保持时间设置方式")
    @Nullable
    private RcaEnum.SessionHoldConfigMode sessionHoldConfigMode;

    @ApiModelProperty(value = "云应用策略id")
    @NotNull
    private UUID mainStrategyId;

    @ApiModelProperty(value = "云应用外设策略id")
    @NotNull
    private UUID peripheralStrategyId;

    @ApiModelProperty(value = "负载均衡模式")
    @Nullable
    private RcaEnum.LoadBalanceMode loadBalanceMode;


    // 动态池配置    =======================================> start

    @ApiModelProperty(value = "cpu阈值")
    @Nullable
    @Range(min = "50", max = "100")
    private Integer cpuThreshold;

    @ApiModelProperty(value = "内存阈值")
    @Nullable
    @Range(min = "50", max = "100")
    private Integer memoryThreshold;

    @ApiModelProperty(value = "系统盘阈值")
    @Nullable
    @Range(min = "50", max = "100")
    private Integer systemDiskThreshold;

    // 动态池配置    =======================================> end





    // 第三方主机配置 =======================================> start
    @ApiModelProperty(value = "第三方主机id数组")
    @Nullable
    private UUID[] hostIdArr;

    // 第三方主机配置 =======================================> end




    // 派生主机配置  =======================================> start
    @ApiModelProperty(value = "应用主机数量")
    @Nullable
    private Integer hostNum;

    @ApiModelProperty(value = "预启动数量")
    @Nullable
    private Integer preStartHostNum;

    @ApiModelProperty(value = "云平台")
    @Nullable
    private UUID platformId;

    @ApiModelProperty(value = "应用镜像")
    @Nullable
    private UUID imageTemplateId;

    @ApiModelProperty(value = "计算集群id")
    @Nullable
    private UUID clusterId;

    @ApiModelProperty(value = "网络策略id")
    @Nullable
    private UUID networkId;

    @ApiModelProperty(value = "镜像派生的VDI应用主机规格")
    @Nullable
    private DeskSpecRequest deskSpecRequest;

    @Nullable
    @ApiModelProperty(value = "应用主机管理员账号")
    private String hostAuthName;

    @Nullable
    @ApiModelProperty(value = "应用主机密码")
    private String hostAuthCode;

    // 派生主机配置  =======================================> end




    // 多会话配置   =======================================> start
    @ApiModelProperty(value = "应用主机最大会话数")
    @Nullable
    private Integer maxHostSessionNum;

    // 多会话配置   =======================================> end


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public RcaEnum.PoolType getPoolType() {
        return poolType;
    }

    public void setPoolType(RcaEnum.PoolType poolType) {
        this.poolType = poolType;
    }

    public RcaEnum.HostSourceType getHostSourceType() {
        return hostSourceType;
    }

    public void setHostSourceType(RcaEnum.HostSourceType hostSourceType) {
        this.hostSourceType = hostSourceType;
    }

    public RcaEnum.HostSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(RcaEnum.HostSessionType sessionType) {
        this.sessionType = sessionType;
    }

    @Nullable
    public Integer getSessionHoldTime() {
        return sessionHoldTime;
    }

    public void setSessionHoldTime(@Nullable Integer sessionHoldTime) {
        this.sessionHoldTime = sessionHoldTime;
    }

    @Nullable
    public RcaEnum.LoadBalanceMode getLoadBalanceMode() {
        return loadBalanceMode;
    }

    public void setLoadBalanceMode(@Nullable RcaEnum.LoadBalanceMode loadBalanceMode) {
        this.loadBalanceMode = loadBalanceMode;
    }

    @Nullable
    public Integer getCpuThreshold() {
        return cpuThreshold;
    }

    public void setCpuThreshold(@Nullable Integer cpuThreshold) {
        this.cpuThreshold = cpuThreshold;
    }

    @Nullable
    public Integer getMemoryThreshold() {
        return memoryThreshold;
    }

    public void setMemoryThreshold(@Nullable Integer memoryThreshold) {
        this.memoryThreshold = memoryThreshold;
    }

    @Nullable
    public Integer getSystemDiskThreshold() {
        return systemDiskThreshold;
    }

    public void setSystemDiskThreshold(@Nullable Integer systemDiskThreshold) {
        this.systemDiskThreshold = systemDiskThreshold;
    }

    @Nullable
    public UUID[] getHostIdArr() {
        return hostIdArr;
    }

    public void setHostIdArr(@Nullable UUID[] hostIdArr) {
        this.hostIdArr = hostIdArr;
    }

    @Nullable
    public Integer getHostNum() {
        return hostNum;
    }

    public void setHostNum(@Nullable Integer hostNum) {
        this.hostNum = hostNum;
    }

    @Nullable
    public Integer getPreStartHostNum() {
        return preStartHostNum;
    }

    public void setPreStartHostNum(@Nullable Integer preStartHostNum) {
        this.preStartHostNum = preStartHostNum;
    }

    @Nullable
    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(@Nullable UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    @Nullable
    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(@Nullable UUID clusterId) {
        this.clusterId = clusterId;
    }

    public UUID getMainStrategyId() {
        return mainStrategyId;
    }

    public void setMainStrategyId(UUID mainStrategyId) {
        this.mainStrategyId = mainStrategyId;
    }

    public UUID getPeripheralStrategyId() {
        return peripheralStrategyId;
    }

    public void setPeripheralStrategyId(UUID peripheralStrategyId) {
        this.peripheralStrategyId = peripheralStrategyId;
    }

    @Nullable
    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(@Nullable UUID networkId) {
        this.networkId = networkId;
    }

    @Nullable
    public Integer getMaxHostSessionNum() {
        return maxHostSessionNum;
    }

    public void setMaxHostSessionNum(@Nullable Integer maxHostSessionNum) {
        this.maxHostSessionNum = maxHostSessionNum;
    }

    @Nullable
    public RcaEnum.SessionHoldConfigMode getSessionHoldConfigMode() {
        return sessionHoldConfigMode;
    }

    public void setSessionHoldConfigMode(@Nullable RcaEnum.SessionHoldConfigMode sessionHoldConfigMode) {
        this.sessionHoldConfigMode = sessionHoldConfigMode;
    }

    @Nullable
    public String getHostAuthName() {
        return hostAuthName;
    }

    public void setHostAuthName(@Nullable String hostAuthName) {
        this.hostAuthName = hostAuthName;
    }

    @Nullable
    public String getHostAuthCode() {
        return hostAuthCode;
    }

    public void setHostAuthCode(@Nullable String hostAuthCode) {
        this.hostAuthCode = hostAuthCode;
    }

    @Nullable
    public DeskSpecRequest getDeskSpecRequest() {
        return deskSpecRequest;
    }

    public void setDeskSpecRequest(@Nullable DeskSpecRequest deskSpecRequest) {
        this.deskSpecRequest = deskSpecRequest;
    }

    @Nullable
    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(@Nullable UUID platformId) {
        this.platformId = platformId;
    }
}
