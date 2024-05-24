package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import javax.annotation.Nullable;

/**
 * Description: 终端硬件配置导入请求dto
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/16
 *
 * @author luocl
 */
public class TerminalConfigImportRequest implements WebRequest {

    @NotNull
    private String filePath;

    @NotNull
    private String fileName;

    @Nullable
    private  Object customData;


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Nullable
    public Object getCustomData() {
        return customData;
    }

    public void setCustomData(@Nullable Object customData) {
        this.customData = customData;
    }
}
