package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import com.ruijie.rcos.sk.base.annotation.*;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo.AssistCertificationVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo.PrimaryCertificationVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.IdvDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.VoiDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.VdiDesktopConfigVO;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 创建用户提交的数据对象
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/12
 *
 * @author Jarman
 */
@ApiModel("创建用户请求体")
public class CreateUserWebRequest implements WebRequest {

    /**
     * 用户类型
     */
    @NotNull
    @ApiModelProperty(value = "用户类型", required = true)
    private IacUserTypeEnum userType;

    /**
     * 用户状态
     */
    @Nullable
    @ApiModelProperty(value = "用户状态", required = true)
    private IacUserStateEnum state;

    /**
     * 用户名
     */
    @NotBlank
    @TextShort
    @StrictTextName
    @ApiModelProperty(value = "用户名称", required = true)
    private String userName;

    /**
     * 姓名
     */
    @TextShort
    @ApiModelProperty(value = "姓名")
    private String realName;

    @NotNull
    @ApiModelProperty(value = "用户组",required = true)
    private IdLabelEntry userGroup;

    @Nullable
    @TextShort
    @ApiModelProperty(value = "手机号码")
    private String phoneNum;

    @Nullable
    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotBlank
    @ApiModelProperty(value = "密码")
    private String password;

    @NotBlank
    @ApiModelProperty(value = "确认密码")
    private String confirmPwd;

    /**
     * 用户描述
     */
    @Nullable
    @Size(max = 128)
    @ApiModelProperty(value = "用户描述")
    private String userDescription;

    @Nullable
    @ApiModelProperty(value = "VDI桌面")
    private VdiDesktopConfigVO vdiDesktopConfig;

    @Nullable
    @ApiModelProperty(value = "IDV桌面")
    private IdvDesktopConfigVO idvDesktopConfig;

    @Nullable
    @ApiModelProperty(value = "VOI桌面")
    private VoiDesktopConfigVO voiDesktopConfig;

    /** 登录权限等级 */
    @Nullable
    @ApiModelProperty(value = "登录权限等级")
    private IacUserLoginIdentityLevelEnum loginIdentityLevel;

    /**
     * 主要认证策略
     */
    @NotNull
    @ApiModelProperty(value = "主要认证策略")
    private PrimaryCertificationVO primaryCertificationVO;

    /** 辅助认证 */
    @Nullable
    @ApiModelProperty(value = "辅助认证")
    private AssistCertificationVO assistCertification;

    /**
     * 随机密码
     */
    @NotNull
    @ApiModelProperty(value = "随机密码")
    private Boolean passwordRandom;

    @Nullable
    @ApiModelProperty("过期时间")
    private Long accountExpireDate;

    @Nullable
    @ApiModelProperty("失效天数")
    private Integer invalidTime;

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

    public IdLabelEntry getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(IdLabelEntry userGroup) {
        this.userGroup = userGroup;
    }

    @Nullable
    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(@Nullable String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public VdiDesktopConfigVO getVdiDesktopConfig() {
        return vdiDesktopConfig;
    }

    public void setVdiDesktopConfig(VdiDesktopConfigVO vdiDesktopConfig) {
        this.vdiDesktopConfig = vdiDesktopConfig;
    }

    @Nullable
    public IdvDesktopConfigVO getIdvDesktopConfig() {
        return idvDesktopConfig;
    }

    public void setIdvDesktopConfig(@Nullable IdvDesktopConfigVO idvDesktopConfig) {
        this.idvDesktopConfig = idvDesktopConfig;
    }

    public IacUserLoginIdentityLevelEnum getLoginIdentityLevel() {
        return loginIdentityLevel;
    }

    public void setLoginIdentityLevel(IacUserLoginIdentityLevelEnum loginIdentityLevel) {
        this.loginIdentityLevel = loginIdentityLevel;
    }

    @Nullable
    public VoiDesktopConfigVO getVoiDesktopConfig() {
        return voiDesktopConfig;
    }

    public void setVoiDesktopConfig(@Nullable VoiDesktopConfigVO voiDesktopConfig) {
        this.voiDesktopConfig = voiDesktopConfig;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }

    @Nullable
    public AssistCertificationVO getAssistCertification() {
        return assistCertification;
    }

    public void setAssistCertification(@Nullable AssistCertificationVO assistCertification) {
        this.assistCertification = assistCertification;
    }

    public PrimaryCertificationVO getPrimaryCertificationVO() {
        return primaryCertificationVO;
    }

    public void setPrimaryCertificationVO(PrimaryCertificationVO primaryCertificationVO) {
        this.primaryCertificationVO = primaryCertificationVO;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public Boolean getPasswordRandom() {
        return passwordRandom;
    }

    public void setPasswordRandom(Boolean passwordRandom) {
        this.passwordRandom = passwordRandom;
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
}
