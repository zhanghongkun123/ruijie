package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

import java.util.UUID;

/**
 * Description: 远程协助 response
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/7
 * 
 * @author ketb
 */
public class RemoteAssistInfoResponse extends DefaultResponse {

    private UUID deskId;

    private UUID adminId;

    private String adminName;

    private RemoteAssistState state;

    /**时间戳，状态变化时进行更新*/
    private long timeStamp = System.currentTimeMillis();

    private String assistIp;

    private int assistPort;

    private String assistToken;

    private String password;

    private int overtimeCount;

    private String desktopName;

    private RemoteAssistState oldState;

    public RemoteAssistState getOldState() {
        return oldState;
    }

    public void setOldState(RemoteAssistState oldState) {
        this.oldState = oldState;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public RemoteAssistState getState() {
        return state;
    }

    public void setState(RemoteAssistState state) {
        this.state = state;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAssistIp() {
        return assistIp;
    }

    public void setAssistIp(String assistIp) {
        this.assistIp = assistIp;
    }

    public int getAssistPort() {
        return assistPort;
    }

    public void setAssistPort(int assistPort) {
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

    public int getOvertimeCount() {
        return overtimeCount;
    }

    public void setOvertimeCount(int overtimeCount) {
        this.overtimeCount = overtimeCount;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

}
