package com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AbnormalTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 异常云桌面分页查询API请求
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 17:26
 *
 * @author zhangyichi
 */
public class AbnormalPageRequest extends PageSearchRequest {

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
