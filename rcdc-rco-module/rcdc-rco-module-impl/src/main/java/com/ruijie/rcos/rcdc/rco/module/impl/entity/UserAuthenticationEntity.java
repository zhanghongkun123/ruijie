package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 用户登录密码等认证信息实体类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年02月23日
 *
 * @author linke
 */
@Entity
@Table(name = "t_rco_user_authentication")
public class UserAuthenticationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID userId;

    /**
     * 是否锁定，默认false
     */
    private Boolean isLock;

    /**
     * 锁定时间
     */
    private Date lockTime;

    /**
     * 解锁时间
     */
    private Date unlockTime;

    /**
     * 密码输错次数
     */
    private Integer pwdErrorTimes;

    /**
     * 上一次登录时间
     */
    private Date lastLoginTime;

    /**
     * 密码修改时间
     */
    private Date updatePasswordTime;

    @Version
    private Integer version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Boolean getIsLock() {
        return isLock;
    }

    public void setIsLock(Boolean isLock) {
        this.isLock = isLock;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public Date getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(Date unlockTime) {
        this.unlockTime = unlockTime;
    }

    public Integer getPwdErrorTimes() {
        return pwdErrorTimes;
    }

    public void setPwdErrorTimes(Integer pwdErrorTimes) {
        this.pwdErrorTimes = pwdErrorTimes;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getUpdatePasswordTime() {
        return updatePasswordTime;
    }

    public void setUpdatePasswordTime(Date updatePasswordTime) {
        this.updatePasswordTime = updatePasswordTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
