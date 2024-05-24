package com.ruijie.rcos.rcdc.rco.module.impl.dto;

/**
 * Description: rcdc => shine 用户变更请求对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/9
 *
 * @author WuShengQiang
 */
public class BindUserRequest {

    private String oldUserName;

    private String userName;

    public String getOldUserName() {
        return oldUserName;
    }

    public void setOldUserName(String oldUserName) {
        this.oldUserName = oldUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
