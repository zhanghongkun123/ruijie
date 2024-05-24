package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.GroupExceptUserDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 更新桌面池用户或用户组关联关系
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 14:39
 *
 * @author linke
 */
public class UpdatePoolBindObjectWebRequest {

    @ApiModelProperty(value = "桌面池Id", required = true)
    @NotNull
    private UUID desktopPoolId;

    @ApiModelProperty(value = "新增用户列表")
    @Nullable
    private List<UUID> addUserByIdList;

    @ApiModelProperty(value = "删除用户列表")
    @Nullable
    private List<UUID> deleteUserByIdList;

    @ApiModelProperty(value = "需要删除用户组下所有用户的用户组列表")
    @Nullable
    private List<UUID> deleteUserByGroupIdList;

    @ApiModelProperty(value = "新增用户组组下用户，除了exceptUserIdList列表中的用户")
    @Nullable
    private List<GroupExceptUserDTO> exceptList;

    @ApiModelProperty(value = "全量桌面池分配的用户组列表")
    @Nullable
    private List<UUID> selectedGroupIdList;

    @ApiModelProperty(value = "全量桌面池分配的安全组列表")
    @Nullable
    private List<UUID> selectedAdGroupIdList;

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    @Nullable
    public List<UUID> getAddUserByIdList() {
        return addUserByIdList;
    }

    public void setAddUserByIdList(@Nullable List<UUID> addUserByIdList) {
        this.addUserByIdList = addUserByIdList;
    }

    @Nullable
    public List<UUID> getDeleteUserByIdList() {
        return deleteUserByIdList;
    }

    public void setDeleteUserByIdList(@Nullable List<UUID> deleteUserByIdList) {
        this.deleteUserByIdList = deleteUserByIdList;
    }

    @Nullable
    public List<UUID> getDeleteUserByGroupIdList() {
        return deleteUserByGroupIdList;
    }

    public void setDeleteUserByGroupIdList(@Nullable List<UUID> deleteUserByGroupIdList) {
        this.deleteUserByGroupIdList = deleteUserByGroupIdList;
    }

    @Nullable
    public List<GroupExceptUserDTO> getExceptList() {
        return exceptList;
    }

    public void setExceptList(@Nullable List<GroupExceptUserDTO> exceptList) {
        this.exceptList = exceptList;
    }

    @Nullable
    public List<UUID> getSelectedGroupIdList() {
        return selectedGroupIdList;
    }

    public void setSelectedGroupIdList(@Nullable List<UUID> selectedGroupIdList) {
        this.selectedGroupIdList = selectedGroupIdList;
    }

    @Nullable
    public List<UUID> getSelectedAdGroupIdList() {
        return selectedAdGroupIdList;
    }

    public void setSelectedAdGroupIdList(@Nullable List<UUID> selectedAdGroupIdList) {
        this.selectedAdGroupIdList = selectedAdGroupIdList;
    }
}
