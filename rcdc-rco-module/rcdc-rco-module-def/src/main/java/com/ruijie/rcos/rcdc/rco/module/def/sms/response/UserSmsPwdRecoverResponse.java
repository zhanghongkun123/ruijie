package com.ruijie.rcos.rcdc.rco.module.def.sms.response;

import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.SmsPwdRecoverNotifyDTO;

/**
 * Description: 用户短信密码找回返回
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/3
 *
 * @author TD
 */
public class UserSmsPwdRecoverResponse extends SmsPwdRecoverNotifyDTO {

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
