package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request;

import org.springframework.lang.Nullable;

/**
 * Description: 启动云桌面参数
 * <p>
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/24 16:43
 *
 * @author zjy
 */
public class StartDeskRequest {

    @Nullable
    private Boolean supportCrossCpuVendor = true;

    public Boolean getSupportCrossCpuVendor() {
        return supportCrossCpuVendor;
    }

    public void setSupportCrossCpuVendor(Boolean supportCrossCpuVendor) {
        this.supportCrossCpuVendor = supportCrossCpuVendor;
    }
}
