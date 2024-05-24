package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.Date;

/**
 * Description: 同步终端和服务器中用户锁定信息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年06月30日
 *
 * @author yxq
 */
public class SyncUserInfoDTO extends ShineCommonRequest {

    /**
     * 是否锁定
     */
    private Boolean lock;

    /**
     * 密码错误次数
     */
    private Integer errorTimes;

    /**
     * 被锁定的时间
     */
    private Date lockTime;

    /**
     * 用户允许错误次数
     */
    private Integer userLockedErrorsTimes;

    /**
     * 锁定时长
     */
    private Integer pwdLockTime;

    /**
     * 是否开启防爆功能
     */
    private Boolean preventsBruteForce;

    /**
     * 是否为策略变更
     */
    private Boolean hasEditStrategy;
    
    /**
     * 时间戳
     */
    private Long timestamp;

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public Integer getErrorTimes() {
        return errorTimes;
    }

    public void setErrorTimes(Integer errorTimes) {
        this.errorTimes = errorTimes;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public Integer getUserLockedErrorsTimes() {
        return userLockedErrorsTimes;
    }

    public void setUserLockedErrorsTimes(Integer userLockedErrorsTimes) {
        this.userLockedErrorsTimes = userLockedErrorsTimes;
    }

    public Integer getPwdLockTime() {
        return pwdLockTime;
    }

    public void setPwdLockTime(Integer pwdLockTime) {
        this.pwdLockTime = pwdLockTime;
    }

    public Boolean getPreventsBruteForce() {
        return preventsBruteForce;
    }

    public void setPreventsBruteForce(Boolean preventsBruteForce) {
        this.preventsBruteForce = preventsBruteForce;
    }

    public Boolean getHasEditStrategy() {
        return hasEditStrategy;
    }

    public void setHasEditStrategy(Boolean hasEditStrategy) {
        this.hasEditStrategy = hasEditStrategy;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
