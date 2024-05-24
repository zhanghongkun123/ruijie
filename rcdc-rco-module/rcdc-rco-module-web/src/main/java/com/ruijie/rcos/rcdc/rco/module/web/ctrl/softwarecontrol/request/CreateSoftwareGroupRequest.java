package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.request;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareGroupTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
public class CreateSoftwareGroupRequest implements WebRequest {

    /**
     * 软件分组ID
     */
    @ApiModelProperty(value = "软件分组ID")
    @Nullable
    private UUID id;

    /**
     * 快照名称
     */
    @ApiModelProperty(value = "软件分组名称，通用名称格式", required = true)
    @NotBlank
    @Size(min = 1, max = 64)
    @TextName
    private String name;

    /**
     * 软件分组类型
     */
    @ApiModelProperty(value = "软件分组类型", hidden = true)
    @Nullable
    private SoftwareGroupTypeEnum groupType = SoftwareGroupTypeEnum.CUSTOM;

    /**
     * 软件分组描述
     */
    @ApiModelProperty(value = "软件分组描述")
    @Nullable
    private String description;

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

    public SoftwareGroupTypeEnum getGroupType() {
        return groupType;
    }

    public void setGroupType(SoftwareGroupTypeEnum groupType) {
        this.groupType = groupType;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }
}
