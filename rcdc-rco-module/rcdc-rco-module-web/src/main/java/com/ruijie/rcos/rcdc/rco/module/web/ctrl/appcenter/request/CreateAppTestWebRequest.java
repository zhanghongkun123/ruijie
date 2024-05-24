package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月28日
 *
 * @author zhk
 */
@ApiModel("创建测试")
public class CreateAppTestWebRequest implements WebRequest {

    @TextShort
    @TextName
    @NotBlank
    @ApiModelProperty(value = "任务名称", required = true)
    private String name;

    @Nullable
    @TextMedium
    @ApiModelProperty(value = "任务描述")
    private String description;

    @NotNull
    @ApiModelProperty(value = "应用ID列表", required = true)
    private UUID[] appIdArr;

    @NotNull
    @ApiModelProperty(value = "桌面ID列表", required = true)
    private UUID[] deskIdArr;

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

    public UUID[] getAppIdArr() {
        return appIdArr;
    }

    public void setAppIdArr(UUID[] appIdArr) {
        this.appIdArr = appIdArr;
    }

    public UUID[] getDeskIdArr() {
        return deskIdArr;
    }

    public void setDeskIdArr(UUID[] deskIdArr) {
        this.deskIdArr = deskIdArr;
    }
}
