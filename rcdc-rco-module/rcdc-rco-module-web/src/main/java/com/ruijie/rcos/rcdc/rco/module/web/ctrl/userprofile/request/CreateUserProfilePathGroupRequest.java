package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 创建用户配置路径组
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public class CreateUserProfilePathGroupRequest implements WebRequest {

    /**
     * 用户配置路径分组ID
     */
    @ApiModelProperty(value = "用户配置路径分组ID")
    @Nullable
    private UUID id;

    /**
     * 组名称
     */
    @ApiModelProperty(value = "用户配置路径分组名称，通用名称格式", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String name;

    /**
     * 用户配置路径分组描述
     */
    @ApiModelProperty(value = "用户配置路径分组描述")
    @TextMedium
    @Nullable
    private String description;

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

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }
}