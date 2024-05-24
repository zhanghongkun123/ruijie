package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.response;

/**
 * Description: 管理员登录响应体
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/26
 *
 * @author WuShengQiang
 */
public class LoginAdminAuthResponse extends CommonRestServerResponse {

    private boolean hasFirstTimeLoggedIn;

    private Boolean weekPassword;

    private Boolean needUpdatePassword;

    private Integer passwordRemindTimes;

    public LoginAdminAuthResponse(int code) {
        super(code);
    }

    public boolean isHasFirstTimeLoggedIn() {
        return hasFirstTimeLoggedIn;
    }

    public void setHasFirstTimeLoggedIn(boolean hasFirstTimeLoggedIn) {
        this.hasFirstTimeLoggedIn = hasFirstTimeLoggedIn;
    }

    public Boolean getWeekPassword() {
        return weekPassword;
    }

    public void setWeekPassword(Boolean weekPassword) {
        this.weekPassword = weekPassword;
    }

    public Boolean getNeedUpdatePassword() {
        return needUpdatePassword;
    }

    public void setNeedUpdatePassword(Boolean needUpdatePassword) {
        this.needUpdatePassword = needUpdatePassword;
    }

    public Integer getPasswordRemindTimes() {
        return passwordRemindTimes;
    }

    public void setPasswordRemindTimes(Integer passwordRemindTimes) {
        this.passwordRemindTimes = passwordRemindTimes;
    }
}
