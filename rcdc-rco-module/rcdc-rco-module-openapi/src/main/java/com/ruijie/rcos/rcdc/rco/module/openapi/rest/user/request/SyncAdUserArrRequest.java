package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 增量同步Ad域用户请求参数
 *
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-02-09
 *
 * @author zqj
 */
public class SyncAdUserArrRequest {

    @NotNull
    private SyncAdUserRequest[] userInfoArr;

    public SyncAdUserRequest[] getUserInfoArr() {
        return userInfoArr;
    }

    public void setUserInfoArr(SyncAdUserRequest[] userInfoArr) {
        this.userInfoArr = userInfoArr;
    }
}
