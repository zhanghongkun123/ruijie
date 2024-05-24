package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserAuthorityEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 11:15
 *
 * @author coderLee23
 */
@Table(name = "v_rco_user_group_sync_data")
@Entity
public class ViewUserGroupSyncDataEntity {

    /**
     * 用户组id
     */
    @Id
    private UUID id;

    /**
     * 用户分组名称
     **/
    private String name;

    /**
     * 用户分组全路径名称
     **/
    private String fullGroupName;

    /**
     * 用户组树的深度
     */
    private Integer depth;

    /**
     * 是否为 ad域组
     */
    @JSONField(name = "isAdGroup")
    private Boolean isAdGroup;

    /**
     * 是否为 ldap组
     */
    @JSONField(name = "isLdapGroup")
    private Boolean isLdapGroup;

    /**
     * 是否为第三方组
     */
    @JSONField(name = "isThirdPartyGroup")
    private Boolean isThirdPartyGroup;

    /**
     * 是否开启随机生成密码，并发送密码到客户邮箱
     */
    private Boolean enableRandomPassword;

    /**
     * AD域用户权限 枚举类型
     */
    @Enumerated(EnumType.STRING)
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

    /**
     * UserIdentityConfigEntity id,针对创建时使用
     */
    private UUID userIdentityConfigId;

    /** 关联对象类型 */
    @Enumerated(EnumType.STRING)
    private IacConfigRelatedType relatedType;

    /** 关联对象ID */
    private UUID relatedId;

    /** 登录权限等级 */
    @Enumerated(EnumType.STRING)
    private IacUserLoginIdentityLevelEnum loginIdentityLevel;

    /** 开启硬件特征码 */
    private Boolean openHardwareCertification;

    /** 最大终端数 */
    private Integer maxHardwareNum;

    /** 开启口令认证 */
    private Boolean openOtpCertification;

    /** 绑定口令认证 */
    private Boolean hasBindOtp;

    /**
     * 动态口令认证密钥
     */
    private String otpSecretKey;

    /**
     * CAS认证
     */
    private Boolean openCasCertification;

    /**
     * 账号密码认证
     */
    private Boolean openAccountPasswordCertification;

    /**
     * 开启短信认证
     */
    private Boolean openSmsCertification;

    /**
     * 开启第三方认证
     */
    private Boolean openThirdPartyCertification;

    /**
     * 开启Radius服务
     */
    private Boolean openRadiusCertification;

    private Date updateTime;

    @Version
    private Integer version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public Boolean getEnableRandomPassword() {
        return enableRandomPassword;
    }

    public void setEnableRandomPassword(Boolean enableRandomPassword) {
        this.enableRandomPassword = enableRandomPassword;
    }

    public IacAdUserAuthorityEnum getAdUserAuthority() {
        return adUserAuthority;
    }

    public void setAdUserAuthority(IacAdUserAuthorityEnum adUserAuthority) {
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

    public UUID getUserIdentityConfigId() {
        return userIdentityConfigId;
    }

    public void setUserIdentityConfigId(UUID userIdentityConfigId) {
        this.userIdentityConfigId = userIdentityConfigId;
    }

    public IacConfigRelatedType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(IacConfigRelatedType relatedType) {
        this.relatedType = relatedType;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public IacUserLoginIdentityLevelEnum getLoginIdentityLevel() {
        return loginIdentityLevel;
    }

    public void setLoginIdentityLevel(IacUserLoginIdentityLevelEnum loginIdentityLevel) {
        this.loginIdentityLevel = loginIdentityLevel;
    }

    public Boolean getOpenHardwareCertification() {
        return openHardwareCertification;
    }

    public void setOpenHardwareCertification(Boolean openHardwareCertification) {
        this.openHardwareCertification = openHardwareCertification;
    }

    public Integer getMaxHardwareNum() {
        return maxHardwareNum;
    }

    public void setMaxHardwareNum(Integer maxHardwareNum) {
        this.maxHardwareNum = maxHardwareNum;
    }

    public Boolean getOpenOtpCertification() {
        return openOtpCertification;
    }

    public void setOpenOtpCertification(Boolean openOtpCertification) {
        this.openOtpCertification = openOtpCertification;
    }

    public Boolean getHasBindOtp() {
        return hasBindOtp;
    }

    public void setHasBindOtp(Boolean hasBindOtp) {
        this.hasBindOtp = hasBindOtp;
    }

    public String getOtpSecretKey() {
        return otpSecretKey;
    }

    public void setOtpSecretKey(String otpSecretKey) {
        this.otpSecretKey = otpSecretKey;
    }

    public Boolean getOpenCasCertification() {
        return openCasCertification;
    }

    public void setOpenCasCertification(Boolean openCasCertification) {
        this.openCasCertification = openCasCertification;
    }

    public Boolean getOpenAccountPasswordCertification() {
        return openAccountPasswordCertification;
    }

    public void setOpenAccountPasswordCertification(Boolean openAccountPasswordCertification) {
        this.openAccountPasswordCertification = openAccountPasswordCertification;
    }

    public Boolean getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(Boolean openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
    }

    public Boolean getOpenThirdPartyCertification() {
        return openThirdPartyCertification;
    }

    public void setOpenThirdPartyCertification(Boolean openThirdPartyCertification) {
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
