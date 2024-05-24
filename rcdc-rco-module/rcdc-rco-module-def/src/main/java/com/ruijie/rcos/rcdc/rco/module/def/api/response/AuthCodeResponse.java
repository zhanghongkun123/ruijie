package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.common.dto.Result;

import java.util.UUID;

/**
 * Description: 授权码认证返回DTO
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:31
 *
 * @author wanglianyun
 */
public class AuthCodeResponse extends Result {

    /**
     * 是否需要绑定域密码
     */
    private Boolean enableBindAdPassword;

    private String userName;

    private UUID userId;

    private String password;

    private IacUserTypeEnum userType;

    private String domainName;

    private Boolean hasBindDomainPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Boolean getEnableBindAdPassword() {
        return enableBindAdPassword;
    }

    public void setEnableBindAdPassword(Boolean enableBindAdPassword) {
        this.enableBindAdPassword = enableBindAdPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Boolean getHasBindDomainPassword() {
        return hasBindDomainPassword;
    }

    public void setHasBindDomainPassword(Boolean hasBindDomainPassword) {
        this.hasBindDomainPassword = hasBindDomainPassword;
    }
}
