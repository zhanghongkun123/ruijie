package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.AppTerminalDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse;

/**
 * Description: 应用客户端列表响应
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/16 15:29
 *
 * @author zhangyichi
 */
public class AppTerminalListResponse {

    private WebResponse.Status status;

    private DefaultPageResponse<AppTerminalDTO> content;

    public WebResponse.Status getStatus() {
        return status;
    }

    public void setStatus(WebResponse.Status status) {
        this.status = status;
    }

    public DefaultPageResponse<AppTerminalDTO> getContent() {
        return content;
    }

    public void setContent(DefaultPageResponse<AppTerminalDTO> content) {
        this.content = content;
    }
}
