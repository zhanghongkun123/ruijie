package com.ruijie.rcos.rcdc.rco.module.def.user.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.IacThirdPartyCertificationType;
import com.ruijie.rcos.sk.base.annotation.IPv4Address;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 认证请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月16日
 *
 * @author xwx
 */
public class ThirdPartyCertificationRequest {

    /**
     * 认证类型
     */
    @NotNull
    private IacThirdPartyCertificationType certificationType;

    /**
     * 用户名
     */
    @NotBlank
    private String userName;

    /**
     * 动态码
     */
    @NotBlank
    private String certificationCode;

    @Nullable
    @IPv4Address
    private String loginIp;

    public IacThirdPartyCertificationType getCertificationType() {
        return certificationType;
    }

    public void setCertificationType(IacThirdPartyCertificationType certificationType) {
        this.certificationType = certificationType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCertificationCode() {
        return certificationCode;
    }

    public void setCertificationCode(String certificationCode) {
        this.certificationCode = certificationCode;
    }

    @Nullable
    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(@Nullable String loginIp) {
        this.loginIp = loginIp;
    }
}
