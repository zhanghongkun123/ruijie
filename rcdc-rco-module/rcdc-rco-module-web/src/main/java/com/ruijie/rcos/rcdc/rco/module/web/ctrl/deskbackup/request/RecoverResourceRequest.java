package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request;

import java.util.UUID;

import com.ruijie.rcos.rcdc.backup.module.def.enums.RecoverType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 恢复资源请求
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/27
 *
 * @author lanzf
 */
@ApiModel("恢复资源请求")
public class RecoverResourceRequest implements WebRequest {

    @ApiModelProperty(value = "资源ID", required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "恢复方式", required = true)
    @NotNull
    private RecoverType recoverType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RecoverType getRecoverType() {
        return recoverType;
    }

    public void setRecoverType(RecoverType recoverType) {
        this.recoverType = recoverType;
    }
}
