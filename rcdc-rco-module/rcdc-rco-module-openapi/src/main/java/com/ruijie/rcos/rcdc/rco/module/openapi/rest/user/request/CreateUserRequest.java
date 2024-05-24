package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacImportUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.sk.base.annotation.CellPhone;
import com.ruijie.rcos.sk.base.annotation.Email;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;

/**
 * Description: 创建用户请求类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/30
 *
 * @author TD
 */
public class CreateUserRequest {

    /**
     * 用户类型
     */
    @NotNull
    private IacUserTypeEnum userType;

    /**
     * 用户名
     */
    @NotBlank
    @TextShort
    @TextName
    private String userName;

    /**
     * 姓名
     */
    @Nullable
    @TextShort
    private String realName;

    /**
     * 用户组名称，多个层级用/分隔
     */
    @NotNull
    private String userGroupName;

    @Nullable
    @TextShort
    @CellPhone
    private String phoneNum;

    @Nullable
    @Email
    private String email;

    /** 登录权限等级 */
    @Nullable
    private IacUserLoginIdentityLevelEnum loginIdentityLevel = IacUserLoginIdentityLevelEnum.AUTO_LOGIN;

    /**
     * 主要认证策略
     */
    @Nullable
    private PrimaryCertificationRequest primaryCertification;

    /** 辅助认证 */
    @Nullable
    private AssistCertificationRequest assistCertification;

    /**
     * 用户密码
     */
    @Nullable
    private String password;

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

    @Nullable
    private String description;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    @Nullable
    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(@Nullable String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public IacUserLoginIdentityLevelEnum getLoginIdentityLevel() {
        return loginIdentityLevel;
    }

    public void setLoginIdentityLevel(@Nullable IacUserLoginIdentityLevelEnum loginIdentityLevel) {
        this.loginIdentityLevel = loginIdentityLevel;
    }

    public PrimaryCertificationRequest getPrimaryCertification() {
        return primaryCertification;
    }

    public void setPrimaryCertification(PrimaryCertificationRequest primaryCertification) {
        this.primaryCertification = primaryCertification;
    }

    @Nullable
    public AssistCertificationRequest getAssistCertification() {
        return assistCertification;
    }

    public void setAssistCertification(@Nullable AssistCertificationRequest assistCertification) {
        this.assistCertification = assistCertification;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
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

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    /**
     * 构造导入用户对象
     * @return CbbImportUserDTO
     */
    public IacImportUserDTO buildCreateUserRequest() {
        IacImportUserDTO request = new IacImportUserDTO();
        request.setGroupNames(this.getUserGroupName());
        request.setUserName(this.getUserName());
        // rest api调用gss接口，需要加密处理
        if (StringUtils.isNotBlank(this.getPassword())) {
            String encPwd = AesUtil.encrypt(this.getPassword(), RedLineUtil.getRealUserRedLine());
            request.setPassWord(encPwd);
        }
        request.setUserType(this.getUserType());
        if (this.getUserType() != IacUserTypeEnum.VISITOR) {
            request.setRealName(this.getRealName());
            request.setPhoneNum(this.getPhoneNum());
            request.setEmail(this.email);
        }
        request.setShouldChangePassword(this.getUserType() != IacUserTypeEnum.VISITOR);
        long expireTime = this.accountExpireDate == null ? 0L : this.accountExpireDate;
        request.setAccountExpireDate(expireTime);
        request.setInvalidTime(this.invalidTime);
        request.setDescription(this.description);
        return request;
    }
}
