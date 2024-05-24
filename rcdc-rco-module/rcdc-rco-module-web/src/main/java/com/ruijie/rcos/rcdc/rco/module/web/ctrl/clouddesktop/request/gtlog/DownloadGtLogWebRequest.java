package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.gtlog;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.DownloadWebRequest;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/4 13:01
 *
 * @author zhangyichi
 */
public class DownloadGtLogWebRequest implements DownloadWebRequest {

    @NotBlank
    private String logFileName;

    @NotNull
    private UUID deskId;

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
}
