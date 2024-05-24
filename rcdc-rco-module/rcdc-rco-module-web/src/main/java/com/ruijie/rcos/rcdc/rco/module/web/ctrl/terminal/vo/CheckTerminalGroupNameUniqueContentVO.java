package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/17
 *
 * @author nt
 */
public class CheckTerminalGroupNameUniqueContentVO {

    private Boolean hasDuplication;

    public CheckTerminalGroupNameUniqueContentVO(Boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }

    public Boolean getHasDuplication() {
        return hasDuplication;
    }

    public void setHasDuplication(Boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }
}
