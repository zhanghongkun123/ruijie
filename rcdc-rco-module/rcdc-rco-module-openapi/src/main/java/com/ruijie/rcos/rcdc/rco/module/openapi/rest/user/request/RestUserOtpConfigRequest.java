package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;

/**
 * Description: 用户动态口令配置Rest请求体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/4
 *
 * @author WuShengQiang
 */
public class RestUserOtpConfigRequest {

    /**
     * 用户名
     */
    @NotBlank
    @TextShort
    @TextName
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
