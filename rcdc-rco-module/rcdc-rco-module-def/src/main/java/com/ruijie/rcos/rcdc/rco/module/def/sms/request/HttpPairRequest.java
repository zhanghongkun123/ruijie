package com.ruijie.rcos.rcdc.rco.module.def.sms.request;

import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.InputType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.base.annotation.TextShort;

/**
 * Description: Http协议参数请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/30
 *
 * @author TD
 */
public class HttpPairRequest {
    
    @NotBlank
    @TextShort
    private String key;

    @NotBlank
    @TextMedium
    private String value;
    
    @NotNull
    private InputType type;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public InputType getType() {
        return type;
    }

    public void setType(InputType type) {
        this.type = type;
    }
}
