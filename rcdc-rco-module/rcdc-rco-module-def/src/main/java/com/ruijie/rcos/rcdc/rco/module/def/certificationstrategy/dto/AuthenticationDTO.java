package com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CertificationTypeEnum;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 用户密码、终端管理密码登录等认证信息DTO类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/03/11
 *
 * @author linke
 */
public class AuthenticationDTO {

    private UUID id;

    /**
     * 管理员、用户或者终端ID
     */
    private UUID resourceId;

    /**
     * 管理员、用户或者终端管理密码
     */
    private CertificationTypeEnum type;

    /**
     * 密码错误次数
     */
    private Integer pwdErrorTimes;

    /**
     * 是否锁定
     */
    private Boolean lock;

    /**
     * 锁定时间
     */
    private Date lockTime;

    /**
     * 解锁时间
     */
    private Date unlockTime;

    /**
     * 上一次登录时间
     */
    private Date lastLoginTime;

    /**
     * 密码修改时间
     */
    private Date updatePasswordTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getResourceId() {
        return resourceId;
    }

    public void setResourceId(UUID resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getPwdErrorTimes() {
        return pwdErrorTimes;
    }

    public void setPwdErrorTimes(Integer pwdErrorTimes) {
        this.pwdErrorTimes = pwdErrorTimes;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
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

    public CertificationTypeEnum getType() {
        return type;
    }

    public void setType(CertificationTypeEnum type) {
        this.type = type;
    }
}
