package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserAuthorityEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/23
 *
 * @author tangxu
 */
public class PushUserInfoDTO {

    private UUID id;

    private UUID groupId;

    private String name;

    private String realName;

    private String password;

    private String phoneNum;

    private String email;

    private IacUserTypeEnum userType;

    private IacUserStateEnum state = IacUserStateEnum.ENABLE;

    private IacAdUserAuthorityEnum adUserAuthority;

    private Date createTime;

    private Date updateTime;

    /**
     * IDV云桌面关联镜像模板id
     */
    private UUID idvDesktopImageId;

    /**
     * IDV云桌面关联策略id
     */
    private UUID idvDesktopStrategyId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public IacAdUserAuthorityEnum getAdUserAuthority() {
        return adUserAuthority;
    }

    public void setAdUserAuthority(IacAdUserAuthorityEnum adUserAuthority) {
        this.adUserAuthority = adUserAuthority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
