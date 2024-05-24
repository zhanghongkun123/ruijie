package com.ruijie.rcos.rcdc.rco.module.def.api.response;

/**
 * Description: CMS修改用户密码响应
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/28 14:30
 *
 * @author yxq
 */
public class ModifyUserPwdResponse {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 密码锁定时长，单位：分钟
     */
    private Integer pwdLockTime;

    /**
     * 剩余密码允许错误次数
     */
    private Integer remainingTimes;

    public ModifyUserPwdResponse() {
    }

    public ModifyUserPwdResponse(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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
