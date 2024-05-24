package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 远程协助信息单元
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/23
 *
 * @author ketb
 */
public class RemoteAssistInfoDTO {

    @NotNull
    private UUID deskId;

    @Nullable
    private UUID adminId;

    @Nullable
    private String adminName;

    @Nullable
    private RemoteAssistState state;

    @Nullable
    private String desktopName;

    @Nullable
    private String assistIp;

    @Nullable
    private Integer assistPort;

    @Nullable
    private String assistToken;

    @Nullable
    private String password;

    @Nullable
    private RemoteAssistState oldState;

    @Nullable
    private String serverAddr;

    @Nullable
    private Integer serverPort;

    public RemoteAssistInfoDTO() {

    }

    public RemoteAssistInfoDTO(UUID deskId, UUID adminId, String adminName) {
        this.deskId = deskId;
        this.adminId = adminId;
        this.adminName = adminName;
    }

    public RemoteAssistState getOldState() {
        return oldState;
    }

    public void setOldState(RemoteAssistState oldState) {
        this.oldState = oldState;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public RemoteAssistState getState() {
        return state;
    }

    public void setState(RemoteAssistState state) {
        this.state = state;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getAssistIp() {
        return assistIp;
    }

    public void setAssistIp(String assistIp) {
        this.assistIp = assistIp;
    }

    public Integer getAssistPort() {
        return assistPort;
    }

    public void setAssistPort(Integer assistPort) {
        this.assistPort = assistPort;
    }

    public String getAssistToken() {
        return assistToken;
    }

    public void setAssistToken(String assistToken) {
        this.assistToken = assistToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Nullable
    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(@Nullable String serverAddr) {
        this.serverAddr = serverAddr;
    }

    @Nullable
    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(@Nullable Integer serverPort) {
        this.serverPort = serverPort;
    }
}
