package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.response;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileChildPathDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathModeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathTypeEnum;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.ApiModel;

import java.util.UUID;

/**
 * Description: 用户配置路径详情响应对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/13
 *
 * @author WuShengQiang
 */
@ApiModel("用户配置路径详情响应对象")
public class UserProfilePathWebResponse extends DefaultWebResponse {

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
     * 路径组对象
     */
    private IdLabelEntry pathGroup;

    /**
     * 路径名称
     */
    private String name;

    /**
     * 子路径对象
     */
    private UserProfileChildPathDTO[] childPathArr;

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

    public IdLabelEntry getPathGroup() {
        return pathGroup;
    }

    public void setPathGroup(IdLabelEntry pathGroup) {
        this.pathGroup = pathGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserProfileChildPathDTO[] getChildPathArr() {
        return childPathArr;
    }

    public void setChildPathArr(UserProfileChildPathDTO[] childPathArr) {
        this.childPathArr = childPathArr;
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
}