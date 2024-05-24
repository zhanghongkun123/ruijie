package com.ruijie.rcos.rcdc.rco.module.impl.session;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/23
 *
 * @author Jarman
 */
public class UserInfo {

    private String terminalId;

    private UUID userId;

    private String userName;

    private IacUserTypeEnum userType;

    private LocalDateTime loginTime;

    public UserInfo() {

    }

    public UserInfo(String terminalId, String userName, IacUserTypeEnum userType) {
        Assert.hasText(terminalId, "terminalId cannot empty");
        Assert.hasText(userName, "userName cannot empty");
        Assert.notNull(userType, "userType cannot null");
        this.terminalId = terminalId;
        this.userName = userName;
        this.userType = userType;
        this.loginTime = LocalDateTime.now();
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
