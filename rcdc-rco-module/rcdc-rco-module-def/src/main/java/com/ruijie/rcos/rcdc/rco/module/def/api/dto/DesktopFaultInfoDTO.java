package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import java.util.Date;
import java.util.UUID;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/25 0:45
 *
 * @author ketb
 */
public class DesktopFaultInfoDTO {

    @NotNull
    private UUID id;

    @NotNull
    private UUID deskId;

    @Nullable
    private String desktopName;

    @NotNull
    private String mac;

    private boolean faultState = false;

    @Nullable
    private String faultDescription;

    @Nullable
    private Date faultTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public boolean getFaultState() {
        return faultState;
    }

    public void setFaultState(boolean faultState) {
        this.faultState = faultState;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public void setFaultDescription(String faultDescription) {
        this.faultDescription = faultDescription;
    }

    @Nullable
    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(@Nullable String desktopName) {
        this.desktopName = desktopName;
    }

    @Nullable
    public Date getFaultTime() {
        return faultTime;
    }

    public void setFaultTime(@Nullable Date faultTime) {
        this.faultTime = faultTime;
    }
}
