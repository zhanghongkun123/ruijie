package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 桌面关联用户请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/9/6
 *
 * @author linke
 */
public class DesktopBindUserWebRequest implements WebRequest {

    @ApiModelProperty(value = "云桌面ID", required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "用户ID数组")
    @NotNull
    private UUID[] userIdArr;

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
}
