package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbNetworkStrategyMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DeskNetworkState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.NetworkType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
public class RestDeskNetworkDTO {

    private UUID id;

    /**
     * 网络名称
     **/
    private String deskNetworkName;

    /**
     * ip子网信息，例如：192.168.1.0/24
     **/
    private String ipCidr;

    private Date createTime;

    /**
     * 管理员名称
     */
    private String creatorUserName;

    /**
     * 网络状态
     */
    private DeskNetworkState deskNetworkState;

    /**
     * 虚拟网络数类型 （vlan or 扁平网络类型）
     */
    private NetworkType networkType;

    /**
     * 虚拟网络类型 （ip池 or dhcp）
     */
    private CbbNetworkStrategyMode networkMode;

    /**
     * 网络主dns
     **/
    private String dnsPrimary;

    /**
     * 网络备dns
     **/
    private String dnsSecondary;

    /**
     * 网关地址
     **/
    private String gateway;

    /**
     * 子网vlan
     **/
    private Integer vlan;


    /**
     * 已使用IP数量
     */
    private Integer refCount;

    /**
     * 可用数量
     */
    private Integer totalCount;

    /**
     * 计算集群ID
     */
    private UUID clusterId;


    /**
     * 计算集群ID列表
     */
    private List<UUID> clusterIdList;

    /**
     * 平台ID
     */
    private UUID platformId;

    /**
     * 云平台名称
     */
    private String platformName;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getIpCidr() {
        return ipCidr;
    }

    public void setIpCidr(String ipCidr) {
        this.ipCidr = ipCidr;
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

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public DeskNetworkState getDeskNetworkState() {
        return deskNetworkState;
    }

    public void setDeskNetworkState(DeskNetworkState deskNetworkState) {
        this.deskNetworkState = deskNetworkState;
    }

    public Integer getRefCount() {
        return refCount;
    }

    public void setRefCount(Integer refCount) {
        this.refCount = refCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }

    public CbbNetworkStrategyMode getNetworkMode() {
        return networkMode;
    }

    public void setNetworkMode(CbbNetworkStrategyMode networkMode) {
        this.networkMode = networkMode;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public List<UUID> getClusterIdList() {
        return clusterIdList;
    }

    public void setClusterIdList(List<UUID> clusterIdList) {
        this.clusterIdList = clusterIdList;
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
}
