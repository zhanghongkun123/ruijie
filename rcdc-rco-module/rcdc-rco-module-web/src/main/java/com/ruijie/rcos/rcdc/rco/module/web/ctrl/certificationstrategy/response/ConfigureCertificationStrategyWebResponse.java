package com.ruijie.rcos.rcdc.rco.module.web.ctrl.certificationstrategy.response;

/**
 *
 * Description: 配置认证策略详情响应
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月01日
 *
 * @author zhang.zhiwen
 */
public class ConfigureCertificationStrategyWebResponse {

    /**
     * 是否启用密码安全策略：0不启用，1启用
     */
    private Boolean securityStrategyEnable;

    /**
     * 开启密码有效期（0否1是）
     */
    private String isOpenUpdateDays;

    /**
     * 每隔（）天用户必须修改密码
     */
    private Integer updateDays;

    /**
     * 密码剩余天数提醒
     */
    private Integer pwdExpireRemindDays;

    /**
     * 密码复杂度等级
     */
    private String pwdLevel;

    /**
     * 首次登录强制修改密码(无论是否符合密码规则)
     */
    private Boolean enableForceUpdatePassword;

    /**
     * 是否启用防暴力破解：0不启用，1启用
     */
    private Boolean preventsBruteForce;

    /**
     * 最大错误次数，超过锁定用户
     */
    private Integer userLockedErrorTimes;

    /**
     * 开启锁定时间（0否1是）
     */
    private String isOpenUserLockTime;

    /**
     * 锁定时间，单位小时
     */
    private Integer userLockTime;

    /**
     * 自动登录
     */
    private Boolean autoLogon;

    private Boolean enablePasswordBlacklist;

    public Boolean getEnablePasswordBlacklist() {
        return enablePasswordBlacklist;
    }

    public void setEnablePasswordBlacklist(Boolean enablePasswordBlacklist) {
        this.enablePasswordBlacklist = enablePasswordBlacklist;
    }

    public Boolean getAutoLogon() {
        return autoLogon;
    }

    public void setAutoLogon(Boolean autoLogon) {
        this.autoLogon = autoLogon;
    }


    public Boolean getSecurityStrategyEnable() {
        return securityStrategyEnable;
    }

    public void setSecurityStrategyEnable(Boolean securityStrategyEnable) {
        this.securityStrategyEnable = securityStrategyEnable;
    }

    public String getIsOpenUpdateDays() {
        return isOpenUpdateDays;
    }

    public void setIsOpenUpdateDays(String isOpenUpdateDays) {
        this.isOpenUpdateDays = isOpenUpdateDays;
    }

    public Integer getUpdateDays() {
        return updateDays;
    }

    public void setUpdateDays(Integer updateDays) {
        this.updateDays = updateDays;
    }

    public String getPwdLevel() {
        return pwdLevel;
    }

    public void setPwdLevel(String pwdLevel) {
        this.pwdLevel = pwdLevel;
    }

    public Boolean getEnableForceUpdatePassword() {
        return enableForceUpdatePassword;
    }

    public void setEnableForceUpdatePassword(Boolean enableForceUpdatePassword) {
        this.enableForceUpdatePassword = enableForceUpdatePassword;
    }

    public Boolean getPreventsBruteForce() {
        return preventsBruteForce;
    }

    public void setPreventsBruteForce(Boolean preventsBruteForce) {
        this.preventsBruteForce = preventsBruteForce;
    }

    public Integer getUserLockedErrorTimes() {
        return userLockedErrorTimes;
    }

    public void setUserLockedErrorTimes(Integer userLockedErrorTimes) {
        this.userLockedErrorTimes = userLockedErrorTimes;
    }

    public String getIsOpenUserLockTime() {
        return isOpenUserLockTime;
    }

    public void setIsOpenUserLockTime(String isOpenUserLockTime) {
        this.isOpenUserLockTime = isOpenUserLockTime;
    }

    public Integer getUserLockTime() {
        return userLockTime;
    }

    public void setUserLockTime(Integer userLockTime) {
        this.userLockTime = userLockTime;
    }

    public Integer getPwdExpireRemindDays() {
        return pwdExpireRemindDays;
    }

    public void setPwdExpireRemindDays(Integer pwdExpireRemindDays) {
        this.pwdExpireRemindDays = pwdExpireRemindDays;
    }
}
