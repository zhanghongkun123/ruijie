package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.poolapp.request;

import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 编辑融合应用入参
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月26日
 *
 * @author zhengjingyong
 */
public class EditRcaAppWebRequest implements WebRequest {


    @ApiModelProperty(value = "应用id",required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "应用名",required = true)
    @NotBlank
    @Size(max = 256)
    private String name;

    @ApiModelProperty(value = "池描述信息")
    @TextMedium
    @Nullable
    private String description;

    @ApiModelProperty(value = "连接参数")
    @Nullable
    private String args;

    @ApiModelProperty(value = "应用启动权限")
    @NotNull
    private RcaEnum.AppStartMode appStartMode;

    @ApiModelProperty(value = "是否启用")
    @NotNull
    private Boolean enabled;

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

    @Nullable
    public String getArgs() {
        return args;
    }

    public void setArgs(@Nullable String args) {
        this.args = args;
    }

    public RcaEnum.AppStartMode getAppStartMode() {
        return appStartMode;
    }

    public void setAppStartMode(RcaEnum.AppStartMode appStartMode) {
        this.appStartMode = appStartMode;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
