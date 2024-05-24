package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkIpPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkIpPoolUseDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbVswitchDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbNetworkStrategyMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.NetworkType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.sk.base.annotation.IPv4Address;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.UUID;

/**
 * 
 * Description: 获取网络策略信息响应
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月19日
 * 
 * @author huangxiaodan
 */
public class NetworkInfoDTO {

    @ApiModelProperty(value = "网络策略id")
    private UUID deskNetworkId;

    @ApiModelProperty(value = "网络策略名称")
    private String deskNetworkName;

    @ApiModelProperty(value = "网络类型")
    private NetworkType networkType;

    @ApiModelProperty(value = "vlan")
    private Integer vlan;

    @ApiModelProperty(value = "首选dns")
    @IPv4Address
    private String dnsPrimary;

    @ApiModelProperty(value = "备用dns")
    @IPv4Address
    private String dnsSecondary;

    @ApiModelProperty(value = "网络地址（CIDR）")
    private String ipCidr;

    @ApiModelProperty(value = "网关")
    private String gateway;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "地址池")
    private CbbDeskNetworkIpPoolDTO[] ipPoolArr;

    @ApiModelProperty(value = "ip使用情况")
    private CbbDeskNetworkIpPoolUseDTO ipPoolUse;

    @ApiModelProperty(value = "虚拟交换机")
    private CbbVswitchDTO vswitch;

    @ApiModelProperty(value = "网络模式，DHCP:DHCP模式，IP_POOL：手动配置IP池模式")
    private CbbNetworkStrategyMode networkMode;

    @ApiModelProperty(value = "管理员名称")
    private String creatorUserName;

    @ApiModelProperty(value = "网络策略所属计算资源")
    private ClusterInfoDTO clusterInfo;

    @ApiModelProperty(value = "被云桌面引用的数量")
    private Integer refCountByDesk;

    @ApiModelProperty(value = "被镜像模板引用的数量")
    private Integer refCountByImageTemplate;

    @ApiModelProperty(value = "绑定用户组数量")
    private Integer bindUserGroupCount;

    public Integer getBindUserGroupCount() {
        return bindUserGroupCount;
    }

    public void setBindUserGroupCount(Integer bindUserGroupCount) {
        this.bindUserGroupCount = bindUserGroupCount;
    }

    public Integer getRefCountByDesk() {
        return refCountByDesk;
    }

    public void setRefCountByDesk(Integer refCountByDesk) {
        this.refCountByDesk = refCountByDesk;
    }

    public Integer getRefCountByImageTemplate() {
        return refCountByImageTemplate;
    }

    public void setRefCountByImageTemplate(Integer refCountByImageTemplate) {
        this.refCountByImageTemplate = refCountByImageTemplate;
    }

    @ApiModelProperty(value = "云平台ID")
    private UUID platformId;

    @ApiModelProperty(value = "云平台名称")
    private String platformName;

    @ApiModelProperty(value = "云平台类型")
    private CloudPlatformType platformType;

    @ApiModelProperty(value = "云平台状态")
    private CloudPlatformStatus platformStatus;

    public CbbNetworkStrategyMode getNetworkMode() {
        return networkMode;
    }

    public void setNetworkMode(CbbNetworkStrategyMode networkMode) {
        this.networkMode = networkMode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

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

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public Integer getVlan() {
        return vlan;
    }

    public void setVlan(Integer vlan) {
        this.vlan = vlan;
    }

    public String getDnsPrimary() {
        return dnsPrimary;
    }

    public void setDnsPrimary(String dnsPrimary) {
        this.dnsPrimary = dnsPrimary;
    }

    public String getDnsSecondary() {
        return dnsSecondary;
    }

    public void setDnsSecondary(String dnsSecondary) {
        this.dnsSecondary = dnsSecondary;
    }

    public String getIpCidr() {
        return ipCidr;
    }

    public void setIpCidr(String ipCidr) {
        this.ipCidr = ipCidr;
    }

    public CbbDeskNetworkIpPoolDTO[] getIpPoolArr() {
        return ipPoolArr;
    }

    public void setIpPoolArr(CbbDeskNetworkIpPoolDTO[] ipPoolArr) {
        this.ipPoolArr = ipPoolArr;
    }

    public CbbDeskNetworkIpPoolUseDTO getIpPoolUse() {
        return ipPoolUse;
    }

    public void setIpPoolUse(CbbDeskNetworkIpPoolUseDTO ipPoolUse) {
        this.ipPoolUse = ipPoolUse;
    }

    public CbbVswitchDTO getVswitch() {
        return vswitch;
    }

    public void setVswitch(CbbVswitchDTO vswitch) {
        this.vswitch = vswitch;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public ClusterInfoDTO getClusterInfo() {
        return clusterInfo;
    }

    public void setClusterInfo(ClusterInfoDTO clusterInfo) {
        this.clusterInfo = clusterInfo;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public CloudPlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(CloudPlatformType platformType) {
        this.platformType = platformType;
    }

    public CloudPlatformStatus getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(CloudPlatformStatus platformStatus) {
        this.platformStatus = platformStatus;
    }
}
