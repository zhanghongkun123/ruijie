package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccp.response;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.storagepool.StoragePoolClustersSummaryResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.storagepool.StoragePoolListInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.AbstractOpenAPIResponse;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.UUID;

/**
 * Description: rccp存储集群计算统计响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/25 14:57
 *
 * @author lyb
 */
public class StorageClustersSummaryResponse extends AbstractOpenAPIResponse<PageResponse<StoragePoolClustersSummaryResponse>> {

    /**
     * 总数
     */
    public Long total = 0L;

    public StorageClustersSummaryItem[] itemArr;

    @Override
    public void dtoToResponse(PageResponse<StoragePoolClustersSummaryResponse> response) {
        Assert.notNull(response, "response must not be null.");

        setTotal(response.getTotal());
        if (response.getItems() != null) {
            setItemArr(Arrays.stream(response.getItems()).map(StorageClustersSummaryItem::new).toArray(StorageClustersSummaryItem[]::new));
        }
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public StorageClustersSummaryItem[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(StorageClustersSummaryItem[] itemArr) {
        this.itemArr = itemArr;
    }
}


/**
 * Description: rccp存储集群计算统计响应条目
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/25 14:57
 *
 * @author lyb
 */
class StorageClustersSummaryItem {

    StorageClustersSummaryItem(StoragePoolClustersSummaryResponse dto) {
        setClusterId(dto.getClusterId());
        setClusterName(dto.getName());
        if (dto.getPoolArr() != null) {
            setStoragePoolArr(Arrays.stream(dto.getPoolArr()).map(StoragePoolItem::new).toArray(StoragePoolItem[]::new));
        }
    }

    /**
     * 存储集群id
     */
    public UUID clusterId;

    /**
     * 存储集群名称
     */
    public String clusterName;

    public StoragePoolItem[] storagePoolArr;

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

    public StoragePoolItem[] getStoragePoolArr() {
        return storagePoolArr;
    }

    public void setStoragePoolArr(StoragePoolItem[] storagePoolArr) {
        this.storagePoolArr = storagePoolArr;
    }
}


/**
 * Description: rccp存储集群计算统计存储池
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/25 14:57
 *
 * @author lyb
 */
class StoragePoolItem {
    /**
     * 存储池id
     */
    public UUID poolId;

    /**
     * 总容量
     */
    public Long totalCapacity;

    /**
     * 已使用容量
     */
    public Long usedCapacity;

    StoragePoolItem(StoragePoolListInfoDTO dto) {
        BeanUtils.copyProperties(dto, this);
        this.poolId = dto.getId();
    }

    public UUID getPoolId() {
        return poolId;
    }

    public void setPoolId(UUID poolId) {
        this.poolId = poolId;
    }

    public Long getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Long totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Long getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(Long usedCapacity) {
        this.usedCapacity = usedCapacity;
    }
}
