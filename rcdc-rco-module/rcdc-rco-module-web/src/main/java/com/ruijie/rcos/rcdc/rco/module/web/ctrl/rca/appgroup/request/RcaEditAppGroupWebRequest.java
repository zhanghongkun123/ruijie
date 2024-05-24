package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.request;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 编辑应用池分组信息
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月08日
 *
 * @author zhengjingyong
 */
public class RcaEditAppGroupWebRequest implements WebRequest {

    @ApiModelProperty(value = "应用分组id",required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "应用分组名",required = true)
    @TextShort
    @TextName
    @NotBlank
    private String name;

    @ApiModelProperty(value = "分组描述信息",required = true)
    @TextMedium
    @Nullable
    private String description;

    @ApiModelProperty(value = "应用id列表",required = false)
    @Nullable
    private List<UUID> appIdList;


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
    public List<UUID> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(@Nullable List<UUID> appIdList) {
        this.appIdList = appIdList;
    }
}

