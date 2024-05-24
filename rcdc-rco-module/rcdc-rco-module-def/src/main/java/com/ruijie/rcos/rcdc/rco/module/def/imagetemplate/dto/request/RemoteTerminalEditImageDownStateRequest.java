package com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.request;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: 远程编辑镜像查询下载状态请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年06月19日
 *
 * @author xgx
 */

public class RemoteTerminalEditImageDownStateRequest implements WebRequest {

    @NotNull
    private UUID imageTemplateId;

    @NotNull
    private String terminalId;

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
}
