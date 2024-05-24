package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.IdArrWebRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 云桌面开机启动请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年01月31日
 *
 * @author xwx
 */
public class StartDesktopWebRequest extends IdArrWebRequest {

    @NotNull
    @ApiModelProperty(value = "是否挂载旧数据盘")
    private Boolean enableMountOldData = false;

    public Boolean getEnableMountOldData() {
        return enableMountOldData;
    }

    public void setEnableMountOldData(Boolean enableMountOldData) {
        this.enableMountOldData = enableMountOldData;
    }
}
