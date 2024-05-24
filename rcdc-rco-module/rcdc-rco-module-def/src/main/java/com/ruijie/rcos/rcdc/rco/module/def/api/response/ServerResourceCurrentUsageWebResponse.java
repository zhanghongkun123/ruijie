package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.shennong.ObtainServerResourceCurrentUsageResponse;

/**
 * Description: ServerResourceCurrentUsageWebResponse
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/27
 *
 * @author wjp
 */
public class ServerResourceCurrentUsageWebResponse {

    private ObtainServerResourceCurrentUsageResponse total;

    private ObtainServerResourceCurrentUsageResponse used;

    public ObtainServerResourceCurrentUsageResponse getTotal() {
        return total;
    }

    public void setTotal(ObtainServerResourceCurrentUsageResponse total) {
        this.total = total;
    }

    public ObtainServerResourceCurrentUsageResponse getUsed() {
        return used;
    }

    public void setUsed(ObtainServerResourceCurrentUsageResponse used) {
        this.used = used;
    }
}
