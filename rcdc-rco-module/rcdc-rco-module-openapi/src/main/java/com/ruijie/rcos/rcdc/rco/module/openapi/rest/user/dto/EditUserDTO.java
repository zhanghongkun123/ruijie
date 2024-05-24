package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.dto;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.AssistCertification;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.PrimaryCertification;

/**
 * Description: openAPI编辑用户信息的DTO，用于补充未在EditUserRequest中开放但是编辑需要的参数
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/31 16:47
 *
 * @author zdc
 */
public class EditUserDTO {

    /**
     * 用户ID
     */
    private UUID userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 用户类型
     */
    private IacUserTypeEnum userType;

    /**
     * 用户组id
     */
    private UUID userGroupId;

    /**
     * 主要认证策略
     */
    private PrimaryCertification primaryCertification;

    /** 辅助认证 */
    private AssistCertification assistCertification;

    /** 登录权限等级，访客为空 */
    private IacUserLoginIdentityLevelEnum loginIdentityLevel;

    /** 用户描述 */
    private String description;

    /** 手机号 */
    private String phoneNum;

    /** 邮箱 */
    private String email;

    /** 过期时间 */
    private Long accountExpireDate;

    /** 失效天数 */
    private Integer invalidTime;

    /** 密码密钥 */
    private String confirmPwd;

    /** 密码 */
    private String password;

    /**
     * 失效恢复
     */
    private Date invalidRecoverTime;

    /**
     * 用户状态
     */
    private IacUserStateEnum state;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public PrimaryCertification getPrimaryCertification() {
        return primaryCertification;
    }

    public void setPrimaryCertification(PrimaryCertification primaryCertification) {
        this.primaryCertification = primaryCertification;
    }

    public AssistCertification getAssistCertification() {
        return assistCertification;
    }

    public void setAssistCertification(AssistCertification assistCertification) {
        this.assistCertification = assistCertification;
    }

    public IacUserLoginIdentityLevelEnum getLoginIdentityLevel() {
        return loginIdentityLevel;
    }

    public void setLoginIdentityLevel(IacUserLoginIdentityLevelEnum loginIdentityLevel) {
        this.loginIdentityLevel = loginIdentityLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(Long accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    public Integer getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Integer invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getInvalidRecoverTime() {
        return invalidRecoverTime;
    }

    public void setInvalidRecoverTime(Date invalidRecoverTime) {
        this.invalidRecoverTime = invalidRecoverTime;
    }

    public IacUserStateEnum getState() {
        return state;
    }

    public void setState(IacUserStateEnum state) {
        this.state = state;
    }
}