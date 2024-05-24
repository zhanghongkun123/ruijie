package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 更新桌面池用户或用户组关联关系
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 14:39
 *
 * @author linke
 */
public class UpdatePoolUserOrGroupWebRequest {

    /**
     * 桌面池Id
     */
    @ApiModelProperty(value = "桌面池Id", required = true)
    @NotNull
    private UUID id;

    /**
     * 指定用户/用户组
     */
    @ApiModelProperty(value = "指定用户/用户组", required = true)
    @NotNull
    private UUID[] userIdArr;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型：USER用户，USERGROUP用户组", required = true)
    @NotNull
    private IacConfigRelatedType relatedType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID[] getUserIdArr() {
        return userIdArr;
    }

    public void setUserIdArr(UUID[] userIdArr) {
        this.userIdArr = userIdArr;
    }

    public IacConfigRelatedType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(IacConfigRelatedType relatedType) {
        this.relatedType = relatedType;
    }
}
