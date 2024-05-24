package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: DesktopStateNumDTO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-08-08
 *
 * @author hli
 */
public class DesktopSoftwareNotifyDTO {

    @Nullable
    private UUID id;

    @Nullable
    private UUID cbbDesktopId;

    @NotNull
    private String terminalId;

    public DesktopSoftwareNotifyDTO(@Nullable UUID id, @Nullable UUID cbbDesktopId, String terminalId) {
        this.id = id;
        this.cbbDesktopId = cbbDesktopId;
        this.terminalId = terminalId;
    }

    @Nullable
    public UUID getCbbDesktopId() {
        return cbbDesktopId;
    }

    public void setCbbDesktopId(@Nullable UUID cbbDesktopId) {
        this.cbbDesktopId = cbbDesktopId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }
}
