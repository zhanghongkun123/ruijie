package com.ruijie.rcos.rcdc.rco.module.impl.spi.struct;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * EstCommonActionRequest Est透传RCDC消息通道 请求DTO
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年3月31日
 * 
 * @author chenl
 */
public class EstCommonActionTcpRequest {

    @NotNull
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
