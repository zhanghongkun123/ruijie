package com.ruijie.rcos.rcdc.rco.module.web.ctrl.extstorage.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/19 17:21
 *
 * @author yanlin
 */
public class CheckExternalStorageNameWebRequest {

    @ApiModelProperty(value = "外置存储ID")
    @Nullable
    private UUID id;

    @ApiModelProperty(value = "外置存储名称")
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
