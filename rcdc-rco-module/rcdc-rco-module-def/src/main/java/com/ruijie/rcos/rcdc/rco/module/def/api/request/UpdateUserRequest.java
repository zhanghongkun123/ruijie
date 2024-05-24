package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserAuthorityEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * 
 * Description: 用户页面请求公共属性
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 * 
 * @author chenzj
 */
public class UpdateUserRequest implements Request {

    @NotNull
    private UUID id;

    @Nullable
    private String userName;

    @Nullable
    private IacUserTypeEnum userType;

    /**
     * 姓名
     */
    @TextShort
    private String realName;

    @NotNull
    private UUID groupId;

    @Nullable
    @TextShort
    private String phoneNum;

    @Nullable
    private String email;

    @Nullable
    @Enumerated(EnumType.STRING)
    private IacAdUserAuthorityEnum adUserAuthority;

    @Nullable
    private IacUserStateEnum userState;

    /**
     * IDV云桌面关联镜像模板id
     */
    @Nullable
    private UUID idvDesktopImageId;

    /**
     * IDV云桌面关联策略id
     */
    @Nullable
    private UUID idvDesktopStrategyId;

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

    @Nullable
    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(@Nullable IacUserTypeEnum userType) {
        this.userType = userType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
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
    public IacAdUserAuthorityEnum getAdUserAuthority() {
        return adUserAuthority;
    }

    public void setAdUserAuthority(@Nullable IacAdUserAuthorityEnum adUserAuthority) {
        this.adUserAuthority = adUserAuthority;
    }

    @Nullable
    public IacUserStateEnum getUserState() {
        return userState;
    }

    public void setUserState(@Nullable IacUserStateEnum userState) {
        this.userState = userState;
    }

    public UUID getIdvDesktopImageId() {
        return idvDesktopImageId;
    }

    public void setIdvDesktopImageId(UUID idvDesktopImageId) {
        this.idvDesktopImageId = idvDesktopImageId;
    }

    public UUID getIdvDesktopStrategyId() {
        return idvDesktopStrategyId;
    }

    public void setIdvDesktopStrategyId(UUID idvDesktopStrategyId) {
        this.idvDesktopStrategyId = idvDesktopStrategyId;
    }
}

