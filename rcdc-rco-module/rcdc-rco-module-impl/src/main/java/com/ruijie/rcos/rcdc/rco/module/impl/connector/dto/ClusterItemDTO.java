package com.ruijie.rcos.rcdc.rco.module.impl.connector.dto;

import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/6
 *
 * @author liangyifeng
 */
public class ClusterItemDTO {
    private UUID id;

    private String clusterName;

    private String clusterDesc;

    private String clusterState;

    private Date createTime;

    private Date lastUpdateTime;

    private NodeItemDTO[] nodeArr;

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

    public NodeItemDTO[] getNodeArr() {
        return nodeArr;
    }

    public void setNodeArr(NodeItemDTO[] nodeArr) {
        this.nodeArr = nodeArr;
    }
}
