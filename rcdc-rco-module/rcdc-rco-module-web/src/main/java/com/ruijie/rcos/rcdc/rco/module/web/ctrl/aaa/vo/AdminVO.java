package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.vo;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUpdatePasswordType;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月13日
 *
 * @author zhuangchenwu
 */
public class AdminVO {

    private UUID id;

    @ApiModelProperty(value = "管理员账号")
    private String userName;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "描述")
    private String describe;

    @ApiModelProperty(value = "是否内置")
    private Boolean hasDefault;

    @ApiModelProperty(value = "是否超级管理员")
    private Boolean hasSuperPrivilege;

    @ApiModelProperty(value = "是否第一次登录")
    private Boolean hasFirstTimeLoggedIn;

    @ApiModelProperty(value = "角色数组")
    private IdLabelEntry[] roleArr;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private Boolean enabled;


    @ApiModelProperty(value = "菜单")
    private String[] menuNameArr;


    @ApiModelProperty(value = "功能")
    private String[] funNameArr;

    @ApiModelProperty(value = "用户组")
    private GroupIdLabelEntry[] userGroupArr;

    @ApiModelProperty(value = "终端组")
    private GroupIdLabelEntry[] terminalGroupArr;

    @ApiModelProperty(value = "镜像组")
    private GroupIdLabelEntry[] imageArr;

    @ApiModelProperty(value = "桌面池组")
    private GroupIdLabelEntry[] desktopPoolArr;

    @ApiModelProperty(value = "磁盘池组")
    private GroupIdLabelEntry[] diskPoolArr;

    @ApiModelProperty(value = "云桌面策略组")
    private GroupIdLabelEntry[] deskStrategyArr;

    @ApiModelProperty(value = "用户信息")
    private IacUserDetailDTO userDetailDTO;

    @ApiModelProperty(value = "应用池权限组")
    @Nullable
    private GroupIdLabelEntry[] appPoolArr;

    @ApiModelProperty(value = "云应用策略权限组")
    @Nullable
    private GroupIdLabelEntry[] appMainStrategyArr;

    @ApiModelProperty(value = "云应用外设策略权限组")
    @Nullable
    private GroupIdLabelEntry[] appPeripheralStrategyArr;

    /**
     * 三个内置管理员是否第一次启用的属性。其他管理员为null；
     */
    private Boolean firstEnable;

    /**
     * 是否被锁定
     */
    private Boolean lock;

    /**
     * 是否开启评测功能
     */
    private Boolean enableEvaluation;

    /**
     * 管理员密码是否为弱密码
     */
    private Boolean weakAdminPassword;

    /**
     * 终端管理密码是否为弱密码
     */
    private Boolean weakTerminalPassword;

    @ApiModelProperty("是否开启系统盘自动扩容")
    private Boolean enableFullSystemDisk;

    @ApiModelProperty("是否开启终端极简部署")
    private Boolean enableTerminalSimplifyDeployment;

    private String token;

    @Nullable
    private Boolean needUpdatePassword;

    @Nullable
    private IacUpdatePasswordType updatePasswordType;

    @Nullable
    private Integer passwordRemindTimes;

    @Nullable
    private MfaAuthVO mfa;

    @Nullable
    public IacUpdatePasswordType getUpdatePasswordType() {
        return updatePasswordType;
    }

    public void setUpdatePasswordType(@Nullable IacUpdatePasswordType updatePasswordType) {
        this.updatePasswordType = updatePasswordType;
    }

    @Nullable
    public Boolean getNeedUpdatePassword() {
        return needUpdatePassword;
    }

    public void setNeedUpdatePassword(@Nullable Boolean needUpdatePassword) {
        this.needUpdatePassword = needUpdatePassword;
    }

    @Nullable
    public Integer getPasswordRemindTimes() {
        return passwordRemindTimes;
    }

    public void setPasswordRemindTimes(@Nullable Integer passwordRemindTimes) {
        this.passwordRemindTimes = passwordRemindTimes;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public Boolean getHasDefault() {
        return hasDefault;
    }

    public void setHasDefault(Boolean hasDefault) {
        this.hasDefault = hasDefault;
    }

    public Boolean getHasSuperPrivilege() {
        return hasSuperPrivilege;
    }

    public void setHasSuperPrivilege(Boolean hasSuperPrivilege) {
        this.hasSuperPrivilege = hasSuperPrivilege;
    }

    public Boolean getHasFirstTimeLoggedIn() {
        return hasFirstTimeLoggedIn;
    }

    public void setHasFirstTimeLoggedIn(Boolean hasFirstTimeLoggedIn) {
        this.hasFirstTimeLoggedIn = hasFirstTimeLoggedIn;
    }

    public IdLabelEntry[] getRoleArr() {
        return roleArr;
    }

    public void setRoleArr(IdLabelEntry[] roleArr) {
        this.roleArr = roleArr;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public GroupIdLabelEntry[] getUserGroupArr() {
        return userGroupArr;
    }

    public void setUserGroupArr(GroupIdLabelEntry[] userGroupArr) {
        this.userGroupArr = userGroupArr;
    }

    public GroupIdLabelEntry[] getTerminalGroupArr() {
        return terminalGroupArr;
    }

    public void setTerminalGroupArr(GroupIdLabelEntry[] terminalGroupArr) {
        this.terminalGroupArr = terminalGroupArr;
    }

    public Boolean getFirstEnable() {
        return firstEnable;
    }

    public void setFirstEnable(Boolean firstEnable) {
        this.firstEnable = firstEnable;
    }

    public String[] getFunNameArr() {
        return funNameArr;
    }

    public void setFunNameArr(String[] funNameArr) {
        this.funNameArr = funNameArr;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public GroupIdLabelEntry[] getImageArr() {
        return imageArr;
    }

    public void setImageArr(GroupIdLabelEntry[] imageArr) {
        this.imageArr = imageArr;
    }

    public GroupIdLabelEntry[] getDesktopPoolArr() {
        return desktopPoolArr;
    }

    public void setDesktopPoolArr(GroupIdLabelEntry[] desktopPoolArr) {
        this.desktopPoolArr = desktopPoolArr;
    }

    public GroupIdLabelEntry[] getDiskPoolArr() {
        return diskPoolArr;
    }

    public void setDiskPoolArr(GroupIdLabelEntry[] diskPoolArr) {
        this.diskPoolArr = diskPoolArr;
    }

    public GroupIdLabelEntry[] getDeskStrategyArr() {
        return deskStrategyArr;
    }

    public void setDeskStrategyArr(GroupIdLabelEntry[] deskStrategyArr) {
        this.deskStrategyArr = deskStrategyArr;
    }

    public Boolean getEnableEvaluation() {
        return enableEvaluation;
    }

    public void setEnableEvaluation(Boolean enableEvaluation) {
        this.enableEvaluation = enableEvaluation;
    }

    public Boolean getWeakAdminPassword() {
        return weakAdminPassword;
    }

    public void setWeakAdminPassword(Boolean weakAdminPassword) {
        this.weakAdminPassword = weakAdminPassword;
    }

    public Boolean getWeakTerminalPassword() {
        return weakTerminalPassword;
    }

    public void setWeakTerminalPassword(Boolean weakTerminalPassword) {
        this.weakTerminalPassword = weakTerminalPassword;
    }

    public IacUserDetailDTO getUserDetailDTO() {
        return userDetailDTO;
    }

    public void setUserDetailDTO(IacUserDetailDTO userDetailDTO) {
        this.userDetailDTO = userDetailDTO;
    }

    public Boolean getEnableFullSystemDisk() {
        return enableFullSystemDisk;
    }

    public void setEnableFullSystemDisk(Boolean enableFullSystemDisk) {
        this.enableFullSystemDisk = enableFullSystemDisk;
    }

    public Boolean getEnableTerminalSimplifyDeployment() {
        return enableTerminalSimplifyDeployment;
    }

    public void setEnableTerminalSimplifyDeployment(Boolean enableTerminalSimplifyDeployment) {
        this.enableTerminalSimplifyDeployment = enableTerminalSimplifyDeployment;
    }

    @Nullable
    public MfaAuthVO getMfa() {
        return mfa;
    }

    public void setMfa(@Nullable MfaAuthVO mfa) {
        this.mfa = mfa;
    }

    @Nullable
    public GroupIdLabelEntry[] getAppPoolArr() {
        return appPoolArr;
    }

    public void setAppPoolArr(@Nullable GroupIdLabelEntry[] appPoolArr) {
        this.appPoolArr = appPoolArr;
    }

    @Nullable
    public GroupIdLabelEntry[] getAppMainStrategyArr() {
        return appMainStrategyArr;
    }

    public void setAppMainStrategyArr(@Nullable GroupIdLabelEntry[] appMainStrategyArr) {
        this.appMainStrategyArr = appMainStrategyArr;
    }

    @Nullable
    public GroupIdLabelEntry[] getAppPeripheralStrategyArr() {
        return appPeripheralStrategyArr;
    }

    public void setAppPeripheralStrategyArr(@Nullable GroupIdLabelEntry[] appPeripheralStrategyArr) {
        this.appPeripheralStrategyArr = appPeripheralStrategyArr;
    }
}
