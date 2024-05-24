package com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: 编辑云平台管理入参
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/8
 *
 * @author WuShengQiang
 */
public class EditCloudPlatformManageWebRequest extends CloudPlatformManageWebRequest {

    @NotNull
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
