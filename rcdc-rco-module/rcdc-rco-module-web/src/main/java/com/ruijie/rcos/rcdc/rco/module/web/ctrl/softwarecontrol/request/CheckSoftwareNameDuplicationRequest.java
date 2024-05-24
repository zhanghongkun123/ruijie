package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.request;


import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextName;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
public class CheckSoftwareNameDuplicationRequest {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @Nullable
    private UUID id;

    @ApiModelProperty(value = "名称，通用名称格式", required = true)
    @NotBlank
    @Size(min = 1, max = 64)
    @TextName
    private String name;

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
