package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;

/**
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/3
 *
 * @author xiao'yong'deng
 */
public class AesPasswordKeyDTO {

    /**
     * 秘钥key
     */
    private String passwordKey;

    public String getPasswordKey() {
        return passwordKey;
    }

    public void setPasswordKey(String passwordKey) {
        this.passwordKey = passwordKey;
    }
}
