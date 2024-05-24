package com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.request;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 *
 * Description: 获取用户桌面列表openApi请求参数
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
public class UserDesktopQueryRequest {

    @NotNull
    private String userName;

    @NotNull
    private String terminalId;

    @NotNull
    private String terminalOsType;

    @NotNull
    private String cpuArch;

    @NotNull
    private Date requestTime;

    @NotNull
    private UUID clusterId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalOsType() {
        return terminalOsType;
    }

    public void setTerminalOsType(String terminalOsType) {
        this.terminalOsType = terminalOsType;
    }

    public String getCpuArch() {
        return cpuArch;
    }

    public void setCpuArch(String cpuArch) {
        this.cpuArch = cpuArch;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }
}
