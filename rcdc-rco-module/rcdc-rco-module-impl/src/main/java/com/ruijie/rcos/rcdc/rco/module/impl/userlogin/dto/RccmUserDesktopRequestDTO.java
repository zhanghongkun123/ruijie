package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.UnifiedUserDesktopResultDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 *
 * Description: 向rccm查询用户VDI云桌面的请求参数
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
public class RccmUserDesktopRequestDTO {

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
    private String terminalId;

    @NotNull
    private String terminalOsType;

    @NotNull
    private String cpuArch;

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
}
