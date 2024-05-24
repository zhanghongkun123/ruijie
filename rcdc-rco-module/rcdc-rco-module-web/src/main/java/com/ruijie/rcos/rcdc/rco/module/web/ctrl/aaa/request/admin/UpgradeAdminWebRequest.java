package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description 升级为管理员请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年08月02日
 *
 * @author linrenjian
 */
public class UpgradeAdminWebRequest implements WebRequest {


    @ApiModelProperty(value = "普通用户ID", required = true)
    @NotNull
    private UUID userId;

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

    @ApiModelProperty(value = "手机号", required = false)
    @TextShort
    @Nullable
    private String telephoneNum;

    @ApiModelProperty(value = "用户组权限", required = false)
    @Nullable
    private String[] userGroupArr;

    @ApiModelProperty(value = "终端组权限", required = false)
    @Nullable
    private String[] terminalGroupArr;


    @ApiModelProperty(value = "镜像限", required = false)
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

    @ApiModelProperty(value = "应用池权限组")
    @Nullable
    private UUID[] appPoolArr;

    @ApiModelProperty(value = "云应用策略权限组")
    @Nullable
    private UUID[] appMainStrategyArr;

    @ApiModelProperty(value = "云应用外设策略权限组")
    @Nullable
    private UUID[] appPeripheralStrategyArr;

    @ApiModelProperty(value = "状态", required = true)
    @NotNull
    private Boolean enabled;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }


    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    @Nullable
    public String getTelephoneNum() {
        return telephoneNum;
    }

    public void setTelephoneNum(@Nullable String telephoneNum) {
        this.telephoneNum = telephoneNum;
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    @Nullable
    public UUID[] getAppPoolArr() {
        return appPoolArr;
    }

    public void setAppPoolArr(@Nullable UUID[] appPoolArr) {
        this.appPoolArr = appPoolArr;
    }

    @Nullable
    public UUID[] getAppMainStrategyArr() {
        return appMainStrategyArr;
    }

    public void setAppMainStrategyArr(@Nullable UUID[] appMainStrategyArr) {
        this.appMainStrategyArr = appMainStrategyArr;
    }

    @Nullable
    public UUID[] getAppPeripheralStrategyArr() {
        return appPeripheralStrategyArr;
    }

    public void setAppPeripheralStrategyArr(@Nullable UUID[] appPeripheralStrategyArr) {
        this.appPeripheralStrategyArr = appPeripheralStrategyArr;
    }
}
