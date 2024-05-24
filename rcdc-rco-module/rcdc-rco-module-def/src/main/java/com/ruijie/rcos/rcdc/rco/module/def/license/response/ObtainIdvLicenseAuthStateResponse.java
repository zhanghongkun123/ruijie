package com.ruijie.rcos.rcdc.rco.module.def.license.response;

import com.ruijie.rcos.rcdc.rco.module.def.license.enums.LicenseAuthStateEnum;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/26 10:59
 *
 * @author ketb
 */
public class ObtainIdvLicenseAuthStateResponse {

    private LicenseAuthStateEnum authState;

    public LicenseAuthStateEnum getAuthState() {
        return authState;
    }

    public void setAuthState(LicenseAuthStateEnum authState) {
        this.authState = authState;
    }
}
