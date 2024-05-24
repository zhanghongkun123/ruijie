package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
public class TerminalTaskRequest {

    @NotNull
    private String terminalMac;

    public String getTerminalMac() {
        return terminalMac;
    }

    public void setTerminalMac(String terminalMac) {
        this.terminalMac = terminalMac;
    }
}
