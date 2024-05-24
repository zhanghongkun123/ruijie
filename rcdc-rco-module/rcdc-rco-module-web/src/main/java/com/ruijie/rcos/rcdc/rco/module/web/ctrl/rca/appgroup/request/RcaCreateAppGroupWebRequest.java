package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.request;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 创建融合应用池入参
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月26日
 *
 * @author zhengjingyong
 */
public class RcaCreateAppGroupWebRequest implements WebRequest {

    @ApiModelProperty(value = "应用分组名",required = true)
    @TextShort
    @TextName
    @NotBlank
    private String name;

    @ApiModelProperty(value = "分组描述信息")
    @TextMedium
    @Nullable
    private String description;

    @ApiModelProperty(value = "应用池id",required = true)
    @NotNull
    private UUID appPoolId;


    @ApiModelProperty(value = "应用id列表",required = true)
    @Nullable
    private List<UUID> appIdList;

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

    public UUID getAppPoolId() {
        return appPoolId;
    }

    public void setAppPoolId(UUID appPoolId) {
        this.appPoolId = appPoolId;
    }

    @Nullable
    public List<UUID> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(@Nullable List<UUID> appIdList) {
        this.appIdList = appIdList;
    }
}
