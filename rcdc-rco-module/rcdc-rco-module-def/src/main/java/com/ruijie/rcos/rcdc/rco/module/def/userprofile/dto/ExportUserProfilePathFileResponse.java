package com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto;

import java.io.File;

/**
 * Description: 导出用户配置路径文件响应对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public class ExportUserProfilePathFileResponse {

    private File exportFile;

    public ExportUserProfilePathFileResponse(File exportFile) {
        this.exportFile = exportFile;
    }

    public File getExportFile() {
        return exportFile;
    }

    public void setExportFile(File exportFile) {
        this.exportFile = exportFile;
    }
}