package com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import java.util.Date;
import java.util.UUID;

/**
 * 用户动态口令关联信息DTO
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月19日
 *
 * @author lihengjing
 */
public class RcoViewUserOtpCertificationDTO extends EqualsHashcodeSupport {

    /**
     * 记录ID
     */
    private UUID id;

    /**
     * 用户ID
     */
    private UUID userId;

    /**
     * 用户名称
     */
    private String userName;

    private IacUserTypeEnum userType;

    private String realName;

    private IacUserStateEnum userState;

    /**
     * 用户组ID
     */
    private UUID userGroupId;


    /**
     * 用户组名称
     */
    private String userGroupName;

    /**
     * 是否开启用戶动态口令
     */
    private Boolean openOtpCertification;

    /**
     * 是否完成绑定密钥
     */
    private Boolean hasBindOtp;

    /**
     * 更新时间
     */
    private Date updateTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public IacUserStateEnum getUserState() {
        return userState;
    }

    public void setUserState(IacUserStateEnum userState) {
        this.userState = userState;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
