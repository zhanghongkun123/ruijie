package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextName;
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
public class CreateUserRequest implements Request {

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
    @TextShort
    private String realName;

    @NotNull
    private UUID groupId;

    @Nullable
    @TextShort
    private String phoneNum;

    @Nullable
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private IacUserTypeEnum userType;

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

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
    }

    @Nullable
    public UUID getIdvDesktopImageId() {
        return idvDesktopImageId;
    }

    public void setIdvDesktopImageId(@Nullable UUID idvDesktopImageId) {
        this.idvDesktopImageId = idvDesktopImageId;
    }

    @Nullable
    public UUID getIdvDesktopStrategyId() {
        return idvDesktopStrategyId;
    }

    public void setIdvDesktopStrategyId(@Nullable UUID idvDesktopStrategyId) {
        this.idvDesktopStrategyId = idvDesktopStrategyId;
    }
}

