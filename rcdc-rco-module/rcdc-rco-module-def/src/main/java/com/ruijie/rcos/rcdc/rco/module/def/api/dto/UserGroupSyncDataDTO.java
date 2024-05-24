package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.io.Serializable;
import java.util.Date;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserAuthorityEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 10:56
 *
 * @author coderLee23
 */
public class UserGroupSyncDataDTO implements Serializable {

    private static final long serialVersionUID = 1962853070757585051L;

    /**
     * 用户分组名称
     **/
    @NotBlank
    private String name;

    /**
     * 用户分组全路径名称
     **/
    @NotBlank
    private String fullGroupName;

    /**
     * 用户组树的深度
     */
    @NotNull
    private Integer depth;

    /**
     * 是否为 ad域组
     */
    @NotNull
    @JSONField(name = "isAdGroup")
    private Boolean isAdGroup;

    /**
     * 是否为 ldap组
     */
    @NotNull
    @JSONField(name = "isLdapGroup")
    private Boolean isLdapGroup;

    /**
     * 是否为第三方用户组
     */
    @NotNull
    @JSONField(name = "isThirdPartyGroup")
    private Boolean isThirdPartyGroup;

    /**
     * 是否开启随机生成密码，并发送密码到客户邮箱
     */
    @Nullable
    private Boolean enableRandomPassword;

    /**
     * AD域用户权限 枚举类型
     */
    @Nullable
    private IacAdUserAuthorityEnum adUserAuthority;

    /**
     * 账号到期时间
     */
    @Nullable
    private Long accountExpireDate;

    /**
     * 账号失效时间
     */
    @Nullable
    private Integer invalidTime;


    /** 关联对象类型 */
    @NotNull
    private IacConfigRelatedType relatedType;


    /** 登录权限等级 */
    @NotNull
    private IacUserLoginIdentityLevelEnum loginIdentityLevel;

    /** 开启硬件特征码 */
    @Nullable
    private Boolean openHardwareCertification;

    /** 最大终端数 */
    @Nullable
    private Integer maxHardwareNum;

    /** 开启口令认证 */
    @NotNull
    private Boolean openOtpCertification;

    /** 绑定口令认证 */
    @Nullable
    private Boolean hasBindOtp;

    /**
     * 动态口令认证密钥
     */
    @Nullable
    private String otpSecretKey;

    /**
     * CAS认证
     */
    @Nullable
    private Boolean openCasCertification;

    /**
     * 账号密码认证
     */
    @Nullable
    private Boolean openAccountPasswordCertification;

    /**
     * 开启短信认证
     */
    @Nullable
    private Boolean openSmsCertification;


    /**
     * 开启第三方认证
     */
    @Nullable
    private Boolean openThirdPartyCertification;

    /**
     * 开启Radius服务
     */
    @Nullable
    private Boolean openRadiusCertification;

    @NotNull
    private Date updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullGroupName() {
        return fullGroupName;
    }

    public void setFullGroupName(String fullGroupName) {
        this.fullGroupName = fullGroupName;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Boolean getAdGroup() {
        return isAdGroup;
    }

    public void setAdGroup(Boolean adGroup) {
        isAdGroup = adGroup;
    }

    public Boolean getLdapGroup() {
        return isLdapGroup;
    }

    public void setLdapGroup(Boolean ldapGroup) {
        isLdapGroup = ldapGroup;
    }

    public Boolean getThirdPartyGroup() {
        return isThirdPartyGroup;
    }

    public void setThirdPartyGroup(Boolean thirdPartyGroup) {
        isThirdPartyGroup = thirdPartyGroup;
    }

    @Nullable
    public Boolean getEnableRandomPassword() {
        return enableRandomPassword;
    }

    public void setEnableRandomPassword(@Nullable Boolean enableRandomPassword) {
        this.enableRandomPassword = enableRandomPassword;
    }

    @Nullable
    public IacAdUserAuthorityEnum getAdUserAuthority() {
        return adUserAuthority;
    }

    public void setAdUserAuthority(@Nullable IacAdUserAuthorityEnum adUserAuthority) {
        this.adUserAuthority = adUserAuthority;
    }

    @Nullable
    public Long getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(@Nullable Long accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    @Nullable
    public Integer getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(@Nullable Integer invalidTime) {
        this.invalidTime = invalidTime;
    }

    public IacConfigRelatedType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(IacConfigRelatedType relatedType) {
        this.relatedType = relatedType;
    }

    public IacUserLoginIdentityLevelEnum getLoginIdentityLevel() {
        return loginIdentityLevel;
    }

    public void setLoginIdentityLevel(IacUserLoginIdentityLevelEnum loginIdentityLevel) {
        this.loginIdentityLevel = loginIdentityLevel;
    }

    @Nullable
    public Boolean getOpenHardwareCertification() {
        return openHardwareCertification;
    }

    public void setOpenHardwareCertification(@Nullable Boolean openHardwareCertification) {
        this.openHardwareCertification = openHardwareCertification;
    }

    @Nullable
    public Integer getMaxHardwareNum() {
        return maxHardwareNum;
    }

    public void setMaxHardwareNum(@Nullable Integer maxHardwareNum) {
        this.maxHardwareNum = maxHardwareNum;
    }

    public Boolean getOpenOtpCertification() {
        return openOtpCertification;
    }

    public void setOpenOtpCertification(Boolean openOtpCertification) {
        this.openOtpCertification = openOtpCertification;
    }

    @Nullable
    public Boolean getHasBindOtp() {
        return hasBindOtp;
    }

    public void setHasBindOtp(@Nullable Boolean hasBindOtp) {
        this.hasBindOtp = hasBindOtp;
    }

    @Nullable
    public String getOtpSecretKey() {
        return otpSecretKey;
    }

    public void setOtpSecretKey(@Nullable String otpSecretKey) {
        this.otpSecretKey = otpSecretKey;
    }

    @Nullable
    public Boolean getOpenCasCertification() {
        return openCasCertification;
    }

    public void setOpenCasCertification(@Nullable Boolean openCasCertification) {
        this.openCasCertification = openCasCertification;
    }

    @Nullable
    public Boolean getOpenAccountPasswordCertification() {
        return openAccountPasswordCertification;
    }

    public void setOpenAccountPasswordCertification(@Nullable Boolean openAccountPasswordCertification) {
        this.openAccountPasswordCertification = openAccountPasswordCertification;
    }

    @Nullable
    public Boolean getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(@Nullable Boolean openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
    }

    @Nullable
    public Boolean getOpenThirdPartyCertification() {
        return openThirdPartyCertification;
    }

    public void setOpenThirdPartyCertification(@Nullable Boolean openThirdPartyCertification) {
        this.openThirdPartyCertification = openThirdPartyCertification;
    }

    public Boolean getOpenRadiusCertification() {
        return openRadiusCertification;
    }

    public void setOpenRadiusCertification(Boolean openRadiusCertification) {
        this.openRadiusCertification = openRadiusCertification;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
