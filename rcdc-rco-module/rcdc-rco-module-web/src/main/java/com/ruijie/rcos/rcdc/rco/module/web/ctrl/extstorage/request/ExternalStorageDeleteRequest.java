package com.ruijie.rcos.rcdc.rco.module.web.ctrl.extstorage.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 删除外置存储请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/10
 *
 * @author TD
 */
public class ExternalStorageDeleteRequest {

    /**
     * 外置存储ID
     */
    @ApiModelProperty(value = "外置存储ID集合", required = true)
    @NotNull
    private UUID[] idArr;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }
}
