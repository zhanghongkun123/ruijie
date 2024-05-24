package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * 
 * Description: 追加终端刷机请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年2月14日
 * 
 * @author nt
 */
public class AppendTerminalSystemUpgradeWebRequest implements WebRequest {

    /**
     * 终端id数组
     */
    @NotNull
    private String[] terminalIdArr;

    /**
     *  终端分组id数组
     */
    @Nullable
    private UUID[] terminalGroupIdArr;

    /**
     * 终端刷机任务id
     */
    @NotNull
    private UUID upgradeTaskId;

    public String[] getTerminalIdArr() {
        return terminalIdArr;
    }

    public void setTerminalIdArr(String[] terminalIdArr) {
        this.terminalIdArr = terminalIdArr;
    }

    @Nullable
    public UUID[] getTerminalGroupIdArr() {
        return terminalGroupIdArr;
    }

    public void setTerminalGroupIdArr(@Nullable UUID[] terminalGroupIdArr) {
        this.terminalGroupIdArr = terminalGroupIdArr;
    }

    public UUID getUpgradeTaskId() {
        return upgradeTaskId;
    }

    public void setUpgradeTaskId(UUID upgradeTaskId) {
        this.upgradeTaskId = upgradeTaskId;
    }

}
