package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.webmvc.api.request.DownloadWebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/4 13:01
 *
 * @author zhangyichi
 */
public class DownloadLogWebRequest implements DownloadWebRequest {

    @NotBlank
    private String logFileName;


    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

}
