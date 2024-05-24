package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desknetwork;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDnsDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbNetworkStrategyMode;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 创建网络策略Web请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月12日
 * 
 * @author huangxiaodan
 */
public class CreateDeskNetworkWebRequest implements WebRequest {

    /**
     * 网络策略名
     */
    @ApiModelProperty(value = "网络策略名", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String deskNetworkName;

    @ApiModelProperty(value = "dns")
    @NotNull
    private CbbDeskNetworkDnsDTO dns;

    @ApiModelProperty(value = "网络配置信息")
    @Nullable
    private CbbDeskNetworkConfigDTO networkConfig;

    @ApiModelProperty(value = "虚拟交换机ID", required = true)
    @NotNull
    private UUID vswitchId;

    @ApiModelProperty(value = "集群ID")
    @Nullable
    private UUID clusterId;

    @ApiModelProperty(value = "网络模式，DHCP:DHCP模式，IP_POOL：手动配置IP池模式", required = true)
    @NotNull
    private CbbNetworkStrategyMode networkMode;

    @ApiModelProperty(value = "云平台ID", required = true)
    @NotNull
    private UUID platformId;

    public CbbNetworkStrategyMode getNetworkMode() {
        return networkMode;
    }

    public void setNetworkMode(CbbNetworkStrategyMode networkMode) {
        this.networkMode = networkMode;
    }

    public String getDeskNetworkName() {
        return deskNetworkName;
    }

    public void setDeskNetworkName(String deskNetworkName) {
        this.deskNetworkName = deskNetworkName;
    }

    public CbbDeskNetworkDnsDTO getDns() {
        return dns;
    }

    public void setDns(CbbDeskNetworkDnsDTO dns) {
        this.dns = dns;
    }

    public CbbDeskNetworkConfigDTO getNetworkConfig() {
        return networkConfig;
    }

    public void setNetworkConfig(CbbDeskNetworkConfigDTO networkConfig) {
        this.networkConfig = networkConfig;
    }

    public UUID getVswitchId() {
        return vswitchId;
    }

    public void setVswitchId(UUID vswitchId) {
        this.vswitchId = vswitchId;
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
}
