package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;

import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;

/**
 * Description: 动态口令登录响应体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/6
 *
 * @author WuShengQiang
 */
public class OtpCodeLoginResultDTO {

    private Integer businessCode;

    private LoginResultMessageDTO loginResultMessageDTO;

    private UserInfoDTO userInfoDTO;

    /**
     * 启用短信认证
     */
    private Boolean openSmsCertification;

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

    public UserInfoDTO getUserInfoDTO() {
        return userInfoDTO;
    }

    public void setUserInfoDTO(UserInfoDTO userInfoDTO) {
        this.userInfoDTO = userInfoDTO;
    }

    public Boolean getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(Boolean openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
    }
}
