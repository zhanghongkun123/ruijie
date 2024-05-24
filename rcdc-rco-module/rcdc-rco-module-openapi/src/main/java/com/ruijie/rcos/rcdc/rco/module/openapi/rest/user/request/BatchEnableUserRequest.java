package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 批量启用用户请求
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/30 11:29
 *
 * @author zdc
 */
public class BatchEnableUserRequest {

    @NotNull
    private String[] userArr;

    public String[] getUserArr() {
        return userArr;
    }

    public void setUserArr(String[] userArr) {
        this.userArr = userArr;
    }
}