package com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月07日
 *
 * @author xiejian
 */
public class CreateAdminRequest implements Request, Serializable {

    @NotBlank
    @TextShort
    @TextName
    private String userName;

    @NotBlank
    @TextShort
    private String realName;

    @Email
    private String email;

    @TextMedium
    private String describe;


    @NotBlank
    private String pwd;

    @NotNull
    private UUID roleId;

    /**
     * 手机号
     */
    @TextShort
    @Nullable
    private String telephoneNum;

    /**
     * 用户组
     */
    @Nullable
    private String[] userGroupIdArr;

    /**
     * 终端组
     */
    @Nullable
    private String[] terminalGroupIdArr;

    /**
     * 镜像组
     */
    @Nullable
    private UUID[] imageArr;

    /**
     * 桌面池组
     */
    @Nullable
    private UUID[] desktopPoolArr;

    /**
     * 磁盘池组
     */
    @Nullable
    private UUID[] diskPoolArr;

    /**
     * 云桌面策略权限
     */
    @Nullable
    private UUID[] deskStrategyArr;

    /**
     * 应用池权限组
     */
    @Nullable
    private UUID[] appPoolArr;

    /**
     * 云应用策略权限组
     */
    @Nullable
    private UUID[] appMainStrategyArr;

    /**
     * 云应用外设策略权限组
     */
    @Nullable
    private UUID[] appPeripheralStrategyArr;

    @Nullable
    private String[] menuNameArr;

    @NotNull
    private Boolean enabled;

    @Nullable
    private Boolean hasFirstTimeLoggedIn;

    @Deprecated
    @Nullable
    private Boolean domainUser;

    @Nullable
    private String userType;

    @Nullable
    public Boolean getDomainUser() {
        return domainUser;
    }

    public void setDomainUser(@Nullable Boolean domainUser) {
        this.domainUser = domainUser;
    }

    public CreateAdminRequest() {
        this.hasFirstTimeLoggedIn = Boolean.TRUE;
    }

    @Nullable
    public Boolean getHasFirstTimeLoggedIn() {
        return this.hasFirstTimeLoggedIn;
    }

    public void setHasFirstTimeLoggedIn(@Nullable Boolean hasFirstTimeLoggedIn) {
        this.hasFirstTimeLoggedIn = hasFirstTimeLoggedIn;
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
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

    public String[] getUserGroupIdArr() {
        return userGroupIdArr;
    }

    public void setUserGroupIdArr(String[] userGroupIdArr) {
        this.userGroupIdArr = userGroupIdArr;
    }

    public String[] getTerminalGroupIdArr() {
        return terminalGroupIdArr;
    }

    public void setTerminalGroupIdArr(String[] terminalGroupIdArr) {
        this.terminalGroupIdArr = terminalGroupIdArr;
    }

    public String[] getMenuNameArr() {
        return menuNameArr;
    }

    public void setMenuNameArr(String[] menuNameArr) {
        this.menuNameArr = menuNameArr;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }


    public UUID[] getImageArr() {
        return imageArr;
    }

    public void setImageArr(UUID[] imageArr) {
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

    @Nullable
    public String getUserType() {
        return userType;
    }

    public void setUserType(@Nullable String userType) {
        this.userType = userType;
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
