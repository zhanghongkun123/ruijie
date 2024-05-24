package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request;

import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 编辑融合应用池入参
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月26日
 *
 * @author zhengjingyong
 */
public class EditRcaAppPoolWebRequest implements WebRequest {


    @ApiModelProperty(value = "应用池id",required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "应用池名",required = true)
    @TextShort
    @TextName
    @NotBlank
    private String name;

    @ApiModelProperty(value = "池描述信息")
    @TextMedium
    private String description;

    @ApiModelProperty(value = "会话保持时间")
    @Nullable
    private Integer sessionHoldTime;

    @ApiModelProperty(value = "会话保持时间设置方式")
    @Nullable
    private RcaEnum.SessionHoldConfigMode sessionHoldConfigMode;

    @ApiModelProperty(value = "负载均衡模式")
    @Nullable
    private RcaEnum.LoadBalanceMode loadBalanceMode;

    // 多会话配置
    @ApiModelProperty(value = "cpu阈值")
    @Nullable
    private Integer cpuThreshold;

    @ApiModelProperty(value = "内存阈值")
    @Nullable
    private Integer memoryThreshold;

    @ApiModelProperty(value = "系统盘阈值")
    @Nullable
    private Integer systemDiskThreshold;



    // 第三方主机配置
    @ApiModelProperty(value = "第三方主机id数组")
    @Nullable
    private UUID[] hostIdArr;



    // 派生主机配置
    @ApiModelProperty(value = "预启动数量")
    @Nullable
    private Integer preStartHostNum;



    // 多会话配置
    @ApiModelProperty(value = "应用主机最大会话数")
    @Nullable
    private Integer maxHostSessionNum;


    @Nullable
    @ApiModelProperty(value = "应用主机管理员账号")
    private String hostAuthName;

    @Nullable
    @ApiModelProperty(value = "应用主机密码")
    private String hostAuthCode;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Nullable
    public Integer getSessionHoldTime() {
        return sessionHoldTime;
    }

    public void setSessionHoldTime(@Nullable Integer sessionHoldTime) {
        this.sessionHoldTime = sessionHoldTime;
    }

    @Nullable
    public RcaEnum.SessionHoldConfigMode getSessionHoldConfigMode() {
        return sessionHoldConfigMode;
    }

    public void setSessionHoldConfigMode(@Nullable RcaEnum.SessionHoldConfigMode sessionHoldConfigMode) {
        this.sessionHoldConfigMode = sessionHoldConfigMode;
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
    public Integer getPreStartHostNum() {
        return preStartHostNum;
    }

    public void setPreStartHostNum(@Nullable Integer preStartHostNum) {
        this.preStartHostNum = preStartHostNum;
    }

    @Nullable
    public Integer getMaxHostSessionNum() {
        return maxHostSessionNum;
    }

    public void setMaxHostSessionNum(@Nullable Integer maxHostSessionNum) {
        this.maxHostSessionNum = maxHostSessionNum;
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
}
