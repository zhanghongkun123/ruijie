package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileChildPathDTO;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 创建路径请求对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public class CreateUserProfilePathRequest implements WebRequest {

    /**
     * 路径ID
     */
    @ApiModelProperty(value = "路径ID")
    @Nullable
    private UUID id;

    /**
     * 路径名称
     */
    @ApiModelProperty(value = "路径名称，通用名称格式", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String name;

    /**
     * 路径分组ID
     */
    @ApiModelProperty(value = "路径分组ID", required = true)
    @NotNull
    private UUID groupId;

    /**
     * 路径描述
     */
    @ApiModelProperty(value = "路径描述")
    @TextMedium
    @Nullable
    private String description;

    @ApiModelProperty(value = "子路径信息")
    @NotNull
    private UserProfileChildPathDTO[] childPathArr;

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

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public UserProfileChildPathDTO[] getChildPathArr() {
        return childPathArr;
    }

    public void setChildPathArr(UserProfileChildPathDTO[] childPathArr) {
        this.childPathArr = childPathArr;
    }
}
