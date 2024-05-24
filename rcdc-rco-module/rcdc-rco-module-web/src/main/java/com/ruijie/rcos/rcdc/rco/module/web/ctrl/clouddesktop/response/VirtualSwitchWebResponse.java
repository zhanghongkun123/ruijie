package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbVswitchState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.NetworkType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 虚拟交换机信息返回
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/22
 *
 * @author TD
 */
public class VirtualSwitchWebResponse {

    @ApiModelProperty(value = "虚拟交换机ID")
    private UUID id;

    @ApiModelProperty(value = "虚拟交换机名称")
    private String name;

    @ApiModelProperty(value = "虚拟交换机类型")
    private NetworkType networkType;

    @ApiModelProperty(value = "虚拟交换机vlan_id")
    private Integer vlan;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "状态")
    private CbbVswitchState state;

    @ApiModelProperty(value = "计算集群信息")
    private ClusterInfoDTO clusterInfoDTO;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "云平台ID")
    private UUID platformId;

    @ApiModelProperty(value = "云平台名称")
    private String platformName;

    @ApiModelProperty(value = "云平台类型")
    private CloudPlatformType platformType;

    @ApiModelProperty(value = "云平台类型")
    private CloudPlatformStatus platformStatus;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CbbVswitchState getState() {
        return state;
    }

    public void setState(CbbVswitchState state) {
        this.state = state;
    }

    public ClusterInfoDTO getClusterInfoDTO() {
        return clusterInfoDTO;
    }

    public void setClusterInfoDTO(ClusterInfoDTO clusterInfoDTO) {
        this.clusterInfoDTO = clusterInfoDTO;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
