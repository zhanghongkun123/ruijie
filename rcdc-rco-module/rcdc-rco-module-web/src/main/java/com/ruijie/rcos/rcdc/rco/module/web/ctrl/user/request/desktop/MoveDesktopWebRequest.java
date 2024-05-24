package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 移动桌面请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/9/6
 *
 * @author linke
 */
public class MoveDesktopWebRequest {

    @ApiModelProperty(value = "云桌面ID数组", required = true)
    @NotEmpty
    @Size(min = 1)
    private UUID[] idArr;

    @ApiModelProperty(value = "目标桌面池ID")
    @NotNull
    private UUID desktopPoolId;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }
}
