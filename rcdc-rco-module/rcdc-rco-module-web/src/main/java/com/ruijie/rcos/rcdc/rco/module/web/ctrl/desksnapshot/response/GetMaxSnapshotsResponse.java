package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.response;


import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月11日
 *
 * @author luojianmo
 */
public class GetMaxSnapshotsResponse {

    @ApiModelProperty(value = "云桌面快照数量最大限制数")
    private Integer maxSnapshots;

    @ApiModelProperty(value = "云桌面快照数量配置范围最小限制数")
    private Integer allowMinSnapshotsConfig;

    @ApiModelProperty(value = "云桌面快照数量配置范围最大限制数")
    private Integer allowMaxSnapshotsConfig;

    public GetMaxSnapshotsResponse(Integer maxSnapshots, Integer allowMinSnapshotsConfig, Integer allowMaxSnapshotsConfig) {
        this.maxSnapshots = maxSnapshots;
        this.allowMinSnapshotsConfig = allowMinSnapshotsConfig;
        this.allowMaxSnapshotsConfig = allowMaxSnapshotsConfig;
    }

    public Integer getMaxSnapshots() {
        return maxSnapshots;
    }

    public void setMaxSnapshots(Integer maxSnapshots) {
        this.maxSnapshots = maxSnapshots;
    }

    public Integer getAllowMinSnapshotsConfig() {
        return allowMinSnapshotsConfig;
    }

    public void setAllowMinSnapshotsConfig(Integer allowMinSnapshotsConfig) {
        this.allowMinSnapshotsConfig = allowMinSnapshotsConfig;
    }

    public Integer getAllowMaxSnapshotsConfig() {
        return allowMaxSnapshotsConfig;
    }

    public void setAllowMaxSnapshotsConfig(Integer allowMaxSnapshotsConfig) {
        this.allowMaxSnapshotsConfig = allowMaxSnapshotsConfig;
    }
}
