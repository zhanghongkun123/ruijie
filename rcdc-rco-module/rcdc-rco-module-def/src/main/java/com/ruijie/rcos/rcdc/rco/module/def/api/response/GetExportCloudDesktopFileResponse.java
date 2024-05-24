package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;


import java.io.File;

/**
 * 
 * Description: 获取导出第三方应用主机信息回应类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/16
 *
 * @author zhiweiHong
 */
public class GetExportCloudDesktopFileResponse extends DefaultResponse {

    private File exportFile;

    public GetExportCloudDesktopFileResponse(File exportFile) {
        this.exportFile = exportFile;
    }

    public GetExportCloudDesktopFileResponse()  { }

    public File getExportFile() {
        return exportFile;
    }

    public void setExportFile(File exportFile) {
        this.exportFile = exportFile;
    }
}
