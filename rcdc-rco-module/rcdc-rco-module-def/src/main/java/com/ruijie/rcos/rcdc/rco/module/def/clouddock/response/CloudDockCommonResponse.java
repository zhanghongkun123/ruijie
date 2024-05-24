package com.ruijie.rcos.rcdc.rco.module.def.clouddock.response;

import com.ruijie.rcos.sk.webmvc.api.response.WebResponse;

/**
 * Description: 云坞基础响应信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/6/28
 *
 * @param <T> 响应数据
 * @author chenjuan
 */
public class CloudDockCommonResponse<T> {

    private WebResponse.Status status;

    private T content;

    public WebResponse.Status getStatus() {
        return status;
    }

    public void setStatus(WebResponse.Status status) {
        this.status = status;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
