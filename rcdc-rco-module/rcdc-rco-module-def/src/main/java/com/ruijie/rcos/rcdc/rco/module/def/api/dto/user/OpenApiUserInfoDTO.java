package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;


import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/20 10:36
 *
 * @author liuwang1
 */
public class OpenApiUserInfoDTO {


    private UUID uuid;

    private String userName;

    private String displayName;

    private UUID userGroupId;

    private IacUserStateEnum state;

    private IacUserTypeEnum userType;

    private String accountExpireDate;

    private Integer invalidTime;

    private String userDescription;

    /**
     * 是否失效
     */
    private Boolean invalid;

    /**
     * 主要认证策略
     */
    private PrimaryCertification primaryCertification;

    /** 辅助认证 */
    private AssistCertification assistCertification;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public IacUserStateEnum getState() {
        return state;
    }

    public void setState(IacUserStateEnum state) {
        this.state = state;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
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

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public Boolean getInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
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
}
