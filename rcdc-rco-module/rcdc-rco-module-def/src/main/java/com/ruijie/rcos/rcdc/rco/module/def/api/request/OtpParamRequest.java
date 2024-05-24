package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.OtpUserType;
import com.ruijie.rcos.sk.base.annotation.IPv4Address;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time:  2024/4/22
 *
 * @author songxiang
 */
public class OtpParamRequest {

    @NotNull
    private OtpUserType userType;

    public OtpUserType getUserType() {
        return userType;
    }

    public void setUserType(OtpUserType userType) {
        this.userType = userType;
    }
}
