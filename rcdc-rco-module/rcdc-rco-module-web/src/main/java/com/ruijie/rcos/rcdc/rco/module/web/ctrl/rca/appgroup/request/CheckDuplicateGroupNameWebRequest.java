package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.request;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 分组重名校验
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月08日
 *
 * @author zhengjingyong
 */
public class CheckDuplicateGroupNameWebRequest implements WebRequest {

    @ApiModelProperty(value = "应用分组id",required = false)
    @Nullable
    private UUID id;

    @ApiModelProperty(value = "应用池id",required = true)
    @NotNull
    private UUID appPoolId;

    @NotBlank
    @TextShort
    @TextName
    @ApiModelProperty(value = "分组名称",required = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    public UUID getAppPoolId() {
        return appPoolId;
    }

    public void setAppPoolId(UUID appPoolId) {
        this.appPoolId = appPoolId;
    }
}
