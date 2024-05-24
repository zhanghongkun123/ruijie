package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import java.util.UUID;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/19 17:21
 *
 * @author yanlin
 */
public class CheckDesktopPoolNameWebRequest {

    @ApiModelProperty(value = "桌面池ID")
    @Nullable
    private UUID id;

    @ApiModelProperty(value = "桌面池名称")
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
