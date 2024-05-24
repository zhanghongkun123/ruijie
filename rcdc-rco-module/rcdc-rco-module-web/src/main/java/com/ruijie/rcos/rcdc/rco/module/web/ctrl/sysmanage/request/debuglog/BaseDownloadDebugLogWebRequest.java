package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.debuglog;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.DownloadWebRequest;

/**
 * Description: 下载调试日志web请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月19日
 *
 * @author fyq
 */
public class BaseDownloadDebugLogWebRequest implements DownloadWebRequest {

    @NotNull
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
