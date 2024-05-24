package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import org.springframework.util.Assert;

/**
 * Title: ServerResourceUsageDayEntity
 * Description: 服务器资源利用率历史表实体类
 * Copyright: Ruijie Co., Ltd. (c) 2019
 *
 * @Author: zhangyichi
 * @Date: 2019/7/23 10:12
 */
@Entity
@Table(name = "t_rco_server_resource_usage_history")
public class ServerResourceUsageHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Date collectTime;

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
     * @param serverId           服务器ID
     * @param cpuAverageUsage    cpu平均使用率
     * @param memoryAverageUsage 内存平均使用率
     * @param diskAverageUsage   硬盘平均使用率
     */
    public ServerResourceUsageHistoryEntity(UUID serverId, Double cpuAverageUsage, Double memoryAverageUsage,
                                            Double diskAverageUsage) {
        this.setServerId(serverId);
        this.setCpuUsage(cpuAverageUsage);
        this.setMemoryUsage(memoryAverageUsage);
        this.setDiskUsage(diskAverageUsage);
    }

    public ServerResourceUsageHistoryEntity() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
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

    /**
     * 获取使用率
     * @param resourceType 资源类型
     * @return Double 使用率
     */
    public Double getUsage(ResourceTypeEnum resourceType) {
        Assert.notNull(resourceType, "the resourceType is null");
        switch (resourceType) {
            case CPU:
                return getCpuUsage();
            case MEMORY:
                return getMemoryUsage();
            case DISK:
                return getDiskUsage();
            default:
                throw new IllegalArgumentException("the resourceType[" + resourceType + "] is illegal.");
        }
    }
}
