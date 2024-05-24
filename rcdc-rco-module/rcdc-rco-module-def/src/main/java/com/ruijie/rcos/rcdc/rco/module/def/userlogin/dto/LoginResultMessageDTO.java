package com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto;

import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import java.util.Date;

/**
 * Description: 用户登录返回结果信息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/03/09
 *
 * @author linke
 */
public class LoginResultMessageDTO extends EqualsHashcodeSupport {

    /**
     * 用户锁定时长
     */
    private Integer pwdLockTime;

    /**
     * 用户密码错误次数
     */
    private Integer errorTimes;

    /**
     * 密码有效期天数
     */
    private Integer pwdSurplusDays;

    /**
     * 密码是否过期
     */
    private Boolean isPasswordExpired;

    /**
     * 密码复杂度变更
     */
    private Boolean isPasswordLevelChange;

    /**
     * 拥有硬件特征码最大数量
     */
    private Integer hardwareCodeMaxNum;

    /**
     * 用户锁定时间
     */
    private Date lockTime;

    /**
     * 允许的错误次数
     */
    private Integer userLockedErrorsTimes;

    /**
     * 是否开启防爆功能
     */
    private Boolean preventsBruteForce;


    /**
     * 是否首次登录强制修改密码
     */
    private Boolean forceUpdatePassword;

    /**
     * 是否重置密码后强制修改密码
     */
    private Boolean updatePasswordByReset;


    /**
     * 是否弱密码
     */
    private Boolean isWeakPassword;

    /**
     * 错误信息，仅第三方认证失败是有效
     */
    private String errorMsg;

    public LoginResultMessageDTO() {

    }

    public LoginResultMessageDTO(Integer pwdLockTime, Integer errorTimes) {
        this.pwdLockTime = pwdLockTime;
        this.errorTimes = errorTimes;
    }

    public Integer getPwdLockTime() {
        return pwdLockTime;
    }

    public void setPwdLockTime(Integer pwdLockTime) {
        this.pwdLockTime = pwdLockTime;
    }

    public Integer getErrorTimes() {
        return errorTimes;
    }

    public void setErrorTimes(Integer errorTimes) {
        this.errorTimes = errorTimes;
    }

    public Integer getPwdSurplusDays() {
        return pwdSurplusDays;
    }

    public void setPwdSurplusDays(Integer pwdSurplusDays) {
        this.pwdSurplusDays = pwdSurplusDays;
    }

    public Boolean getIsPasswordExpired() {
        return isPasswordExpired;
    }

    public void setIsPasswordExpired(Boolean isPasswordExpired) {
        this.isPasswordExpired = isPasswordExpired;
    }

    public Boolean getIsPasswordLevelChange() {
        return isPasswordLevelChange;
    }

    public void setIsPasswordLevelChange(Boolean isPasswordLevelChange) {
        this.isPasswordLevelChange = isPasswordLevelChange;
    }

    public Integer getHardwareCodeMaxNum() {
        return hardwareCodeMaxNum;
    }

    public void setHardwareCodeMaxNum(Integer hardwareCodeMaxNum) {
        this.hardwareCodeMaxNum = hardwareCodeMaxNum;
    }

    public Integer getUserLockedErrorsTimes() {
        return userLockedErrorsTimes;
    }

    public void setUserLockedErrorsTimes(Integer userLockedErrorsTimes) {
        this.userLockedErrorsTimes = userLockedErrorsTimes;
    }

    public Boolean getPreventsBruteForce() {
        return preventsBruteForce;
    }

    public void setPreventsBruteForce(Boolean preventsBruteForce) {
        this.preventsBruteForce = preventsBruteForce;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
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

    public Boolean getIsWeakPassword() {
        return isWeakPassword;
    }

    public void setIsWeakPassword(Boolean isWeakPassword) {
        this.isWeakPassword = isWeakPassword;
    }
}
