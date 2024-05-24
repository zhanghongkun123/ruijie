package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

/**
 * Description: 终端查询桌面
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/15
 *
 * @author zqj
 */
public class QueryDesktopDTO {

    private boolean isNewTerminal;

    public boolean isNewTerminal() {
        return isNewTerminal;
    }

    public void setNewTerminal(boolean newTerminal) {
        isNewTerminal = newTerminal;
    }
}
