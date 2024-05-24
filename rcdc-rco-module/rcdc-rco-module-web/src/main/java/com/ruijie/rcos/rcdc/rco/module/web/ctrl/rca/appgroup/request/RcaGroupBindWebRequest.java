package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.request;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 应用组分配关系请求体
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月24日
 *
 * @author zhengjingyong
 */
public class RcaGroupBindWebRequest implements WebRequest {

    @ApiModelProperty(value = "池id")
    @NotNull
    private UUID poolId;

    @ApiModelProperty(value = "批量分配请求体")
    @NotEmpty
    private List<RcaGroupBindAppGroupWebRequest> bindInfoList;

    public UUID getPoolId() {
        return poolId;
    }

    public void setPoolId(UUID poolId) {
        this.poolId = poolId;
    }

    public List<RcaGroupBindAppGroupWebRequest> getBindInfoList() {
        return bindInfoList;
    }

    public void setBindInfoList(List<RcaGroupBindAppGroupWebRequest> bindInfoList) {
        this.bindInfoList = bindInfoList;
    }
}
