package com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.response;

import java.io.File;

/**
 * Description: 获取导出表格响应体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/2
 *
 * @author WuShengQiang
 */
public class GetExportExcelResponse {

    private File exportFile;

    public GetExportExcelResponse(File exportFile) {
        this.exportFile = exportFile;
    }

    public GetExportExcelResponse() {
    }

    public File getExportFile() {
        return exportFile;
    }

    public void setExportFile(File exportFile) {
        this.exportFile = exportFile;
    }
}
