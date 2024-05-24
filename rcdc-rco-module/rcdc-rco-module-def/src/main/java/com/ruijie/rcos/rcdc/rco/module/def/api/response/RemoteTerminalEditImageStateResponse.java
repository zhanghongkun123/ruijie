package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: 远程终端编辑镜像：终端、镜像下载状态信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年7月1日
 *
 * @author ypp
 */
public class RemoteTerminalEditImageStateResponse extends DefaultResponse {

    /**
     * 终端状态
     */
    private CbbTerminalStateEnums terminalState;

    /**
     * 镜像下载状态
     */
    private boolean hasDownloadSuc;

    public CbbTerminalStateEnums getTerminalState() {
        return terminalState;
    }

    public void setTerminalState(CbbTerminalStateEnums terminalState) {
        this.terminalState = terminalState;
    }

    public boolean isHasDownloadSuc() {
        return hasDownloadSuc;
    }

    public void setHasDownloadSuc(boolean hasDownloadSuc) {
        this.hasDownloadSuc = hasDownloadSuc;
    }
}
