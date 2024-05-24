package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbLocalExternalStorageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/10
 *
 * @author TD
 */
@ApiModel("文件流转配置信息")
public class GetAuditFileGlobalConfigResponse {
    
    @ApiModelProperty("申请记录保留时间（天）")
    private Integer interval;
    
    @ApiModelProperty("是否开启外置存储")
    private Boolean enableExtStorage;
    
    @ApiModelProperty("外置存储信息")
    private CbbLocalExternalStorageDTO externalStorageDTO;

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Boolean getEnableExtStorage() {
        return enableExtStorage;
    }

    public void setEnableExtStorage(Boolean enableExtStorage) {
        this.enableExtStorage = enableExtStorage;
    }

    public CbbLocalExternalStorageDTO getExternalStorageDTO() {
        return externalStorageDTO;
    }

    public void setExternalStorageDTO(CbbLocalExternalStorageDTO externalStorageDTO) {
        this.externalStorageDTO = externalStorageDTO;
    }
}
