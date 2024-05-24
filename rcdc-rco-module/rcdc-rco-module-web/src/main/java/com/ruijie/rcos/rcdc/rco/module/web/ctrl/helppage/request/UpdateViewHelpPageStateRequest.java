package com.ruijie.rcos.rcdc.rco.module.web.ctrl.helppage.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月04日
 *
 * @author luojianmo
 */
public class UpdateViewHelpPageStateRequest implements WebRequest {

    /**
     * 是否已经看过
     */
    @ApiModelProperty(value = "帮助状态：false：已经看过， true：未看过", required = true)
    @NotNull
    private Boolean  value;

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}
