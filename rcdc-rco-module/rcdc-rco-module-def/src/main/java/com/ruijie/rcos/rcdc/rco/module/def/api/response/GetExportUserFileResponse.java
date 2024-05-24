package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

import java.io.File;

/**
 * 
 * Description: 获取导出用户信息回应类
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/27
 *
 * @author tangxu
 */
public class GetExportUserFileResponse extends DefaultResponse {

    private File exportFile;

    public GetExportUserFileResponse(File exportFile) {
        this.exportFile = exportFile;
    }

    public File getExportFile() {
        return exportFile;
    }

    public void setExportFile(File exportFile) {
        this.exportFile = exportFile;
    }

}
