package com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import org.springframework.lang.Nullable;

/**
 *
 * Description: RcoDispatcherRequest
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
public class RcoDispatcherRequest {

    @NotBlank
    private String dispatcherKey;

    @NotBlank
    private String terminalId;

    @Nullable
    private String requestId;

    @Nullable
    private String data;

    @Nullable
    private Boolean isNewConnection;

    /**
     * 客户端的IP，
     */
    @Nullable
    private String ip;

    public String getDispatcherKey() {
        return dispatcherKey;
    }

    public void setDispatcherKey(String dispatcherKey) {
        this.dispatcherKey = dispatcherKey;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @Nullable
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(@Nullable String requestId) {
        this.requestId = requestId;
    }

    @Nullable
    public String getData() {
        return data;
    }

    public void setData(@Nullable String data) {
        this.data = data;
    }

    @Nullable
    public Boolean getIsNewConnection() {
        return isNewConnection;
    }

    public void setIsNewConnection(@Nullable Boolean isNewConnection) {
        this.isNewConnection = isNewConnection;
    }

    @Nullable
    public String getIp() {
        return ip;
    }

    public void setIp(@Nullable String ip) {
        this.ip = ip;
    }
}
