package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ExportCloudDesktopDataStateEnums;


/**
 * Description: 导出PC数据缓存
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/20
 *
 * @author zqj
 */
public class ExportComputerFileInfoDTO {

    /**
     * 导出状态
     */
    private ExportCloudDesktopDataStateEnums state;

    /**
     * 导出文件路径
     */
    private String exportFilePath;

    /**
     * 创建时间
     */
    private Long createTimestamp;

    private String fileName;

    /**
     * 初始化时就是doing状态
     */
    public ExportComputerFileInfoDTO() {
        this.state = ExportCloudDesktopDataStateEnums.DOING;
    }

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

    public Long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
