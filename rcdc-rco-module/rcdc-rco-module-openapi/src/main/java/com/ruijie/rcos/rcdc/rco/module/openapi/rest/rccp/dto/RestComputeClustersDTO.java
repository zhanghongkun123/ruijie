package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccp.dto;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 计算集群DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/18
 *
 * @author WuShengQiang
 */
public class RestComputeClustersDTO {

    private UUID id;

    private String clusterName;

    private String clusterDesc;

    private String clusterState;

    private Set<String> archSet;
    
    private UUID platformId;

    private String platformName;

    private Date createTime;

    private Date lastUpdateTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getClusterDesc() {
        return clusterDesc;
    }

    public void setClusterDesc(String clusterDesc) {
        this.clusterDesc = clusterDesc;
    }

    public String getClusterState() {
        return clusterState;
    }

    public void setClusterState(String clusterState) {
        this.clusterState = clusterState;
    }

    public Set<String> getArchSet() {
        return archSet;
    }

    public void setArchSet(Set<String> archSet) {
        this.archSet = archSet;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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
