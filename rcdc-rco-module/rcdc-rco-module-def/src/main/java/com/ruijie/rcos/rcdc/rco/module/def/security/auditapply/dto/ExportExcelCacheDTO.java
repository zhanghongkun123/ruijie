package com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.ExportExcelStateEnum;

/**
 * Description: 申请单导出表格DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/2
 *
 * @author WuShengQiang
 */
public class ExportExcelCacheDTO {

    /**
     * 导出状态
     */
    private ExportExcelStateEnum state;

    /**
     * 导出文件路径
     */
    private String exportFilePath;

    private String fileName;

    /**
     * 创建时间
     */
    private Long createTimestamp;

    public ExportExcelStateEnum getState() {
        return state;
    }

    public void setState(ExportExcelStateEnum state) {
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

    public Long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
}
