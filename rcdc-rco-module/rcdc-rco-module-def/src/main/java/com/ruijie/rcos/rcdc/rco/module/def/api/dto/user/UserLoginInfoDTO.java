package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;

/**
 * Description: 用户登录信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-14 16:53:00
 *
 * @author zjy
 */
public class UserLoginInfoDTO extends UserInfoDTO {

    private Integer businessCode;

    /**
     * 是否需要更新密码
     **/
    private Boolean needUpdatePassword;

    /**
     * 密码锁定时间
     **/
    private Integer pwdLockTime;

    /**
     * 剩余错误次数
     **/
    private Integer remainingTimes;

    /**
     * 密码过期时间
     **/
    private Integer pwdSurplusDays;

    /**
     * 密码是否过期
     **/
    private Boolean passwordExpired;

    /**
     * 密码等级是否变更
     **/
    private Boolean passwordLevelChange;

    /**
     * 是否开启统一登录
     **/
    private Boolean hasUnifiedLogin = false;


    /**
     * 是否首次登录强制修改密码
     */
    private Boolean forceUpdatePassword;

    /**
     * 是否重置密码后强制修改密码
     */
    private Boolean updatePasswordByReset;

    /**
     * 错误信息
     **/
    private String errorMsg;

    /**
     * 是否开启防暴力破解
     */
    private Boolean preventsBruteForce;

    /**
     * 是否弱密码
     */
    private Boolean isWeakPassword;

    public Boolean getIsWeakPassword() {
        return isWeakPassword;
    }

    public void setIsWeakPassword(Boolean isWeakPassword) {
        this.isWeakPassword = isWeakPassword;
    }

    public Integer getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(Integer businessCode) {
        this.businessCode = businessCode;
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

    public Boolean getHasUnifiedLogin() {
        return hasUnifiedLogin;
    }

    public void setHasUnifiedLogin(Boolean hasUnifiedLogin) {
        this.hasUnifiedLogin = hasUnifiedLogin;
    }

    public Boolean getForceUpdatePassword() {
        return forceUpdatePassword;
    }

    public void setForceUpdatePassword(Boolean forceUpdatePassword) {
        this.forceUpdatePassword = forceUpdatePassword;
    }

    public Boolean getUpdatePasswordByReset() {
        return updatePasswordByReset;
    }

    public void setUpdatePasswordByReset(Boolean updatePasswordByReset) {
        this.updatePasswordByReset = updatePasswordByReset;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Boolean getPreventsBruteForce() {
        return preventsBruteForce;
    }

    public void setPreventsBruteForce(Boolean preventsBruteForce) {
        this.preventsBruteForce = preventsBruteForce;
    }
}
