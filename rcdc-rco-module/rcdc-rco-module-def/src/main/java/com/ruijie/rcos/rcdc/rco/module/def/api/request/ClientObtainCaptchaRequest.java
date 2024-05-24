package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.enums.ClientType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;


/**
 * Description: 客户端获取图形验证码请求
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:50
 *
 * @author wanglianyun
 */
public class ClientObtainCaptchaRequest {

    @NotNull
    private ClientType source;

    @Nullable
    private String invalidateCode;

    @Nullable
    private Long timestamp;

    public ClientType getSource() {
        return source;
    }

    public void setSource(ClientType source) {
        this.source = source;
    }

    @Nullable
    public String getInvalidateCode() {
        return invalidateCode;
    }

    public void setInvalidateCode(@Nullable String invalidateCode) {
        this.invalidateCode = invalidateCode;
    }

    @Nullable
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@Nullable Long timestamp) {
        this.timestamp = timestamp;
    }
}
