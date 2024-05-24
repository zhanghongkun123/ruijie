package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DomainServerType;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: AD/LDAP域配置的基本请求参数
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-04-09 15:35
 *
 * @author zql
 */
public class DomainConfigBaseWebRequest implements WebRequest {

    @IPv4Address
    protected String serverIp;

    @NotBlank
    @Size(min = 1, max = 6)
    protected String serverPort;

    @Nullable
    @Size(max = 128)
    @TextName
    protected String serverName;

    /**
     * LDAP上为userDn,AD域上为用户名
     */
    @Size(min = 1, max = 128)
    protected String managerName;

    @Size(min = 1)
    protected String managerPassword;

    /** AD域域名 */
    @Nullable
    @Size(max = 128)
    protected String domainName;

    @NotNull
    private DomainServerType serverType;

    /**
     * LDAP所需的字段:
     */
    @Nullable
    @Size(max = 128)
    private String baseDn;

    @Nullable
    @Size(max = 128)
    @TextName
    private String objectClass;

    @Nullable
    @Size(max = 64)
    private String usernameAttr;

    @Nullable
    @Size(max = 64)
    private String mobileAttr;

    @Nullable
    @Size(max = 64)
    private String mailAttr;

    /**
     * 启用匿名访问
     */
    @NotNull
    private Boolean enableAnonymous;

    @Nullable
    private String adOu;

    @ApiModelProperty("管理员鉴权DN")
    @Nullable
    private String managerDn;

    /**
     * 姓名属性
     */
    @Nullable
    @Size(max = 64)
    private String realNameAttr;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPassword() {
        return managerPassword;
    }

    public void setManagerPassword(String managerPassword) {
        this.managerPassword = managerPassword;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    @Nullable
    public String getBaseDn() {
        return baseDn;
    }

    public void setBaseDn(@Nullable String baseDn) {
        this.baseDn = baseDn;
    }

    @Nullable
    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(@Nullable String objectClass) {
        this.objectClass = objectClass;
    }

    @Nullable
    public String getUsernameAttr() {
        return usernameAttr;
    }

    public void setUsernameAttr(@Nullable String usernameAttr) {
        this.usernameAttr = usernameAttr;
    }

    @Nullable
    public String getMobileAttr() {
        return mobileAttr;
    }

    public void setMobileAttr(@Nullable String mobileAttr) {
        this.mobileAttr = mobileAttr;
    }

    @Nullable
    public String getMailAttr() {
        return mailAttr;
    }

    public void setMailAttr(@Nullable String mailAttr) {
        this.mailAttr = mailAttr;
    }

    public DomainServerType getServerType() {
        return serverType;
    }

    public void setServerType(DomainServerType serverType) {
        this.serverType = serverType;
    }

    public Boolean getEnableAnonymous() {
        return enableAnonymous;
    }

    public void setEnableAnonymous(Boolean enableAnonymous) {
        this.enableAnonymous = enableAnonymous;
    }

    @Nullable
    public String getAdOu() {
        return adOu;
    }

    public void setAdOu(@Nullable String adOu) {
        this.adOu = adOu;
    }

    @Nullable
    public String getManagerDn() {
        return managerDn;
    }

    public void setManagerDn(@Nullable String managerDn) {
        this.managerDn = managerDn;
    }

    @Nullable
    public String getRealNameAttr() {
        return realNameAttr;
    }

    public void setRealNameAttr(@Nullable String realNameAttr) {
        this.realNameAttr = realNameAttr;
    }
}
