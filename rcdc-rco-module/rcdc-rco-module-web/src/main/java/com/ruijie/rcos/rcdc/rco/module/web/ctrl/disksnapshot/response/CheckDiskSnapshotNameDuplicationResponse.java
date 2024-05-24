package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 校验磁盘快照名称是否重复响应
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年07月28日
 *
 * @author lyb
 */
@ApiModel("磁盘快照名称是否重复返回体")
public class CheckDiskSnapshotNameDuplicationResponse {

    @ApiModelProperty(value = "是否重复， true：表示重复， false：表示没重复")
    private Boolean hasDuplication;

    public CheckDiskSnapshotNameDuplicationResponse(Boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }

    public Boolean getHasDuplication() {
        return hasDuplication;
    }

    public void setHasDuplication(Boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }
}
