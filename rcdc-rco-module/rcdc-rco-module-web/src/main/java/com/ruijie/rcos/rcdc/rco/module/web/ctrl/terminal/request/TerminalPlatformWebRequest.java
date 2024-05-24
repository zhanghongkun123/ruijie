package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/10
 *
 * @author nt
 */
public class TerminalPlatformWebRequest implements WebRequest {

    @ApiModelProperty(value = "终端类型数组", required = true)
    @NotNull
    @NotEmpty
    private CbbTerminalPlatformEnums[] platformArr;

    public CbbTerminalPlatformEnums[] getPlatformArr() {
        return platformArr;
    }

    public void setPlatformArr(CbbTerminalPlatformEnums[] platformArr) {
        this.platformArr = platformArr;
    }
}
