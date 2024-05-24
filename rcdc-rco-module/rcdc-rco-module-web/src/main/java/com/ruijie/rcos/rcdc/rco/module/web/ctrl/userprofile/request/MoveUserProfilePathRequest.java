package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 移动用户配置路径
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public class MoveUserProfilePathRequest extends IdArrWebRequest {

    /**
     * 源路径组ID
     */
    @ApiModelProperty(value = "源路径组", required = true)
    @NotNull
    private UUID sourceGroupId;

    /**
     * 目标路径组ID
     */
    @ApiModelProperty(value = "目标路径组", required = true)
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
