package com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathModeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 路径详细信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/21
 *
 * @author zwf
 */
public class UserProfilePathDetailDTO {
    /**
     * 路径ID
     */
    private UUID id;

    /**
     * 组ID
     */
    private UUID groupId;

    /**
     * 组名称
     */
    private String groupName;

    /**
     * 路径名称
     */
    private String name;

    /**
     * 小写路径+后缀\
     */
    private String pathLowerCase;

    /**
     * 描述
     **/
    private String description;

    /**
     * 创建人
     */
    private String creatorUserName;

    /**
     * 是否系统自带
     */
    private Boolean isDefault;

    /**
     * 配置方式(同步/排除)
     */
    @NotNull
    private UserProfilePathModeEnum mode;

    /**
     * 类型(文件夹/文件/注册表)
     */
    @NotNull
    private UserProfilePathTypeEnum type;

    /**
     * 路径列表
     */
    @NotBlank
    private String path;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getPathLowerCase() {
        return pathLowerCase;
    }

    public void setPathLowerCase(String pathLowerCase) {
        this.pathLowerCase = pathLowerCase;
    }

    public UserProfilePathModeEnum getMode() {
        return mode;
    }

    public void setMode(UserProfilePathModeEnum mode) {
        this.mode = mode;
    }

    public String getModeName() {
        return this.mode.name();
    }

    public UserProfilePathTypeEnum getType() {
        return type;
    }

    public void setType(UserProfilePathTypeEnum type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 自定义对比器
     *
     * @param another 路径对象
     * @return 比值
     */
    public int compareTo(UserProfilePathDetailDTO another) {
        Assert.notNull(another, "another cannot null");
        return this.path.toLowerCase().compareTo(another.path.toLowerCase());
    }
}
