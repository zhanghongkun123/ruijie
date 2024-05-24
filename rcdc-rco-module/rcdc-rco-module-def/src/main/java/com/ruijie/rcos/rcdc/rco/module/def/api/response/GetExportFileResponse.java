package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import java.io.File;

/**
 *
 * Description: 获取导出文件信息回应类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/04/13
 *
 * @author guoyongxin
 */
public class GetExportFileResponse {

    private File exportFile;

    public GetExportFileResponse(File exportFile) {
        this.exportFile = exportFile;
    }

    public GetExportFileResponse()  { }

    public File getExportFile() {
        return exportFile;
    }

    public void setExportFile(File exportFile) {
        this.exportFile = exportFile;
    }
}
