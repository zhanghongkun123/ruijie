package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.UUID;

/**
 * Description: 添加或移除应用池第三方主机
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年2月21日
 *
 * @author zhengjingyong
 */
public class BindRcaPoolThirdPartyHostWebRequest implements WebRequest {

    @ApiModelProperty(value = "应用池id",required = true)
    @NotNull
    private UUID poolId;

    @ApiModelProperty(value = "第三方主机id数组",required = true)
    @NotEmpty
    private List<UUID> hostIdList;

    public UUID getPoolId() {
        return poolId;
    }

    public void setPoolId(UUID poolId) {
        this.poolId = poolId;
    }

    public List<UUID> getHostIdList() {
        return hostIdList;
    }

    public void setHostIdList(List<UUID> hostIdList) {
        this.hostIdList = hostIdList;
    }
}
