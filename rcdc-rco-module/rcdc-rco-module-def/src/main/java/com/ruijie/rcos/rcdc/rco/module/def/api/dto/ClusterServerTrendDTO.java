package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.Date;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月9日
 * 
 * @author zhuangchenwu
 */
public class ClusterServerTrendDTO {

    private String cpuUsedRate;
    
    private String memoryUsedRate;
    
    private String storageReadBandwidth;
    
    private String storageWriteBandwidth;
    
    private Date createTime;

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

}
