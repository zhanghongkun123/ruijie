package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.DownloadWebRequest;

import java.util.UUID;

/**
 * Description: 第三方应用主机日志收集后的下载
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月22日
 *
 * @author liuwc
 */
public class DownloadRcaHostLogWebRequest  implements DownloadWebRequest {

    @NotBlank
    private String logName;

    @NotNull
    private UUID hostId;

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }
}