package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.rco.module.def.enums.AuthType;

/**
 * Description: 登录认证方式
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:21
 *
 * @author wanglianyun
 */
public class LoginAuthTypeDTO {

    /**
     * 认证方式显示的名称
     */
    private String showName;

    /**
     * 认证方式图标 base64转一层
     */
    private String logo;

    /**
     * 认证方式类型 其他非Auth2.0标准的认证类型暂归类为OTHER
     */
    private String authType;


    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

}
