package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.io.Serializable;
import java.util.Date;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.*;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 11:36
 *
 * @author coderLee23
 */
public class UserSyncDataDTO implements Serializable {

    private static final long serialVersionUID = 2117695194349426196L;

    /**
     * 用户名称
     **/
    @NotBlank
    private String name;

    /**
     * 用户姓名
     */
    @Nullable
    private String realName;

    /**
     * 用户组
     */
    @NotBlank
    private String fullGroupName;

    /**
     * 用户组树的深度
     */
    @NotNull
    private Integer depth;

    /**
     * 用户密码
     **/
    @NotBlank
    private String password;

    /**
     * 用户手机号
     **/
    @Nullable
    private String phoneNum;

    /**
     * 用户邮箱
     **/
    @Nullable
    private String email;

    /**
     * 是否需要更新密码
     */
    @Nullable
    private Boolean needUpdatePassword;

    /**
     * 是否被管理员重置密码，用户重置后若修改密码则改为False
     */
    @NotNull
    private Boolean resetPasswordByAdmin = Boolean.FALSE;

    /**
     * 用户描述
     **/
    @Nullable
    private String description;

    /**
     * 用户类型
     */
    @NotNull
    private IacUserTypeEnum userType;

    /**
     * 用户状态
     */
    @NotNull
    private IacUserStateEnum state = IacUserStateEnum.ENABLE;

    /**
     * AD域用户权限
     **/
    @NotNull
    private IacAdUserAuthorityEnum adUserAuthority;

    /**
     * 用户是否修改过密码
     */
    @Nullable
    private Boolean isUserModifyPassword;

    @NotNull
    private Date updateTime;

    @Nullable
    private Long accountExpireDate;

    @Nullable
    private Boolean enableDomainSync;

    /**
     * AD域组集合JSON [{"dn":"str","objectGuid":""}]
     */
    @Nullable
    private String adGroupJson;

    @NotNull
    private Integer invalidTime;

    @Nullable
    private Date invalidRecoverTime;

    @Nullable
    private Date loginOutTime;

    @NotNull
    private Boolean invalid;

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
    @Nullable
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getNeedUpdatePassword() {
        return needUpdatePassword;
    }

    public void setNeedUpdatePassword(Boolean needUpdatePassword) {
        this.needUpdatePassword = needUpdatePassword;
    }

    public Boolean getResetPasswordByAdmin() {
        return resetPasswordByAdmin;
    }

    public void setResetPasswordByAdmin(Boolean resetPasswordByAdmin) {
        this.resetPasswordByAdmin = resetPasswordByAdmin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
    }

    public IacUserStateEnum getState() {
        return state;
    }

    public void setState(IacUserStateEnum state) {
        this.state = state;
    }

    public IacAdUserAuthorityEnum getAdUserAuthority() {
        return adUserAuthority;
    }

    public void setAdUserAuthority(IacAdUserAuthorityEnum adUserAuthority) {
        this.adUserAuthority = adUserAuthority;
    }

    public Boolean getUserModifyPassword() {
        return isUserModifyPassword;
    }

    public void setUserModifyPassword(Boolean userModifyPassword) {
        isUserModifyPassword = userModifyPassword;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(Long accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    public Boolean getEnableDomainSync() {
        return enableDomainSync;
    }

    public void setEnableDomainSync(Boolean enableDomainSync) {
        this.enableDomainSync = enableDomainSync;
    }

    public String getAdGroupJson() {
        return adGroupJson;
    }

    public void setAdGroupJson(String adGroupJson) {
        this.adGroupJson = adGroupJson;
    }

    public Integer getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Integer invalidTime) {
        this.invalidTime = invalidTime;
    }

    public Date getInvalidRecoverTime() {
        return invalidRecoverTime;
    }

    public void setInvalidRecoverTime(Date invalidRecoverTime) {
        this.invalidRecoverTime = invalidRecoverTime;
    }

    public Date getLoginOutTime() {
        return loginOutTime;
    }

    public void setLoginOutTime(Date loginOutTime) {
        this.loginOutTime = loginOutTime;
    }

    public Boolean getInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
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

}
