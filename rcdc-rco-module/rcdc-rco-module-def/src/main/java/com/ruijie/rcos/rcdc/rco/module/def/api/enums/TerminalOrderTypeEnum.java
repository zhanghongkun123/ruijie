package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/3
 *
 * @author songxiang
 */
public enum TerminalOrderTypeEnum {
    NORMAL(0),
    /**
     * 终端已经为该镜像成功安装过驱动
     */
    INSTALL_SUCCESS(1);

    private int priority;

    TerminalOrderTypeEnum(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }
}
