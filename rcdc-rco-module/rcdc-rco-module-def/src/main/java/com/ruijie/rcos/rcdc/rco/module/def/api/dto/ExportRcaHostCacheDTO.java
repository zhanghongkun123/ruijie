package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ExportRcaHostDataStateEnums;

/**
 * 
 * Description: 导出第三方应用主机缓存数据
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/18
 *
 * @author zhiweiHong
 */

public class ExportRcaHostCacheDTO {

    /**
     * 导出状态
     */
    private ExportRcaHostDataStateEnums state;

    /**
     * 导出文件路径
     */
    private String exportFilePath;

    private String fileName;


    public ExportRcaHostDataStateEnums getState() {
        return state;
    }

    public void setState(ExportRcaHostDataStateEnums state) {
        this.state = state;
    }

    public String getExportFilePath() {
        return exportFilePath;
    }

    public void setExportFilePath(String exportFilePath) {
        this.exportFilePath = exportFilePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
