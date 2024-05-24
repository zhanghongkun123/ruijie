package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: cms docking rest response
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/01/16
 *
 * @author ljm
 */
public class LoginAdminResponse extends DefaultResponse {

    private int authCode;

    /**
     * 锁定时长，单位分钟
     */
    private Integer pwdLockTime;

    /**
     * 剩余次数
     */
    private Integer remainingTimes;

    /**
     * 管理员是否首次登录
     */
    private Boolean hasFirstTimeLoggedIn;

    public LoginAdminResponse() {

    }

    public LoginAdminResponse(int authCode) {
        this.authCode = authCode;
    }

    public int getAuthCode() {
        return authCode;
    }

    public void setAuthCode(int authCode) {
        this.authCode = authCode;
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

    public Boolean getHasFirstTimeLoggedIn() {
        return hasFirstTimeLoggedIn;
    }

    public void setHasFirstTimeLoggedIn(Boolean hasFirstTimeLoggedIn) {
        this.hasFirstTimeLoggedIn = hasFirstTimeLoggedIn;
    }
}
