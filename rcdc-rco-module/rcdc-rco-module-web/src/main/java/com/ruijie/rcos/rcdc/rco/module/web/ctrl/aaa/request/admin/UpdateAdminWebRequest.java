package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月21日
 *
 * @author zhuangchenwu
 */
public class UpdateAdminWebRequest implements WebRequest {

    @ApiModelProperty(value = "管理员ID", required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "管理员账号", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String userName;

    @ApiModelProperty(value = "姓名", required = true)
    @NotBlank
    @TextShort
    private String realName;

    @ApiModelProperty(value = "邮箱", required = false)
    @Email
    private String email;

    @ApiModelProperty(value = "描述", required = false)
    @TextMedium
    private String describe;

    @ApiModelProperty(value = "角色", required = true)
    @NotNull
    private UUID roleId;

    @ApiModelProperty(value = "用户组权限", required = false)
    @Nullable
    private String[] userGroupArr;

    @ApiModelProperty(value = "终端组权限", required = false)
    @Nullable
    private String[] terminalGroupArr;

    /**
     * 镜像组
     */
    @Nullable
    private UUID[] imageArr;

    @ApiModelProperty(value = "桌面池权限", required = false)
    @Nullable
    private UUID[] desktopPoolArr;

    @ApiModelProperty(value = "磁盘池权限", required = false)
    @Nullable
    private UUID[] diskPoolArr;

    @ApiModelProperty(value = "云桌面策略权限组", required = false)
    @Nullable
    private UUID[] deskStrategyArr;

    @ApiModelProperty(value = "状态", required = true)
    @NotNull
    private Boolean enabled;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String[] getUserGroupArr() {
        return userGroupArr;
    }

    public void setUserGroupArr(String[] userGroupArr) {
        this.userGroupArr = userGroupArr;
    }

    public String[] getTerminalGroupArr() {
        return terminalGroupArr;
    }

    public void setTerminalGroupArr(String[] terminalGroupArr) {
        this.terminalGroupArr = terminalGroupArr;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Nullable
    public UUID[] getImageArr() {
        return imageArr;
    }

    public void setImageArr(@Nullable UUID[] imageArr) {
        this.imageArr = imageArr;
    }

    @Nullable
    public UUID[] getDesktopPoolArr() {
        return desktopPoolArr;
    }

    public void setDesktopPoolArr(@Nullable UUID[] desktopPoolArr) {
        this.desktopPoolArr = desktopPoolArr;
    }

    @Nullable
    public UUID[] getDiskPoolArr() {
        return diskPoolArr;
    }

    public void setDiskPoolArr(@Nullable UUID[] diskPoolArr) {
        this.diskPoolArr = diskPoolArr;
    }

    @Nullable
    public UUID[] getDeskStrategyArr() {
        return deskStrategyArr;
    }

    public void setDeskStrategyArr(@Nullable UUID[] deskStrategyArr) {
        this.deskStrategyArr = deskStrategyArr;
    }
}
