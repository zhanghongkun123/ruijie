package com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws;

import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年01月25日
 *
 * @author xgx
 */
public class CbbConfirmQrCodeMobileReqDTO extends CbbQrCodeMobileReqDTO {
    @Nullable
    private String userData;

    @Nullable
    public String getUserData() {
        return userData;
    }

    public void setUserData(@Nullable String userData) {
        this.userData = userData;
    }
}
