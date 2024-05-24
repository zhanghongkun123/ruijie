package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: CheckUploadRequest
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/21
 *
 * @author chenl
 */
public class GetPacketRequest extends DefaultWebRequest {

    @NotNull
    private UUID id;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
