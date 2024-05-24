package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserAuthorityEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.UserAppVmInfoDTO;

/**
 * Description: 应用组绑定用户信息DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 24/10/2022 下午 3:26
 *
 * @author gaoxueyuan
 */
public class RcaDynamicUserInfoDTO {

    private UUID id;

    /**
     * 用户角色
     */
    private String userRole;

    private IacUserTypeEnum userType;

    private String realName;

    private String userName;

    private UUID groupId;

    private String groupName;

    private String[] groupNameArr;

    private IacUserStateEnum userState;

    private Date createTime;

    private String email;

    private String phoneNum;

    private Integer desktopNum;

    /** 是否可删除 */
    private Boolean canDelete;

    /** 回收站是否有桌面 */
    private Boolean hasRecycleBin;

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
     * 开启外部CAS认证
     */
    private Boolean openCasCertification;

    /**
     * 开启账号密码认证
     */
    private Boolean openAccountPasswordCertification;

    /**
     * 是否开启硬件特征码
     */
    private Boolean openHardwareCertification;

    /**
     * 应用组Id
     */
    private UUID appGroupId;

    private String accountExpireDateStr;

    private Integer invalidTime;

    private Boolean isInvalid;

    private String invalidDescription;

    private String userDescription;

    private String accountExpireDate;

    public String[] getGroupNameArr() {
        return groupNameArr;
    }

    public void setGroupNameArr(String[] groupNameArr) {
        this.groupNameArr = groupNameArr;
    }

    public IacUserStateEnum getUserState() {
        return userState;
    }

    public void setUserState(IacUserStateEnum userState) {
        this.userState = userState;
    }

    public List<UserAppVmInfoDTO> getAppVmInfoList() {
        return appVmInfoList;
    }

    public void setAppVmInfoList(List<UserAppVmInfoDTO> appVmInfoList) {
        this.appVmInfoList = appVmInfoList;
    }

    private List<UserAppVmInfoDTO> appVmInfoList;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public Boolean getHasRecycleBin() {
        return hasRecycleBin;
    }

    public void setHasRecycleBin(Boolean hasRecycleBin) {
        this.hasRecycleBin = hasRecycleBin;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
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

    public void setLock(Boolean lock) {
        this.lock = lock;
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

    public UUID getAppGroupId() {
        return appGroupId;
    }

    public void setAppGroupId(UUID appGroupId) {
        this.appGroupId = appGroupId;
    }

    public String getAccountExpireDateStr() {
        return accountExpireDateStr;
    }

    public void setAccountExpireDateStr(String accountExpireDateStr) {
        this.accountExpireDateStr = accountExpireDateStr;
    }

    public Integer getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Integer invalidTime) {
        this.invalidTime = invalidTime;
    }

    public Boolean getInvalid() {
        return isInvalid;
    }

    public void setInvalid(Boolean invalid) {
        isInvalid = invalid;
    }

    public String getInvalidDescription() {
        return invalidDescription;
    }

    public void setInvalidDescription(String invalidDescription) {
        this.invalidDescription = invalidDescription;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(String accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }
}
