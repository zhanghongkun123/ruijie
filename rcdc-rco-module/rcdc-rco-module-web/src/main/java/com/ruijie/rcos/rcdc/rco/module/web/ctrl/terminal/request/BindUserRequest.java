package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 用户变更请求入参
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/8 10:26
 *
 * @author wushengqiang
 */
public class BindUserRequest {

    @ApiModelProperty(value = "终端ID", required = true)
    @NotBlank
    private String id;

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull
    private UUID bindUserId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID getBindUserId() {
        return bindUserId;
    }

    public void setBindUserId(UUID bindUserId) {
        this.bindUserId = bindUserId;
    }
}
