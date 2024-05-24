package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AbnormalTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 16:22
 *
 * @author zhangyichi
 */

public class AbnormalPageWebRequest extends PageWebRequest {

    @NotNull
    private ResourceTypeEnum resourceType;

    @NotNull
    private AbnormalTypeEnum abnormalType;

    public ResourceTypeEnum getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    public AbnormalTypeEnum getAbnormalType() {
        return abnormalType;
    }

    public void setAbnormalType(AbnormalTypeEnum abnormalType) {
        this.abnormalType = abnormalType;
    }
}
