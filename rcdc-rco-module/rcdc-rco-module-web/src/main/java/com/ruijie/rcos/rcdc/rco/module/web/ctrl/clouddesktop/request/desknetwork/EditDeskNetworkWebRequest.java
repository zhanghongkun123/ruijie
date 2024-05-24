package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desknetwork;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbNetworkStrategyMode;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDnsDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.NetworkType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: 修改网络策略web请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月13日
 * 
 * @author huangxiaodan
 */
public class EditDeskNetworkWebRequest implements WebRequest {

    /**
     * RCDC业务网络策略ID
     */
    @ApiModelProperty(value = "网络策略id", required = true)
    @NotNull
    private UUID id;

    /**
     * 网络策略名
     */
    @ApiModelProperty(value = "网络策略名", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String deskNetworkName;

    /**
     * VLAN ID
     */
    @ApiModelProperty(value = "vlan ID")
    @Nullable
    @Range(min = "1", max = "4094")
    private Integer vlan;

    @ApiModelProperty(value = "网络类型")
    @Nullable
    private NetworkType networkType;

    @ApiModelProperty(value = "dns")
    @Nullable
    private CbbDeskNetworkDnsDTO dns;

    @ApiModelProperty(value = "网络配置信息")
    @Nullable
    private CbbDeskNetworkConfigDTO networkConfig;

    @ApiModelProperty(value = "网络模式，DHCP:DHCP模式，IP_POOL：手动配置IP池模式", required = true)
    @NotNull
    private CbbNetworkStrategyMode networkMode;


    @ApiModelProperty(value = "平台ID", required = false)
    @Nullable
    private UUID platformId;

    public CbbNetworkStrategyMode getNetworkMode() {
        return networkMode;
    }

    public void setNetworkMode(CbbNetworkStrategyMode networkMode) {
        this.networkMode = networkMode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeskNetworkName() {
        return deskNetworkName;
    }

    public void setDeskNetworkName(String deskNetworkName) {
        this.deskNetworkName = deskNetworkName;
    }

    public Integer getVlan() {
        return vlan;
    }

    public void setVlan(Integer vlan) {
        this.vlan = vlan;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
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

    @Nullable
    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(@Nullable UUID platformId) {
        this.platformId = platformId;
    }
}
