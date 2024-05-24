package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccp.response;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.cluster.RccpClusterResourceResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.AbstractOpenAPIResponse;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.UUID;

/**
 * Description: rccp计算集群资源统计列表
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/25 14:57
 *
 * @author lyb
 */
public class ComputeClustersSummaryResponse extends AbstractOpenAPIResponse<PageResponse<RccpClusterResourceResponse>> {

    /**
     * 总数
     */
    public Long total = 0L;

    /**
     * 结果项
     */
    public ComputeClustersSummaryItem[] itemArr;

    @Override
    public void dtoToResponse(PageResponse<RccpClusterResourceResponse> response) {
        Assert.notNull(response, "response must not be null.");

        setTotal(response.getTotal());
        if (response.getItems() != null) {
            setItemArr(Arrays.stream(response.getItems()).map(ComputeClustersSummaryItem::new).toArray(ComputeClustersSummaryItem[]::new));
        }
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public ComputeClustersSummaryItem[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(ComputeClustersSummaryItem[] itemArr) {
        this.itemArr = itemArr;
    }
}


/**
 * Description: rccp计算集群资源统计条目
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/25 14:57
 *
 * @author lyb
 */
class ComputeClustersSummaryItem {

    ComputeClustersSummaryItem(RccpClusterResourceResponse dto) {
        BeanUtils.copyProperties(dto, this);
        setClusterId(dto.getId());
        setAllocatedCpuCores(dto.getAllocatedCpu());
        setTotalCpuCores(dto.getTotalCpu());
    }

    /**
     * 计算集群id
     */
    public UUID clusterId;

    /**
     * 计算集群名称
     */
    public String clusterName;

    /**
     * 集群已分配cpu数
     */
    public Integer allocatedCpuCores;

    /**
     * 集群已分配内存数
     */
    public Long allocatedMemory;

    /**
     * 集群cpu总数
     */
    public Integer totalCpuCores;

    /**
     * 集群内存总数
     */
    public Long totalMemory;

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Integer getAllocatedCpuCores() {
        return allocatedCpuCores;
    }

    public void setAllocatedCpuCores(Integer allocatedCpuCores) {
        this.allocatedCpuCores = allocatedCpuCores;
    }

    public Long getAllocatedMemory() {
        return allocatedMemory;
    }

    public void setAllocatedMemory(Long allocatedMemory) {
        this.allocatedMemory = allocatedMemory;
    }

    public Integer getTotalCpuCores() {
        return totalCpuCores;
    }

    public void setTotalCpuCores(Integer totalCpuCores) {
        this.totalCpuCores = totalCpuCores;
    }

    public Long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(Long totalMemory) {
        this.totalMemory = totalMemory;
    }
}
