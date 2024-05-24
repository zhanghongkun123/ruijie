package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

/**
 * Title: ServerResourceUsageDayEntity
 * Description: 服务器资源利用率（小时）表实体类
 * Copyright: Ruijie Co., Ltd. (c) 2019
 *
 * @Author: zhangyichi
 * @Date: 2019/7/23 10:12
 */
@Entity
@Table(name = "t_rco_server_resource_usage_hour")
public class ServerResourceUsageHourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Date statisticTime;

    private Date createTime;

    private UUID serverId;

    private Double cpuUsage;

    private Double memoryUsage;

    private Double diskUsage;

    @Version
    private Integer version;

    /**
     * 为了和SQL中的avg方法返回值匹配，使用Double类型
     *
     * @param serverId 服务器ID
     * @param cpuAverageUsage cpu平均使用率
     * @param memoryAverageUsage 内存平均使用率
     * @param diskAverageUsage 硬盘平均使用率
     */
    public ServerResourceUsageHourEntity(UUID serverId, Double cpuAverageUsage, Double memoryAverageUsage, Double diskAverageUsage) {
        this.setServerId(serverId);
        this.setCpuUsage(cpuAverageUsage);
        this.setMemoryUsage(memoryAverageUsage);
        this.setDiskUsage(diskAverageUsage);
    }

    public ServerResourceUsageHourEntity() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getStatisticTime() {
        return statisticTime;
    }

    public void setStatisticTime(Date statisticTime) {
        this.statisticTime = statisticTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public UUID getServerId() {
        return serverId;
    }

    public void setServerId(UUID serverId) {
        this.serverId = serverId;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Double getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(Double diskUsage) {
        this.diskUsage = diskUsage;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
