package com.ruijie.rcos.rcdc.rco.module.def.api.response;

/**
 * Description: 快照容量最大支持范围
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/05/30
 *
 * @author lihengjing
 */
public class MaxSnapshotsRangeResponse {
    private Integer maxSnapshot;

    private Integer minSnapshot;

    public Integer getMaxSnapshot() {
        return maxSnapshot;
    }

    public void setMaxSnapshot(Integer maxSnapshot) {
        this.maxSnapshot = maxSnapshot;
    }

    public Integer getMinSnapshot() {
        return minSnapshot;
    }

    public void setMinSnapshot(Integer minSnapshot) {
        this.minSnapshot = minSnapshot;
    }


    @Override
    public String toString() {
        return "MaxSnapshotsRangeResponse{" +
                "maxSnapshot=" + maxSnapshot +
                ", minSnapshot=" + minSnapshot +
                '}';
    }
}
