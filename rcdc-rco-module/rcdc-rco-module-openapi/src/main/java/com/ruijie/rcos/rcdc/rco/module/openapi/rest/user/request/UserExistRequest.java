package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.List;

/**
 * Description: 用户是否存在请求参数
 *
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-02-09
 *
 * @author linke
 */
public class UserExistRequest {

    @NotNull
    private List<String> userNameList;

    public List<String> getUserNameList() {
        return userNameList;
    }

    public void setUserNameList(List<String> userNameList) {
        this.userNameList = userNameList;
    }
}
