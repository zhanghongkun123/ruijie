package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7
 *
 * @author wjp
 */
public class LoginUserResponse {

    private int authCode;

    private String name;

    private UUID userGroupId;

    private IacUserStateEnum state;

    private IacUserTypeEnum userType;

    private UUID id;

    /**
     * 是否需要修改用户密码
     */
    private Boolean needUpdatePassword;

    /**
     * 锁定时长，单位分钟
     */
    private Integer pwdLockTime;

    /**
     * 剩余次数
     */
    private Integer remainingTimes;

    /**
     * 密码有效期天数
     */
    private Integer pwdSurplusDays;

    /**
     * 密码是否过期
     */
    private Boolean passwordExpired;

    /**
     * 密码复杂度变更
     */
    private Boolean passwordLevelChange;

    public LoginUserResponse(int authCode) {
        this.authCode = authCode;
    }

    public LoginUserResponse() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public IacUserStateEnum getState() {
        return state;
    }

    public void setState(IacUserStateEnum state) {
        this.state = state;
    }

    public int getAuthCode() {
        return authCode;
    }

    public void setAuthCode(int authCode) {
        this.authCode = authCode;
    }

    public Boolean getNeedUpdatePassword() {
        return needUpdatePassword;
    }

    public void setNeedUpdatePassword(Boolean needUpdatePassword) {
        this.needUpdatePassword = needUpdatePassword;
    }

    public Integer getPwdLockTime() {
        return pwdLockTime;
    }

    public void setPwdLockTime(Integer pwdLockTime) {
        this.pwdLockTime = pwdLockTime;
    }

    public Integer getRemainingTimes() {
        return remainingTimes;
    }

    public void setRemainingTimes(Integer remainingTimes) {
        this.remainingTimes = remainingTimes;
    }

    public Integer getPwdSurplusDays() {
        return pwdSurplusDays;
    }

    public void setPwdSurplusDays(Integer pwdSurplusDays) {
        this.pwdSurplusDays = pwdSurplusDays;
    }

    public Boolean getPasswordExpired() {
        return passwordExpired;
    }

    public void setPasswordExpired(Boolean passwordExpired) {
        this.passwordExpired = passwordExpired;
    }

    public Boolean getPasswordLevelChange() {
        return passwordLevelChange;
    }

    public void setPasswordLevelChange(Boolean passwordLevelChange) {
        this.passwordLevelChange = passwordLevelChange;
    }
}
