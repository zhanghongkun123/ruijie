package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ExportRcaHostDataStateEnums;


/**
 * Description: 导出第三方应用主机数据缓存
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/8/27
 *
 * @author zhiweiHong
 */
public class ExportRcaHostFileInfoDTO {

    /**
     * 导出状态
     */
    private ExportRcaHostDataStateEnums state;

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
    public ExportRcaHostFileInfoDTO() {
        this.state = ExportRcaHostDataStateEnums.DOING;
    }

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
