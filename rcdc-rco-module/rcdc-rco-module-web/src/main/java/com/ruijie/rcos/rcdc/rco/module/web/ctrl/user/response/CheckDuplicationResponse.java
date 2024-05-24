package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/17
 *
 * @author Jarman
 */
public class CheckDuplicationResponse extends DefaultResponse {

    private boolean hasDuplication;

    public CheckDuplicationResponse(boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }

    public boolean isHasDuplication() {
        return hasDuplication;
    }

    public void setHasDuplication(boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }

}
