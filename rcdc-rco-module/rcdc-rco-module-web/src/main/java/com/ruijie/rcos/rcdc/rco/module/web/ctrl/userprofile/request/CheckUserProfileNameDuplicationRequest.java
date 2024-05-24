package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 检查用户配置名称是否重复请求对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public class CheckUserProfileNameDuplicationRequest {

    /**
     * 对象ID
     */
    @ApiModelProperty(value = "ID")
    @Nullable
    private UUID id;

    @ApiModelProperty(value = "名称，通用名称格式", required = true)
    @NotBlank
    @TextShort
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