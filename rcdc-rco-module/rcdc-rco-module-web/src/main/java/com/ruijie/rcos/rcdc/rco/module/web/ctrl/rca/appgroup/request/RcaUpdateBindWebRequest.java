package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.request;

import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaGroupExceptUserDTO;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 批量更新应用池分配关系请求体
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月24日
 *
 * @author zhengjingyong
 */
public class RcaUpdateBindWebRequest implements WebRequest {

    @ApiModelProperty(value = "是否全量更新用户")
    @Nullable
    private Boolean isFullUpdateUser = false;

    @ApiModelProperty(value = "新增用户列表")
    @Nullable
    private List<UUID> addUserByIdList;

    @ApiModelProperty(value = "删除用户列表")
    @Nullable
    private List<UUID> deleteUserByIdList;

    @ApiModelProperty(value = "需要删除用户组下所有用户的用户组列表")
    @Nullable
    private List<UUID> deleteGroupIdList;

    @ApiModelProperty(value = "新增用户组组下用户，除了exceptUserIdList列表中的用户")
    @Nullable
    private List<RcaGroupExceptUserDTO> addGroupIdListExceptUserList;

    @ApiModelProperty(value = "全量勾选的用户组列表")
    @Nullable
    private List<UUID> selectedGroupIdList;

    @ApiModelProperty(value = "全量勾选的的安全组列表")
    @Nullable
    private List<UUID> selectedAdGroupIdList;

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
    public List<UUID> getDeleteGroupIdList() {
        return deleteGroupIdList;
    }

    public void setDeleteGroupIdList(@Nullable List<UUID> deleteGroupIdList) {
        this.deleteGroupIdList = deleteGroupIdList;
    }

    @Nullable
    public List<RcaGroupExceptUserDTO> getAddGroupIdListExceptUserList() {
        return addGroupIdListExceptUserList;
    }

    public void setAddGroupIdListExceptUserList(@Nullable List<RcaGroupExceptUserDTO> addGroupIdListExceptUserList) {
        this.addGroupIdListExceptUserList = addGroupIdListExceptUserList;
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

    @Nullable
    public Boolean getIsFullUpdateUser() {
        return isFullUpdateUser;
    }

    public void setIsFullUpdateUser(@Nullable Boolean fullUpdateUser) {
        isFullUpdateUser = fullUpdateUser;
    }
}
