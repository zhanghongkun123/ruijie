package com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 连通性检查响应对象
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/8
 *
 * @author WuShengQiang
 */
public class TestConnectWebResponse {

    @ApiModelProperty(value = "连通性检查， true：正常连通， false：不连通")
    private Boolean canConnect;

    public Boolean getCanConnect() {
        return canConnect;
    }

    public void setCanConnect(Boolean canConnect) {
        this.canConnect = canConnect;
    }
}
