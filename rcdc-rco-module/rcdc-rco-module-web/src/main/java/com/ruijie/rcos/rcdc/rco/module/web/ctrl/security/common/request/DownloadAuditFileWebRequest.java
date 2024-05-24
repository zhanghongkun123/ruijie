package com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.common.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.DownloadWebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 下载导出的文件请求体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/1
 *
 * @author WuShengQiang
 */
public class DownloadAuditFileWebRequest implements DownloadWebRequest {

    @ApiModelProperty(value = "下载文件的id", required = true)
    @NotNull
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
