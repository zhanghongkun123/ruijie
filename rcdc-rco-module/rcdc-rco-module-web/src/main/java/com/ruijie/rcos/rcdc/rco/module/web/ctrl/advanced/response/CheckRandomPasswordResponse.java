package com.ruijie.rcos.rcdc.rco.module.web.ctrl.advanced.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/17
 *
 * @author Jarman
 */
public class CheckRandomPasswordResponse extends DefaultResponse {

    private Boolean randomPassword;

    public CheckRandomPasswordResponse(Boolean randomPassword) {
        this.randomPassword = randomPassword;
    }

    public Boolean getRandomPassword() {
        return randomPassword;
    }

    public void setRandomPassword(Boolean randomPassword) {
        this.randomPassword = randomPassword;
    }
}
