package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.PlatformOverviewDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: 云平台运行信息API响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 19:38
 *
 * @author zhangyichi
 */
public class PlatformOverviewResponse extends DefaultResponse {

    private PlatformOverviewDTO platformOverview;

    public PlatformOverviewResponse(PlatformOverviewDTO platformOverview) {
        this.platformOverview = platformOverview;
    }

    public PlatformOverviewDTO getPlatformOverview() {
        return platformOverview;
    }

    public void setPlatformOverview(PlatformOverviewDTO platformOverview) {
        this.platformOverview = platformOverview;
    }
}
