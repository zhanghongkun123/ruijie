package com.ruijie.rcos.rcdc.rco.module.web.ctrl.adgroup.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacFilterType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-09-20
 *
 * @author zqj
 */
public class CbbDomainMatchRequest implements WebRequest {

    @Nullable
    @ApiModelProperty(value = "字段名")
    private String name;

    @Nullable
    @ApiModelProperty(value = "字段值")
    private String  value;

    @NotNull
    @ApiModelProperty(value = "筛选类型")
    private IacFilterType type;

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getValue() {
        return value;
    }

    public void setValue(@Nullable String value) {
        this.value = value;
    }

    public IacFilterType getType() {
        return type;
    }

    public void setType(IacFilterType type) {
        this.type = type;
    }
}
