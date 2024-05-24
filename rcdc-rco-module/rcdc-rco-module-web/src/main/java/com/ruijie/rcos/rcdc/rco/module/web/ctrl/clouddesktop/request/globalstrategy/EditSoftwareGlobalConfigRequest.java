package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 编辑软件策略请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/27 9:39
 *
 * @author chenl
 */
@ApiModel("编辑软件策略请求")
public class EditSoftwareGlobalConfigRequest implements WebRequest {

    @ApiModelProperty("是否开启软件策略")
    @NotNull
    private Boolean value;


    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}
