package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/7/28
 *
 * @author hs
 */
public class UserHasLoginResponse extends DefaultResponse {

    private boolean hasLogin;

    public UserHasLoginResponse(boolean hasLogin) {
        this.hasLogin = hasLogin;
    }

    public boolean isHasLogin() {
        return hasLogin;
    }

    public void setHasLogin(boolean hasLogin) {
        this.hasLogin = hasLogin;
    }
}
