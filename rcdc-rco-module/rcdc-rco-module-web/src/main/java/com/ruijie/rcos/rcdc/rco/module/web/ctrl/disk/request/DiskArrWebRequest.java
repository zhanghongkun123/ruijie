package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk.request;

import java.util.Arrays;
import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 解绑磁盘请求类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/16
 *
 * @author TD
 */
public class DiskArrWebRequest implements WebRequest {

    /**
     * 磁盘ID集合
     */
    @NotEmpty
    @ApiModelProperty(value = "磁盘ID集合", required = true)
    private UUID[] diskIdArr;

    public UUID[] getDiskIdArr() {
        return diskIdArr;
    }

    public void setDiskIdArr(UUID[] diskIdArr) {
        this.diskIdArr = diskIdArr;
    }

    @Override
    public String toString() {
        return "UnBindUserWebRequest{" +
                "diskIdArr=" + Arrays.toString(diskIdArr) +
                '}';
    }
}
