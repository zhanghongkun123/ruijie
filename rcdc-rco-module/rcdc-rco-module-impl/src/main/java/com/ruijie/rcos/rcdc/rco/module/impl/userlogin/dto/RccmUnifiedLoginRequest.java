package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.UUID;

/**
 *
 * Description: rcdc向rccm请求统一登录校验的请求参数
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
public class RccmUnifiedLoginRequest {

    @NotNull
    private String terminalId;

    /**
     * 节点ID
     */
    @NotNull
    private UUID clusterId;

    /**
     * 请求时间
     */
    @NotNull
    private Date requestTime;

    @NotNull
    private String userName;

    @NotNull
    private CbbDispatcherRequest dispatcherRequest;

    /**
     * 是否本地认证通过，
     */
    @Nullable
    private Boolean hasLocalAuth;


    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public CbbDispatcherRequest getDispatcherRequest() {
        return dispatcherRequest;
    }

    public void setDispatcherRequest(CbbDispatcherRequest dispatcherRequest) {
        this.dispatcherRequest = dispatcherRequest;
    }

    @Nullable
    public Boolean getHasLocalAuth() {
        return hasLocalAuth;
    }

    public void setHasLocalAuth(@Nullable Boolean hasLocalAuth) {
        this.hasLocalAuth = hasLocalAuth;
    }
}
