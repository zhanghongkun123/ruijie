package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * Description: 用户类型请求
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/6
 *
 * @author lintingling
 */
public class UserTypeRequest implements Request {

    @NotNull
    private IacUserTypeEnum userType;

    public UserTypeRequest(IacUserTypeEnum userType) {
        this.userType = userType;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }
}
