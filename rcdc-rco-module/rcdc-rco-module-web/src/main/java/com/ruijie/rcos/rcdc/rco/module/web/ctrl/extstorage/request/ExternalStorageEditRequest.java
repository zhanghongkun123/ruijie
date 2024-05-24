package com.ruijie.rcos.rcdc.rco.module.web.ctrl.extstorage.request;

import com.ruijie.rcos.sk.base.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 编辑外置存储请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/5
 *
 * @author TD
 */
public class ExternalStorageEditRequest {

    /**
     * 外置存储ID
     */
    @ApiModelProperty(value = "外置存储ID", required = true)
    @NotNull
    private UUID id;

    /**
     * 存储名称
     */
    @ApiModelProperty(value = "存储名称", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String name;

    /**
     * 存储描述
     */
    @ApiModelProperty(value = "存储描述")
    @Nullable
    @TextMedium
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
