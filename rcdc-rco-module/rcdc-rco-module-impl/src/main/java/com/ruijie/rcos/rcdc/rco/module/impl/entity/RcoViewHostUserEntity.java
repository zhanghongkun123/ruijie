package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserAuthorityEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseDurationEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.LoginInfoChangeDTO;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


/**
 * Description: 用户视图实体对象
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/28
 *
 * @author Jarman
 */
@Entity
@Table(name = "v_rco_host_user")
public class RcoViewHostUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID hostId;

    private String password;

    private UUID desktopPoolId;

    private UUID desktopId;

    /**
     * 用户角色
     */
    private String userRole;

    @Version
    private Integer version;

    @Enumerated(EnumType.STRING)
    private IacUserTypeEnum userType;

    private String realName;

    private String userName;

    private UUID groupId;

    private String groupName;

    @Enumerated(EnumType.STRING)
    private IacUserStateEnum state;

    private Date createTime;

    private String email;

    private String phoneNum;

    private Integer desktopNum;

    private String userDescription;

    @Enumerated(EnumType.STRING)
    private IacAdUserAuthorityEnum adUserAuthority;

    private Boolean needUpdatePassword;

    /**
     * 是否锁定，默认false
     */
    private Boolean lock;

    /**
     * 锁定时间
     */
    private Date lockTime;

    /**
     * 解锁时间
     */
    private Date unlockTime;

    /**
     * 密码输错次数
     */
    private Integer pwdErrorTimes;

    /**
     * 上一次登录时间
     */
    private Date lastLoginTime;

    /**
     * 密码修改时间
     */
    private Date updatePasswordTime;

    /**
     * 开启口令认证
     */
    private Boolean openOtpCertification;

    /** 动态口令绑定 */
    private Boolean hasBindOtp;

    /**
     * 开启Radius认证服务
     */
    private Boolean openRadiusCertification;

    /**
     * 开启外部CAS认证
     */
    private Boolean openCasCertification;

    /**
     * 开启账号密码认证
     */
    private Boolean openAccountPasswordCertification;

    /**
     * 开启第三方认证
     */
    private Boolean openThirdPartyCertification;

    /**
     * 是否开启硬件特征码
     */
    private Boolean openHardwareCertification;

    /**
     * 可绑定终端数
     */
    private Integer maxHardwareNum;

    /**
     * 账户过期时间
     */
    private Long accountExpireDate;

    /**
     * 是否开启域同步
     */
    private Boolean enableDomainSync;

    /**
     * 账户失效天数
     */
    private Integer invalidTime;

    /**
     * 失效账户恢复时间
     */
    private Date invalidRecoverTime;

    /**
     * 开启短信认证
     */
    private Boolean openSmsCertification;

    private Date loginOutTime;

    private Boolean invalid;


    /**
     * 用户上一次登录IP
     */
    private String lastLoginTerminalIp;

    /**
     * 用户上一次登录时间
     */
    private Date lastLoginTerminalTime;

    /**
     * 用户是否修改过密码
     */
    private Boolean isUserModifyPassword;

    /**
     * 是否被管理员重置密码，用户重置后若修改密码则改为False
     */
    private Boolean resetPasswordByAdmin = Boolean.FALSE;

    /**
     * 要求的授权类型
     */
    private String authMode;

    /**
     * 获得的授权类型
     */
    private String licenseType;

    /**
     * 授权持续类型
     */
    @Enumerated(EnumType.STRING)
    private CbbLicenseDurationEnum licenseDuration;

    public Boolean getOpenRadiusCertification() {
        return openRadiusCertification;
    }

    public void setOpenRadiusCertification(Boolean openRadiusCertification) {
        this.openRadiusCertification = openRadiusCertification;
    }


    /**
     * 构造初始IP信息
     * @return LoginInfoChangeDTO  变更信息
     */
    public LoginInfoChangeDTO buildLoginInfoChangeDTO() {
        LoginInfoChangeDTO loginInfoChangeDTO = new LoginInfoChangeDTO();
        loginInfoChangeDTO.setId(id);
        loginInfoChangeDTO.setLastLoginTerminalIp(lastLoginTerminalIp);
        loginInfoChangeDTO.setLastLoginTerminalTime(lastLoginTerminalTime);
        return loginInfoChangeDTO;
    }


    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public IacUserStateEnum getState() {
        return state;
    }

    public void setState(IacUserStateEnum state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Integer getDesktopNum() {
        return desktopNum;
    }

    public void setDesktopNum(Integer desktopNum) {
        this.desktopNum = desktopNum;
    }

    public IacAdUserAuthorityEnum getAdUserAuthority() {
        return adUserAuthority;
    }

    public void setAdUserAuthority(IacAdUserAuthorityEnum adUserAuthority) {
        this.adUserAuthority = adUserAuthority;
    }

    public Boolean getNeedUpdatePassword() {
        return needUpdatePassword;
    }

    public void setNeedUpdatePassword(Boolean needUpdatePassword) {
        this.needUpdatePassword = needUpdatePassword;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean isLock) {
        this.lock = isLock;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public Date getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(Date unlockTime) {
        this.unlockTime = unlockTime;
    }

    public Integer getPwdErrorTimes() {
        return pwdErrorTimes;
    }

    public void setPwdErrorTimes(Integer pwdErrorTimes) {
        this.pwdErrorTimes = pwdErrorTimes;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getUpdatePasswordTime() {
        return updatePasswordTime;
    }

    public void setUpdatePasswordTime(Date updatePasswordTime) {
        this.updatePasswordTime = updatePasswordTime;
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

    public Boolean getOpenHardwareCertification() {
        return openHardwareCertification;
    }

    public void setOpenHardwareCertification(Boolean openHardwareCertification) {
        this.openHardwareCertification = openHardwareCertification;
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

    public Boolean getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(Boolean openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
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

    public String getLastLoginTerminalIp() {
        return lastLoginTerminalIp;
    }

    public void setLastLoginTerminalIp(String lastLoginTerminalIp) {
        this.lastLoginTerminalIp = lastLoginTerminalIp;
    }

    public Date getLastLoginTerminalTime() {
        return lastLoginTerminalTime;
    }

    public void setLastLoginTerminalTime(Date lastLoginTerminalTime) {
        this.lastLoginTerminalTime = lastLoginTerminalTime;
    }

    public Boolean getUserModifyPassword() {
        return isUserModifyPassword;
    }

    public void setUserModifyPassword(Boolean userModifyPassword) {
        isUserModifyPassword = userModifyPassword;
    }

    public Boolean getResetPasswordByAdmin() {
        return resetPasswordByAdmin;
    }

    public void setResetPasswordByAdmin(Boolean resetPasswordByAdmin) {
        this.resetPasswordByAdmin = resetPasswordByAdmin;
    }

    public Boolean getOpenThirdPartyCertification() {
        return openThirdPartyCertification;
    }

    public void setOpenThirdPartyCertification(Boolean openThirdPartyCertification) {
        this.openThirdPartyCertification = openThirdPartyCertification;
    }

    public Integer getMaxHardwareNum() {
        return maxHardwareNum;
    }

    public void setMaxHardwareNum(Integer maxHardwareNum) {
        this.maxHardwareNum = maxHardwareNum;
    }

    public String getAuthMode() {
        return authMode;
    }

    public void setAuthMode(String authMode) {
        this.authMode = authMode;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public CbbLicenseDurationEnum getLicenseDuration() {
        return licenseDuration;
    }

    public void setLicenseDuration(CbbLicenseDurationEnum licenseDuration) {
        this.licenseDuration = licenseDuration;
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }
}
