package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.Date;
import java.util.UUID;

/**
 * Description: rcdc向rccm请求统一修改密码校验的请求参数
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/3/27
 *
 * @author ZouJian
 */
public class RccmUnifiedChangePwdRequest {

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

}
