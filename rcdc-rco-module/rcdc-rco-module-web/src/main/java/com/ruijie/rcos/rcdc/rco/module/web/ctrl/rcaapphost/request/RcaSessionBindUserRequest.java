package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 增加应用主机和用户的会话绑定关系
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月26日
 *
 * @author liuwc
 */
public class RcaSessionBindUserRequest {

    @ApiModelProperty(value = "云主机id")
    @NotNull
    private UUID hostId;

    @ApiModelProperty(value = "用户id列表")
    @NotNull
    private UUID[] idArr;

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }
}
