package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/28
 *
 * @author nt
 */
public class ObtainEnbaleOpenKmsResponse extends DefaultResponse {

    private Boolean enableOpenKms;

    public ObtainEnbaleOpenKmsResponse(Boolean enableOpenKms) {
        this.enableOpenKms = enableOpenKms;
    }

    public Boolean getEnableOpenKms() {
        return enableOpenKms;
    }

    public void setEnableOpenKms(Boolean enableOpenKms) {
        this.enableOpenKms = enableOpenKms;
    }
}
