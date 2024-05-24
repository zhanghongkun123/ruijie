package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;

/**
 * Description: 用户登录信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-18 11:53:00
 *
 * @author zjy
 */
public class UserUpdatePwdDTO {

    private Integer businessCode;

    private Integer pwdLockTime;

    private Integer remainingTimes;

    public Integer getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(Integer businessCode) {
        this.businessCode = businessCode;
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
}