package com.ruijie.rcos.rcdc.rco.module.impl.connector.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * Description: 校验用户动态口令响应
 * Copyright: Copyright (c) 2022
 * Company: RuiJie Co., Ltd.
 * Create Time: 2022/4/18 4:24 下午
 *
 * @author chenli
 */
public class GetUserOtpConfigDTO {


    @NotBlank
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
