package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/8/4
 *
 * @author hs
 */
public class CheckDuplicationDTO {

    private boolean hasDuplication;

    public CheckDuplicationDTO(boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }

    public boolean isHasDuplication() {
        return this.hasDuplication;
    }

    public void setHasDuplication(boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }
}
