package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 重复登录云桌面的终端信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/18
 *
 * @author nt
 */
public class RepeatStartVmWebclientNotifyDTO {

    @NotNull
    private UUID desktopId;

    @Nullable
    private String userName;

    @Nullable
    private String terminalName;

    @Nullable
    private String macAddr;

    @Nullable
    private String ip;

    @Nullable
    private Long startVmTime;

    @Nullable
    private String desktopName;

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    @Nullable
    public String getUserName() {
        return userName;
    }

    public void setUserName(@Nullable String userName) {
        this.userName = userName;
    }

    @Nullable
    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(@Nullable String terminalName) {
        this.terminalName = terminalName;
    }

    @Nullable
    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(@Nullable String macAddr) {
        this.macAddr = macAddr;
    }

    @Nullable
    public String getIp() {
        return ip;
    }

    public void setIp(@Nullable String ip) {
        this.ip = ip;
    }

    @Nullable
    public Long getStartVmTime() {
        return startVmTime;
    }

    public void setStartVmTime(@Nullable Long startVmTime) {
        this.startVmTime = startVmTime;
    }

    @Nullable
    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(@Nullable String desktopName) {
        this.desktopName = desktopName;
    }
}
