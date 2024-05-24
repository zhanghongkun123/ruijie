package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.io.Serializable;
import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年1月10日
 *
 * @author zdc
 */
@ApiModel("任务进度查询请求")
public class TaskQueryRequset implements Serializable {

    @ApiModelProperty(value = "资源id", required = true)
    @NotNull
    private UUID resoureId;

    public UUID getResoureId() {
        return resoureId;
    }

    public void setResoureId(UUID resoureId) {
        this.resoureId = resoureId;
    }
}
