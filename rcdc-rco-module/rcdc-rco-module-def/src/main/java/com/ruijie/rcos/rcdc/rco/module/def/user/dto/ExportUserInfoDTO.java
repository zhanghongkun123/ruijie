package com.ruijie.rcos.rcdc.rco.module.def.user.dto;

import com.ruijie.rcos.rcdc.rco.module.def.enums.ExportUserDataStateEnums;

/**
 * 
 * Description: 导出用户信息缓存数据
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/24
 *
 * @author tangxu
 */

public class ExportUserInfoDTO {

    /**
     * 导出状态
     */
    private ExportUserDataStateEnums state;

    /**
     * 是否二次进入下载，前端查询时会根据本标识进行变更提示语
     */
    private Boolean reDoing = Boolean.FALSE;
    
    /**
     * 导出文件路径
     */
    private String exportFilePath;

    /**
     * 创建时间
     */
    private Long createTimestamp;

    public ExportUserDataStateEnums getState() {
        return state;
    }

    public void setState(ExportUserDataStateEnums state) {
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

    public Boolean getReDoing() {
        return reDoing;
    }

    public void setReDoing(Boolean reDoing) {
        this.reDoing = reDoing;
    }
}
