package com.ruijie.rcos.rcdc.rco.module.impl.spi.response;

import com.ruijie.rcos.rcdc.terminal.module.def.enums.TerminalAuthResultEnums;

/**
 * Description: 终端离线登录补充授权结果
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月15日
 *
 * @author ypp
 */
public class SupplementAuthResponse {

    private TerminalAuthResultEnums terminalAuthState;

    public TerminalAuthResultEnums getTerminalAuthState() {
        return terminalAuthState;
    }

    public void setTerminalAuthState(TerminalAuthResultEnums terminalAuthState) {
        this.terminalAuthState = terminalAuthState;
    }
}
