package com.ruijie.rcos.rcdc.rco.module.impl.connector.response;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/29
 *
 * @author zqj
 */
public class GetComputerInfoResponse {


    @NotNull
    private UUID terminalId;

    @Nullable
    private String platformId;

    @Nullable
    private UUID userId;

    @Nullable
    private UUID hostId;

    @Nullable
    private String userName;


    @Nullable
    private String poolModel;

    @Nullable
    private String workModel;

    @Nullable
    private CbbDesktopSessionType sessionType;

    public UUID getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(UUID terminalId) {
        this.terminalId = terminalId;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Nullable
    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(@Nullable UUID hostId) {
        this.hostId = hostId;
    }

    @Nullable
    public String getWorkModel() {
        return workModel;
    }

    public void setWorkModel(@Nullable String workModel) {
        this.workModel = workModel;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    @Nullable
    public String getPoolModel() {
        return poolModel;
    }

    public void setPoolModel(@Nullable String poolModel) {
        this.poolModel = poolModel;
    }
}
