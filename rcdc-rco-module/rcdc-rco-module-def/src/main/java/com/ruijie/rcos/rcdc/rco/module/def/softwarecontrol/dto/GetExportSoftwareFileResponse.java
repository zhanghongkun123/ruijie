package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

import java.io.File;

/**
 * 
 * Description: 获取导出软件信息文件信息回应类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/16
 *
 * @author lihengjing
 */
public class GetExportSoftwareFileResponse extends DefaultResponse {

    private File exportFile;

    public GetExportSoftwareFileResponse(File exportFile) {
        this.exportFile = exportFile;
    }

    public GetExportSoftwareFileResponse()  { }

    public File getExportFile() {
        return exportFile;
    }

    public void setExportFile(File exportFile) {
        this.exportFile = exportFile;
    }
}
