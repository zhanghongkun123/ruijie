package com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.ExportUserProfilePathDataStateEnum;

/**
 * Description: 导出路径信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public class ExportUserProfilePathCacheDTO {

    /**
     * 导出状态
     */
    private ExportUserProfilePathDataStateEnum state;

    /**
     * 导出文件路径
     */
    private String exportFilePath;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 创建时间
     */
    private Long createTimestamp;

    public ExportUserProfilePathDataStateEnum getState() {
        return state;
    }

    public void setState(ExportUserProfilePathDataStateEnum state) {
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
