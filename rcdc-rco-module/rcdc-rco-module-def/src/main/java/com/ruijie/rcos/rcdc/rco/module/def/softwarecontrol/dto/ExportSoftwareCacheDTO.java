package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto;


import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.ExportSoftwareDataStateEnums;

/**
 * 
 * Description: 导出软件信息缓存数据
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/16
 *
 * @author lihengjing
 */

public class ExportSoftwareCacheDTO {

    /**
     * 导出状态
     */
    private ExportSoftwareDataStateEnums state;

    /**
     * 导出文件路径
     */
    private String exportFilePath;

    private String fileName;


    public ExportSoftwareDataStateEnums getState() {
        return state;
    }

    public void setState(ExportSoftwareDataStateEnums state) {
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
