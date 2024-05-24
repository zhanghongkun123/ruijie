package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;

import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;

/**
 * Description: 校验动态口令码响应体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/5
 *
 * @author WuShengQiang
 */
public class CheckUserOtpCodeResultDTO {

    private Integer businessCode;

    private LoginResultMessageDTO loginResultMessageDTO;

    public Integer getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(Integer businessCode) {
        this.businessCode = businessCode;
    }

    public LoginResultMessageDTO getLoginResultMessageDTO() {
        return loginResultMessageDTO;
    }

    public void setLoginResultMessageDTO(LoginResultMessageDTO loginResultMessageDTO) {
        this.loginResultMessageDTO = loginResultMessageDTO;
    }
}
