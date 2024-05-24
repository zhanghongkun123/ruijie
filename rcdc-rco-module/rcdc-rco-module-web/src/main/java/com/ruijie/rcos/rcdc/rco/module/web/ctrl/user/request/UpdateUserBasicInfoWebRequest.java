package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo.AssistCertificationVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo.PrimaryCertificationVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.IdvDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.VoiDesktopConfigVO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 更新用户级别信息提交的数据对象
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/12
 *
 * @author Jarman
 */
@ApiModel("编辑用户请求体")
public class UpdateUserBasicInfoWebRequest implements WebRequest {

    @ApiModelProperty(value = "用户ID",required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "用户名")
    @Nullable
    private String userName;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    @TextShort
    private String realName;

    @ApiModelProperty(value = "用户组ID",required = true)
    @NotNull
    private IdLabelEntry userGroup;

    @ApiModelProperty(value = "手机号")
    @TextShort
    @Nullable
    private String phoneNum;

    @ApiModelProperty(value = "邮箱")
    @Nullable
    private String email;

    @ApiModelProperty(value = "用户描述")
    @Nullable
    @Size(max = 128)
    private String userDescription;

    @ApiModelProperty(value = "用户类型NORMAL/VISITOR/AD/LDAP",required = true)
    @NotNull
    private IacUserTypeEnum userType;

    @ApiModelProperty(value = "用户状态ENABLE/DISABLE", required = true)
    @Nullable
    private IacUserStateEnum state;

    @ApiModelProperty(value = "IDV云桌面")
    @Nullable
    private IdvDesktopConfigVO idvDesktopConfig;

    @ApiModelProperty(value = "VOI云桌面")
    @Nullable
    private VoiDesktopConfigVO voiDesktopConfig;

    /** 登录权限等级，访客为空 */
    @ApiModelProperty(value = "用户等级AUTO_LOGIN/MANUAL_LOGIN")
    @Nullable
    private IacUserLoginIdentityLevelEnum loginIdentityLevel;

    /**
     * 主要认证策略
     */
    @ApiModelProperty(value = "主要认证策略")
    @NotNull
    private PrimaryCertificationVO primaryCertificationVO;

    /** 辅助认证 */
    @ApiModelProperty(value = "辅助认证")
    @Nullable
    private AssistCertificationVO assistCertification;

    @ApiModelProperty("到期时间")
    @Nullable
    private Long accountExpireDate;

    @ApiModelProperty("失效天数")
    @Nullable
    private Integer invalidTime;

    @ApiModelProperty("失效恢复")
    @Nullable
    private Date invalidRecoverTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Nullable
    public String getUserName() {
        return userName;
    }

    public void setUserName(@Nullable String userName) {
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

    public Date getInvalidRecoverTime() {
        return invalidRecoverTime;
    }

    public void setInvalidRecoverTime(Date invalidRecoverTime) {
        this.invalidRecoverTime = invalidRecoverTime;
    }
}
