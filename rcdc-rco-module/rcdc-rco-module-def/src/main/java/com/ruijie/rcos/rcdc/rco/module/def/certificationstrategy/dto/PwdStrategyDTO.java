package com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto;

import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

/**
 * 密码认证策略DTO
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年2月25日
 *
 * @author linke
 */
public class PwdStrategyDTO extends EqualsHashcodeSupport {

    /**
     * 是否启用密码安全策略：0不启用，1启用
     */
    private Boolean securityStrategyEnable;

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
     * 用户锁定时间，单位分钟
     */
    private Integer userLockTime;

    /**
     * 最大错误次数，超过锁定管理员
     */
    private Integer adminLockedErrorTimes;

    /**
     * 管理员锁定时间，单位分钟
     */
    private Integer adminLockTime;

    /**
     * 最大错误次数，超过锁定终端管理密码
     */
    private Integer terminalLockedErrorTimes;

    /**
     * 终端管理密码锁定时间，单位分钟
     */
    private Integer terminalLockTime;

    /**
     * 是否允许修改密码
     */
    private Boolean changePassword;

    /**
     *  是否启用弱密码库
     */
    private Boolean enablePasswordBlacklist;

    public Boolean getEnablePasswordBlacklist() {
        return enablePasswordBlacklist;
    }

    public void setEnablePasswordBlacklist(Boolean enablePasswordBlacklist) {
        this.enablePasswordBlacklist = enablePasswordBlacklist;
    }

    public Boolean getSecurityStrategyEnable() {
        return securityStrategyEnable;
    }

    public void setSecurityStrategyEnable(Boolean securityStrategyEnable) {
        this.securityStrategyEnable = securityStrategyEnable;
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

    public Integer getAdminLockedErrorTimes() {
        return adminLockedErrorTimes;
    }

    public void setAdminLockedErrorTimes(Integer adminLockedErrorTimes) {
        this.adminLockedErrorTimes = adminLockedErrorTimes;
    }

    public Integer getAdminLockTime() {
        return adminLockTime;
    }

    public void setAdminLockTime(Integer adminLockTime) {
        this.adminLockTime = adminLockTime;
    }

    public Integer getTerminalLockedErrorTimes() {
        return terminalLockedErrorTimes;
    }

    public void setTerminalLockedErrorTimes(Integer terminalLockedErrorTimes) {
        this.terminalLockedErrorTimes = terminalLockedErrorTimes;
    }

    public Integer getTerminalLockTime() {
        return terminalLockTime;
    }

    public void setTerminalLockTime(Integer terminalLockTime) {
        this.terminalLockTime = terminalLockTime;
    }

    public Boolean getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(Boolean changePassword) {
        this.changePassword = changePassword;
    }
}
