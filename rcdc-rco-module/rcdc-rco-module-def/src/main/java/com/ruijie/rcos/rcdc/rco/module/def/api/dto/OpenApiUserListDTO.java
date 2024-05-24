package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/20 11:07
 *
 * @author liuwang1
 */
public class OpenApiUserListDTO {

    private UUID id;

    /**
     * 用户角色
     */
    private String userRole;

    private IacUserTypeEnum userType;

    private String realName;

    private String userDescription;

    private String userName;

    private UUID groupId;

    private String groupName;

    private String[] groupNameArr;

    private IacUserStateEnum userState;

    private Date createTime;

    /**
     * 邮箱信息
     */
    private String email;

    /** 绑定的云桌面数量 */
    private Integer desktopNum;

    /** 是否可删除 */
    private Boolean canDelete;

    /** 回收站是否有桌面 */
    private Boolean hasRecycleBin;

    /** 是否锁定 */
    private Boolean lock;

    /** 锁定时间 */
    private Date lockTime;

    /** 解锁时间 */
    private Date unlockTime;

    /** 开启口令认证 */
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
     * 开启企业微信
     */
    private Boolean openWorkWeixinCertification;

    /**
     * 开启飞书
     */
    private Boolean openFeishuCertification;

    /**
     * 开启钉钉
     */
    private Boolean openDingdingCertification;

    /**
     * 开启Oauth2
     */
    private Boolean openOauth2Certification;

    /**
     * 开启锐捷客户端扫码
     */
    private Boolean openRjclientCertification;

    /**
     * 是否开启硬件特征码
     */
    private Boolean openHardwareCertification;

    /**
     * 开启短信认证
     */
    private Boolean openSmsCertification;

    /**
     * 账户过期时间
     */
    private String accountExpireDateStr;

    /**
     * 是否开启域同步
     */
    private Boolean enableDomainSync;

    /**
     * 是否绑定磁盘
     */
    private Boolean hasBindDisk = false;

    private Integer invalidTime;

    private Boolean isInvalid;

    private String accountExpireDate;

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Integer getDesktopNum() {
        return desktopNum;
    }

    public void setDesktopNum(Integer desktopNum) {
        this.desktopNum = desktopNum;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public IacUserStateEnum getUserState() {
        return userState;
    }

    public void setUserState(IacUserStateEnum userState) {
        this.userState = userState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String[] getGroupNameArr() {
        return groupNameArr;
    }

    public void setGroupNameArr(String[] groupNameArr) {
        this.groupNameArr = groupNameArr;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public Date getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(Date unlockTime) {
        this.unlockTime = unlockTime;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
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

    public Boolean getOpenWorkWeixinCertification() {
        return openWorkWeixinCertification;
    }

    public void setOpenWorkWeixinCertification(Boolean openWorkWeixinCertification) {
        this.openWorkWeixinCertification = openWorkWeixinCertification;
    }

    public Boolean getOpenFeishuCertification() {
        return openFeishuCertification;
    }

    public void setOpenFeishuCertification(Boolean openFeishuCertification) {
        this.openFeishuCertification = openFeishuCertification;
    }

    public Boolean getOpenDingdingCertification() {
        return openDingdingCertification;
    }

    public void setOpenDingdingCertification(Boolean openDingdingCertification) {
        this.openDingdingCertification = openDingdingCertification;
    }

    public Boolean getOpenOauth2Certification() {
        return openOauth2Certification;
    }

    public void setOpenOauth2Certification(Boolean openOauth2Certification) {
        this.openOauth2Certification = openOauth2Certification;
    }

    public Boolean getOpenRjclientCertification() {
        return openRjclientCertification;
    }

    public void setOpenRjclientCertification(Boolean openRjclientCertification) {
        this.openRjclientCertification = openRjclientCertification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getOpenHardwareCertification() {
        return openHardwareCertification;
    }

    public void setOpenHardwareCertification(Boolean openHardwareCertification) {
        this.openHardwareCertification = openHardwareCertification;
    }

    public Boolean getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(Boolean openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
    }

    public String getAccountExpireDateStr() {
        return accountExpireDateStr;
    }

    public void setAccountExpireDateStr(String accountExpireDateStr) {
        this.accountExpireDateStr = accountExpireDateStr;
    }

    public Boolean getEnableDomainSync() {
        return enableDomainSync;
    }

    public void setEnableDomainSync(Boolean enableDomainSync) {
        this.enableDomainSync = enableDomainSync;
    }

    public Boolean getHasBindDisk() {
        return hasBindDisk;
    }

    public void setHasBindDisk(Boolean hasBindDisk) {
        this.hasBindDisk = hasBindDisk;
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

    public Boolean getInvalid() {
        return isInvalid;
    }

    public void setInvalid(Boolean invalid) {
        isInvalid = invalid;
    }

    public String getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(String accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }
}
