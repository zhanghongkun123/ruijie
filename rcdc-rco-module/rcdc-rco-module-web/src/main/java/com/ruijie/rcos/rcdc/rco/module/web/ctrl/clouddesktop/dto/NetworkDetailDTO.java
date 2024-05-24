package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbNetworkStrategyMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.NetworkType;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * Description: 获取网络策略信息响应，用于编辑网络回填数据
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月19日
 * 
 * @author huangxiaodan
 */
public class NetworkDetailDTO extends PlatformBaseInfoDTO {

    @ApiModelProperty(value = "网络策略ID")
    private UUID deskNetworkId;

    @ApiModelProperty(value = "网络策略名称")
    private String deskNetworkName;

    @ApiModelProperty(value = "管理员名称")
    private String createUser;

    @ApiModelProperty(value = "网络类型")
    private NetworkType networkType;

    @ApiModelProperty(value = "vlan")
    private Integer vlan;

    @ApiModelProperty(value = "dns")
    private CbbDeskNetworkDnsDTO dns;

    @ApiModelProperty(value = "网络配置信息")
    private CbbDeskNetworkConfigDTO networkConfig;

    @ApiModelProperty(value = "虚拟交换机")
    private CbbVswitchDTO vswitch;

    @ApiModelProperty(value = "网络模式，DHCP:DHCP模式，IP_POOL：手动配置IP池模式")
    private CbbNetworkStrategyMode networkMode;

    @ApiModelProperty(value = "镜像模板引用数量")
    private Integer refCountByImageTemplate;

    @ApiModelProperty(value = "云桌面引用数量")
    private Integer refCountByDesk;

    @ApiModelProperty(value = "网络策略所属计算资源")
    private ClusterInfoDTO clusterInfo;

    public UUID getDeskNetworkId() {
        return deskNetworkId;
    }

    public void setDeskNetworkId(UUID deskNetworkId) {
        this.deskNetworkId = deskNetworkId;
    }

    public String getDeskNetworkName() {
        return deskNetworkName;
    }

    public void setDeskNetworkName(String deskNetworkName) {
        this.deskNetworkName = deskNetworkName;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }

    public Integer getVlan() {
        return vlan;
    }

    public void setVlan(Integer vlan) {
        this.vlan = vlan;
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

    public CbbVswitchDTO getVswitch() {
        return vswitch;
    }

    public void setVswitch(CbbVswitchDTO vswitch) {
        this.vswitch = vswitch;
    }

    public CbbNetworkStrategyMode getNetworkMode() {
        return networkMode;
    }

    public void setNetworkMode(CbbNetworkStrategyMode networkMode) {
        this.networkMode = networkMode;
    }

    public Integer getRefCountByImageTemplate() {
        return refCountByImageTemplate;
    }

    public void setRefCountByImageTemplate(Integer refCountByImageTemplate) {
        this.refCountByImageTemplate = refCountByImageTemplate;
    }

    public Integer getRefCountByDesk() {
        return refCountByDesk;
    }

    public void setRefCountByDesk(Integer refCountByDesk) {
        this.refCountByDesk = refCountByDesk;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public ClusterInfoDTO getClusterInfo() {
        return clusterInfo;
    }

    public void setClusterInfo(ClusterInfoDTO clusterInfo) {
        this.clusterInfo = clusterInfo;
    }
}
