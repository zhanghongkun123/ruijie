package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 桌面池查询关联用户或用户组信息参数
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 14:39
 *
 * @author linke
 */
public class DesktopPoolUserListRequest {

    @ApiModelProperty(value = "桌面池ID", required = true)
    @NotNull
    private UUID desktopPoolId;

    @ApiModelProperty(value = "类型：USER/USERGROUP")
    @Nullable
    private IacConfigRelatedType relatedType;

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    @Nullable
    public IacConfigRelatedType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(@Nullable IacConfigRelatedType relatedType) {
        this.relatedType = relatedType;
    }
}
