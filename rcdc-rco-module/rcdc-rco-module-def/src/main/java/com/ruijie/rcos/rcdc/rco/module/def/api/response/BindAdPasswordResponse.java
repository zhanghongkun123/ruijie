package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.common.dto.Result;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;

/**
 * Description: 客户端绑定加域桌面密码
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:32
 *
 * @author wanglianyun
 */
public class BindAdPasswordResponse extends Result {

    private LoginResultMessageDTO loginResultMessageDTO;

    public LoginResultMessageDTO getLoginResultMessageDTO() {
        return loginResultMessageDTO;
    }

    public void setLoginResultMessageDTO(LoginResultMessageDTO loginResultMessageDTO) {
        this.loginResultMessageDTO = loginResultMessageDTO;
    }
}
