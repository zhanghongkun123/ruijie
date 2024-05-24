package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.request.CloudPlatformManageWebRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月25日
 *
 * @author lanzf
 */
@ApiModel("备份恢复")
public class CloudPlatformResourceRecoverRequest {

    @ApiModelProperty(value = "替换的云平台", required = true)
    @NotNull
    private IdLabelEntry fromPlatformEntry;

    @ApiModelProperty(value = "新的云平台", required = true)
    @NotNull
    private CloudPlatformManageWebRequest toPlatform;

    public IdLabelEntry getFromPlatformEntry() {
        return fromPlatformEntry;
    }

    public void setFromPlatformEntry(IdLabelEntry fromPlatformEntry) {
        this.fromPlatformEntry = fromPlatformEntry;
    }

    public CloudPlatformManageWebRequest getToPlatform() {
        return toPlatform;
    }

    public void setToPlatform(CloudPlatformManageWebRequest toPlatform) {
        this.toPlatform = toPlatform;
    }
}
