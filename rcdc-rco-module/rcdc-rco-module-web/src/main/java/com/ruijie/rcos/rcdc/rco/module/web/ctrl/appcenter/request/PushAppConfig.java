package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.LocationTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月29日
 *
 * @author zhk
 */
@ApiModel("推送配置")
public class PushAppConfig implements WebRequest {

    @Nullable
    @ApiModelProperty(value = "应用包存放位置")
    @Size(max = 200)
    private String location;

    @NotNull
    @ApiModelProperty(value = "是否保存应用包", required = true)
    private Boolean enableSave;

    @ApiModelProperty(value = "保存路径类型", required = true)
    @NotNull
    private LocationTypeEnum locationType;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getEnableSave() {
        return enableSave;
    }

    public void setEnableSave(Boolean enableSave) {
        this.enableSave = enableSave;
    }

    public LocationTypeEnum getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationTypeEnum locationType) {
        this.locationType = locationType;
    }
}
