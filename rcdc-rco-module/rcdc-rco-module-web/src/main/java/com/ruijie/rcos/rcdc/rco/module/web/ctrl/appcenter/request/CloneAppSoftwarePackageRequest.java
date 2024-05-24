package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;


import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年6月21日
 *
 * @author chenl
 */
public class CloneAppSoftwarePackageRequest extends CreateAppSoftwarePackageRequest implements Serializable {
    @ApiModelProperty(value = "被克隆应用软件包ID", required = true)
    @NotNull
    private UUID id;

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

}