package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.request;


import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;


/**
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月11日
 *
 * @author luojianmo
 */
public class UpdateMaxSnapshotsRequest {

    @ApiModelProperty(value = "云桌面快照数量最大限制数")
    @NotNull
    private Integer maxSnapshots;

    public UpdateMaxSnapshotsRequest(Integer maxSnapshots) {
        this.maxSnapshots = maxSnapshots;
    }

    public Integer getMaxSnapshots() {
        return maxSnapshots;
    }

    public void setMaxSnapshots(Integer maxSnapshots) {
        this.maxSnapshots = maxSnapshots;
    }
}
