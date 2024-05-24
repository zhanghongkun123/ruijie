package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkIpPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbVswitchDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbNetworkStrategyMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DeskNetworkState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.IpType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.NetworkType;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 网络策略信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 17:12 2020/5/14
 *
 * @author yxd
 */
public class DeskNetworkDTO {

    private UUID id;

    private String deskNetworkName;

    private String ipCidr;

    private String gateway;

    private Integer vlan;

    private DeskNetworkState deskNetworkState;

    private Integer totalCount;

    private Integer refCount;

    private IpType ipType;

    private NetworkType networkType;

    private String dnsPrimary;

    private String dnsSecondary;

    private CbbDeskNetworkIpPoolDTO[] ipPoolArr;

    private Date createTime;

    private CbbVswitchDTO vswitchInfo;

    private Boolean bindUserGroup;

    private CbbNetworkStrategyMode networkMode;

    public CbbNetworkStrategyMode getNetworkMode() {
        return networkMode;
    }

    public void setNetworkMode(CbbNetworkStrategyMode networkMode) {
        this.networkMode = networkMode;
    }

    public DeskNetworkDTO() {
        this.ipType = IpType.IPV4;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeskNetworkName() {
        return this.deskNetworkName;
    }

    public void setDeskNetworkName(String deskNetworkName) {
        this.deskNetworkName = deskNetworkName;
    }

    public String getIpCidr() {
        return this.ipCidr;
    }

    public void setIpCidr(String ipCidr) {
        this.ipCidr = ipCidr;
    }

    public String getGateway() {
        return this.gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public Integer getVlan() {
        return this.vlan;
    }

    public void setVlan(Integer vlan) {
        this.vlan = vlan;
    }

    public DeskNetworkState getDeskNetworkState() {
        return this.deskNetworkState;
    }

    public void setDeskNetworkState(DeskNetworkState deskNetworkState) {
        this.deskNetworkState = deskNetworkState;
    }

    public Integer getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getRefCount() {
        return this.refCount;
    }

    public void setRefCount(Integer refCount) {
        this.refCount = refCount;
    }

    public NetworkType getNetworkType() {
        return this.networkType;
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }

    public String getDnsPrimary() {
        return this.dnsPrimary;
    }

    public void setDnsPrimary(String dnsPrimary) {
        this.dnsPrimary = dnsPrimary;
    }

    public String getDnsSecondary() {
        return this.dnsSecondary;
    }

    public void setDnsSecondary(String dnsSecondary) {
        this.dnsSecondary = dnsSecondary;
    }

    public CbbDeskNetworkIpPoolDTO[] getIpPoolArr() {
        return this.ipPoolArr;
    }

    public void setIpPoolArr(CbbDeskNetworkIpPoolDTO[] ipPoolArr) {
        this.ipPoolArr = ipPoolArr;
    }

    public IpType getIpType() {
        return this.ipType;
    }

    public void setIpType(IpType ipType) {
        this.ipType = ipType;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public CbbVswitchDTO getVswitchInfo() {
        return vswitchInfo;
    }

    public void setVswitchInfo(CbbVswitchDTO vswitchInfo) {
        this.vswitchInfo = vswitchInfo;
    }

    public Boolean getBindUserGroup() {
        return bindUserGroup;
    }

    public void setBindUserGroup(Boolean bindUserGroup) {
        this.bindUserGroup = bindUserGroup;
    }
}
