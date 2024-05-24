package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ExportCloudDesktopDataStateEnums;
/**
 * 
 * Description: 导出云桌面缓存数据
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/18
 *
 * @author zhiweiHong
 */

public class ExportCloudDesktopCacheDTO {

    /**
     * 导出状态
     */
    private ExportCloudDesktopDataStateEnums state;

    /**
     * 导出文件路径
     */
    private String exportFilePath;

    private String fileName;


    public ExportCloudDesktopDataStateEnums getState() {
        return state;
    }

    public void setState(ExportCloudDesktopDataStateEnums state) {
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
