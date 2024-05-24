package com.ruijie.rcos.rcdc.rco.module.impl.enums;


/**
 * Description: 终端硬件配置操作枚举
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年10月19日
 *
 * @author clone
 */
public enum TerminalConfigOperateEnum {
    UPDATE("update"),
    ADD("add"),
    DELETE("delete");

    private String operate;
    TerminalConfigOperateEnum(String operate) {
        this.operate = operate;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}
