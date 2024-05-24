package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.ExportSoftwareDataStateEnums;


/**
 * Description: 导出软件信息数据缓存
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/14
 *
 * @author lihengjing
 */
public class ExportSoftwareFileInfoDTO {

    /**
     * 导出状态
     */
    private ExportSoftwareDataStateEnums state;

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
    public ExportSoftwareFileInfoDTO() {
        this.state = ExportSoftwareDataStateEnums.DOING;
    }

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
