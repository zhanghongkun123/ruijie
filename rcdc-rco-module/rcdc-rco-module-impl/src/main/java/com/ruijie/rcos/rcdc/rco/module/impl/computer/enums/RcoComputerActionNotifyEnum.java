package com.ruijie.rcos.rcdc.rco.module.impl.computer.enums;

/**
 * Description: pc终端操作通知
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/17
 *
 * @author zqj
 */
public enum RcoComputerActionNotifyEnum {

    /**
     * 添加
     */
    ADD_COMPUTER("addComputer"),

    /**
     * 绑定用户
     */
    COMPUTER_BIND_USER("computerBindUser"),

    /**
     * 更新
     */
    UPDATE_COMPUTER("updateComputer"),

    /**
     * 移动分组
     */
    MOVE_COMPUTER("moveComputer");


    private final String action;

    public String getAction() {
        return action;
    }

    RcoComputerActionNotifyEnum(String action) {
        this.action = action;
    }
}
