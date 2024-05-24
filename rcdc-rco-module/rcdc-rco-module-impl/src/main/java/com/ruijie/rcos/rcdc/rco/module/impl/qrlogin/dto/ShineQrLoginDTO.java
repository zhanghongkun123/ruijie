package com.ruijie.rcos.rcdc.rco.module.impl.qrlogin.dto;

import org.springframework.lang.Nullable;

/**
 * Description: 客户端扫码登录
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/06
 *
 * @author zhang.zhiwen
 */
public class ShineQrLoginDTO {

    private String loginId;

    @Nullable
    private String userName;


    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
