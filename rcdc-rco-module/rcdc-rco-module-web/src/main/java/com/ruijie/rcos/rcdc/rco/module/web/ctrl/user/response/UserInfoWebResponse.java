package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserAuthorityEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/28
 *
 * @author ljm
 */
@ApiModel("用户信息返回体")
public class UserInfoWebResponse extends DefaultResponse {

    @ApiModelProperty(value = "用户ID")
    private UUID id;

    @ApiModelProperty(value = "用户类型")
    private IacUserTypeEnum userType;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "用户组ID")
    private UUID groupId;

    @ApiModelProperty(value = "用户角色")
    private String userRole;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户组名")
    private String groupName;

    @ApiModelProperty(value = "多个用户组")
    private String[] groupNameArr;

    @ApiModelProperty(value = "用户状态")
    private IacUserStateEnum userState;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号")
    private String phoneNum;

    @ApiModelProperty(value = "用户描述")
    private String userDescription;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "用户权限")
    private IacAdUserAuthorityEnum adUserAuthority;

    /**
     * 是否处于已登录终端状态
     */
    @ApiModelProperty(value = "登录终端状态")
    private boolean hasLogin;

    /**
     * 创建中云桌面数
     */
    @ApiModelProperty(value = "创建中云桌面数")
    private int creatingDesktopNum;

    /**
     * 已绑定的桌面数
     */
    @ApiModelProperty(value = "已绑定的桌面数")
    private long desktopNum;

    /**
     * 登录权限等级
     */
    @ApiModelProperty(value = "登录权限等级")
    private IacUserLoginIdentityLevelEnum loginIdentityLevel;

    @ApiModelProperty(value = "开启口令认证")
    private Boolean openOtpCertification;

    @ApiModelProperty(value = "动态口令绑定")
    private Boolean hasBindOtp;

    @ApiModelProperty(value = "开启外部CAS认证")
    private Boolean openCasCertification;

    @ApiModelProperty(value = "开启账号密码认证")
    private Boolean openAccountPasswordCertification;

    @ApiModelProperty(value = "开启第三方认证")
    private Boolean openThirdPartyCertification;

    @ApiModelProperty(value = "开启硬件特征码")
    private Boolean openHardwareCertification;

    /**
     * 账户过期时间
     */
    @ApiModelProperty(value = "账户过期时间")
    private String accountExpireDateStr;

    /**
     * 是否开启域同步
     */
    @ApiModelProperty(value = "是否开启域同步")
    private Boolean enableDomainSync;

    @ApiModelProperty(value = "云桌面临时权限名称")
    private String desktopTempPermissionName;
    
    @ApiModelProperty(value = "是否开启短信认证")
    private Boolean openSmsCertification;

    @ApiModelProperty(value = "过期时间")
    private String accountExpireDate;

    @ApiModelProperty(value = "失效天数")
    private Integer invalidTime;

    @ApiModelProperty(value = "是否失效")
    private Boolean isInvalid;

    @ApiModelProperty(value = "失效描述")
    private String invalidDescription;

    @ApiModelProperty(value = "是否开启Radius服务")
    private Boolean openRadiusCertification;

    @ApiModelProperty(value = "开启企业微信")
    private Boolean openWorkWeixinCertification;

    @ApiModelProperty(value = "开启飞书")
    private Boolean openFeishuCertification;

    @ApiModelProperty(value = "开启钉钉")
    private Boolean openDingdingCertification;

    @ApiModelProperty(value = "开启Oauth2")
    private Boolean openOauth2Certification;

    @ApiModelProperty(value = "开启锐捷客户端扫码")
    private Boolean openRjclientCertification;

    public Boolean getOpenRadiusCertification() {
        return openRadiusCertification;
    }

    public void setOpenRadiusCertification(Boolean openRadiusCertification) {
        this.openRadiusCertification = openRadiusCertification;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
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

    public IacAdUserAuthorityEnum getAdUserAuthority() {
        return adUserAuthority;
    }

    public void setAdUserAuthority(IacAdUserAuthorityEnum adUserAuthority) {
        this.adUserAuthority = adUserAuthority;
    }

    public boolean isHasLogin() {
        return hasLogin;
    }

    public void setHasLogin(boolean hasLogin) {
        this.hasLogin = hasLogin;
    }

    public int getCreatingDesktopNum() {
        return creatingDesktopNum;
    }

    public void setCreatingDesktopNum(int creatingDesktopNum) {
        this.creatingDesktopNum = creatingDesktopNum;
    }

    public long getDesktopNum() {
        return desktopNum;
    }

    public void setDesktopNum(long desktopNum) {
        this.desktopNum = desktopNum;
    }

    public IacUserLoginIdentityLevelEnum getLoginIdentityLevel() {
        return loginIdentityLevel;
    }

    public void setLoginIdentityLevel(IacUserLoginIdentityLevelEnum loginIdentityLevel) {
        this.loginIdentityLevel = loginIdentityLevel;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
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

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getDesktopTempPermissionName() {
        return desktopTempPermissionName;
    }

    public void setDesktopTempPermissionName(String desktopTempPermissionName) {
        this.desktopTempPermissionName = desktopTempPermissionName;
    }

    public String getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(String accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
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

}
