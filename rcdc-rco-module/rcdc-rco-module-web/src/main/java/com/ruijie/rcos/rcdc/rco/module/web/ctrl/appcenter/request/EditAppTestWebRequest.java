package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年01月03日
 *
 * @author zhk
 */
public class EditAppTestWebRequest implements WebRequest {

    @ApiModelProperty(value = "测试ID", required = true)
    @NotNull
    private UUID id;

    @TextShort
    @TextName
    @NotBlank
    @ApiModelProperty(value = "任务名称", required = true)
    private String name;

    @Nullable
    @TextMedium
    @ApiModelProperty(value = "任务描述")
    private String description;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }
}
