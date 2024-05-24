package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;

import java.util.Date;
import java.util.UUID;

/**
 * Description: ShineLoginResponseDTO
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年04月22日
 *
 * @author fang
 */
public class ShineLoginResponseDTO {
    /**
     * 响应码
     */
    private Integer code = CommonMessageCode.SUCCESS;

    private int authResultCode;

    private Boolean needUpdatePassword;

    // 存放上层业务返回值
    private Object content;

    /**
     * 结果提示信息
     */
    private LoginResultMessageDTO loginResultMessage;

    /**
     * 用户类型
     */
    private IacUserTypeEnum userType;

    private String userName;

    private String password;

    /**
     * 账户过期时间
     */
    private Date accountExpireDate;

    private UUID userId;

    /**
     * 是否显示图形验证码
     */
    private Boolean needShowCaptcha;

    /**
     * ad域用户域名，其他用户为空
     */
    private String domainName = "";

    private Boolean hasBindDomainPassword;

    public Boolean getNeedShowCaptcha() {
        return needShowCaptcha;
    }

    public void setNeedShowCaptcha(Boolean needShowCaptcha) {
        this.needShowCaptcha = needShowCaptcha;
    }


    public int getAuthResultCode() {
        return authResultCode;
    }

    public void setAuthResultCode(int authResultCode) {
        this.authResultCode = authResultCode;
    }

    public Boolean getNeedUpdatePassword() {
        return needUpdatePassword;
    }

    public void setNeedUpdatePassword(Boolean needUpdatePassword) {
        this.needUpdatePassword = needUpdatePassword;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public LoginResultMessageDTO getLoginResultMessage() {
        return loginResultMessage;
    }

    public void setLoginResultMessage(LoginResultMessageDTO loginResultMessage) {
        this.loginResultMessage = loginResultMessage;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(Date accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Boolean getHasBindDomainPassword() {
        return hasBindDomainPassword;
    }

    public void setHasBindDomainPassword(Boolean hasBindDomainPassword) {
        this.hasBindDomainPassword = hasBindDomainPassword;
    }
}
