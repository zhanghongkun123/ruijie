package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import java.util.UUID;

/**
 * Description: cms docking rest response
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/01/16
 *
 * @author ljm
 */
public class RandomTokenResponse extends DefaultResponse {

    private UUID token;

    public RandomTokenResponse(UUID token) {
        this.token = token;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }
}
