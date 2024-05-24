package com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.userlicense.request.UserSessionInfoRequest;
import org.springframework.lang.Nullable;


/**
 * Description: RccmManageRequest
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/9
 *
 * @author lihengjing
 */
public class WebClientRequest {

    /**
     * {UUID} clusterId RCCM下发分配给RCDC集群ID，用于辨识RCCM是否被重新部署
     */
    @Nullable
    private UUID clusterId;


    /**
     * 是否启用代理
     */
    @Nullable
    private Boolean enableProxy;

    /**
     * 终端标识
     */
    @Nullable
    private String terminalId;

    /**
     * userName
     */
    @Nullable
    private String userName;

    /**
     * 支持跨CPU厂商启动（异构场景）
     */
    @Nullable
    private Boolean supportCrossCpuVendor = false;

    /**
     * 终端IP
     */
    @Nullable
    private String terminalIp;

    /**
     * 网页版客户端接入集群ID
     */
    @Nullable
    private UUID accessClusterId;

    /**
     * 网页版客户端当前会话信息
     */
    @Nullable
    private UserSessionInfoRequest userSessionInfoRequest;

    @Nullable
    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(@Nullable UUID clusterId) {
        this.clusterId = clusterId;
    }

    @Nullable
    public Boolean getEnableProxy() {
        return enableProxy;
    }

    public void setEnableProxy(@Nullable Boolean enableProxy) {
        this.enableProxy = enableProxy;
    }

    @Nullable
    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(@Nullable String terminalId) {
        this.terminalId = terminalId;
    }

    @Nullable
    public String getUserName() {
        return userName;
    }

    public void setUserName(@Nullable String userName) {
        this.userName = userName;
    }

    @Nullable
    public Boolean getSupportCrossCpuVendor() {
        return supportCrossCpuVendor;
    }

    public void setSupportCrossCpuVendor(@Nullable Boolean supportCrossCpuVendor) {
        this.supportCrossCpuVendor = supportCrossCpuVendor;
    }

    @Nullable
    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(@Nullable String terminalIp) {
        this.terminalIp = terminalIp;
    }

    @Nullable
    public UUID getAccessClusterId() {
        return accessClusterId;
    }

    public void setAccessClusterId(@Nullable UUID accessClusterId) {
        this.accessClusterId = accessClusterId;
    }

    @Nullable
    public UserSessionInfoRequest getUserSessionInfoRequest() {
        return userSessionInfoRequest;
    }

    public void setUserSessionInfoRequest(@Nullable UserSessionInfoRequest userSessionInfoRequest) {
        this.userSessionInfoRequest = userSessionInfoRequest;
    }
}
