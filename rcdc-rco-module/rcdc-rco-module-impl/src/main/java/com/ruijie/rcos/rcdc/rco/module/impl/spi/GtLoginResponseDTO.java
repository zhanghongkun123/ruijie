package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;

/**
 * Description: 用户在GT登录响应结果
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/18 14:37
 *
 * @author yxq
 */
public class GtLoginResponseDTO {

    /**
     * 结果提示信息
     */
    private LoginResultMessageDTO loginResultMessage;

    public LoginResultMessageDTO getLoginResultMessage() {
        return loginResultMessage;
    }

    public void setLoginResultMessage(LoginResultMessageDTO loginResultMessage) {
        this.loginResultMessage = loginResultMessage;
    }
}
