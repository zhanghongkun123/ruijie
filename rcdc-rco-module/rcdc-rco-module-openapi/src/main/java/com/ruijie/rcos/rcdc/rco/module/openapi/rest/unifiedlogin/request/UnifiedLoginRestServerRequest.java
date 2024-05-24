package com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.request;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.Date;
import java.util.UUID;

/**
 *
 * Description: rcdc统一登录openApi请求参数
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
public class UnifiedLoginRestServerRequest {

    @NotNull
    private Date requestTime;

    /**
     * 节点在集群管理系统中的ID
     */
    @NotNull
    private UUID clusterId;

    @NotNull
    private RcoDispatcherRequest dispatcherRequest;

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

    public RcoDispatcherRequest getDispatcherRequest() {
        return dispatcherRequest;
    }

    public void setDispatcherRequest(RcoDispatcherRequest dispatcherRequest) {
        this.dispatcherRequest = dispatcherRequest;
    }
}
