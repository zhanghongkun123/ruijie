package com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.request;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 磁盘池添加磁盘请求类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/11
 *
 * @author TD
 */
public class DiskPoolAddDiskWebRequest implements WebRequest {

    /**
     * 磁盘池id
     */
    @NotNull
    @ApiModelProperty(value = "磁盘池ID", required = true)
    private UUID diskPoolId;

    /**
     * 添加磁盘个数
     */
    @NotNull
    @Range(min = "1", max = "1000")
    @ApiModelProperty(value = "添加磁盘个数", required = true)
    private Integer addNum;

    public UUID getDiskPoolId() {
        return diskPoolId;
    }

    public void setDiskPoolId(UUID diskPoolId) {
        this.diskPoolId = diskPoolId;
    }

    public Integer getAddNum() {
        return addNum;
    }

    public void setAddNum(Integer addNum) {
        this.addNum = addNum;
    }
}
