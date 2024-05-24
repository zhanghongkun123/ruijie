package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
public class MoveSoftwareRequest extends IdArrWebRequest {

    /**
     * 软件名称
     */
    @ApiModelProperty(value = "源软件组", required = true)
    @NotNull
    private UUID sourceGroupId;

    /**
     * 软件分组ID
     */
    @ApiModelProperty(value = "目标软件组", required = true)
    @NotNull
    private UUID targetGroupId;

    public UUID getSourceGroupId() {
        return sourceGroupId;
    }

    public void setSourceGroupId(UUID sourceGroupId) {
        this.sourceGroupId = sourceGroupId;
    }

    public UUID getTargetGroupId() {
        return targetGroupId;
    }

    public void setTargetGroupId(UUID targetGroupId) {
        this.targetGroupId = targetGroupId;
    }
}
