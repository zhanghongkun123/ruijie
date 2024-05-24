package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;

/**
 * 集群服务器信息趋势实体类
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月9日
 * 
 * @author zhuangchenwu
 */
@Entity
@Table(name = "t_rco_cluster_server_trend")
public class ClusterServerTrendEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String cpuUsedRate = Constants.STRING_0;
    
    private String memoryUsedRate = Constants.STRING_0;
    
    private String storageReadBandwidth = Constants.STRING_0;
    
    private String storageWriteBandwidth = Constants.STRING_0;
    
    private Date createTime;
    
    @Version
    private int version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCpuUsedRate() {
        return cpuUsedRate;
    }

    public void setCpuUsedRate(String cpuUsedRate) {
        this.cpuUsedRate = cpuUsedRate;
    }

    public String getMemoryUsedRate() {
        return memoryUsedRate;
    }

    public void setMemoryUsedRate(String memoryUsedRate) {
        this.memoryUsedRate = memoryUsedRate;
    }

    public String getStorageReadBandwidth() {
        return storageReadBandwidth;
    }

    public void setStorageReadBandwidth(String storageReadBandwidth) {
        this.storageReadBandwidth = storageReadBandwidth;
    }

    public String getStorageWriteBandwidth() {
        return storageWriteBandwidth;
    }

    public void setStorageWriteBandwidth(String storageWriteBandwidth) {
        this.storageWriteBandwidth = storageWriteBandwidth;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    
}
