package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ExportDataStateEnums;

/**
 *
 * Description: 导出数据缓存状态
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/04/12
 *
 * @author guoyongxin
 */
public class ExportFileInfoDTO {

    /**
     * 文件名称
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

    /**
     * 创建时间
     */
    private Long createTimestamp;

    /**
     * 导出任务开始时间
     */
    private Long taskStartTime;

    /**
     * 初始化时就是doing状态
     */
    public ExportFileInfoDTO() {
        this.state = ExportDataStateEnums.DOING;
    }

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

    public Long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public Long getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(Long taskStartTime) {
        this.taskStartTime = taskStartTime;
    }
}
