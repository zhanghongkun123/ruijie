package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ExportDataStateEnums;

/**
 *
 * Description: 导出缓存数据
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/04/13
 *
 * @author guoyongxin
 */
public class ExportCacheDTO {

    /**
     * 导出文件名称
     */
    private String fileName;

    /**
     * 导出文件路径
     */
    private String exportFilePath;

    /**
     * 导出状态
     */
    private ExportDataStateEnums state;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExportFilePath() {
        return exportFilePath;
    }

    public void setExportFilePath(String exportFilePath) {
        this.exportFilePath = exportFilePath;
    }

    public ExportDataStateEnums getState() {
        return state;
    }

    public void setState(ExportDataStateEnums state) {
        this.state = state;
    }
}
