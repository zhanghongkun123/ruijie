package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月23日
 *
 * @author zouqi
 */
public class RcdcAuthorizedDetailWebResponse {

    private boolean isAuthorizationForTemporary;
    
    private boolean isAuthorizationForPerpetual;

    public boolean isAuthorizationForTemporary() {
        return isAuthorizationForTemporary;
    }

    public void setAuthorizationForTemporary(boolean isAuthorizationForTemporary) {
        this.isAuthorizationForTemporary = isAuthorizationForTemporary;
    }

    public boolean isAuthorizationForPerpetual() {
        return isAuthorizationForPerpetual;
    }

    public void setAuthorizationForPerpetual(boolean isAuthorizationForPerpetual) {
        this.isAuthorizationForPerpetual = isAuthorizationForPerpetual;
    }
}
