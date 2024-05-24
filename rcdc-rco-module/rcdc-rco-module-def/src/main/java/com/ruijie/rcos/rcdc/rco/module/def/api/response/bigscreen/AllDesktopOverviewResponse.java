package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.AllDesktopOverviewDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: 获取所有云桌面开机信息API响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 19:35
 *
 * @author zhangyichi
 */
public class AllDesktopOverviewResponse extends DefaultResponse {

    private AllDesktopOverviewDTO allDesktopOverviewDTO;

    public AllDesktopOverviewResponse() {
    }

    public AllDesktopOverviewResponse(AllDesktopOverviewDTO allDesktopOverviewDTO) {
        this.allDesktopOverviewDTO = allDesktopOverviewDTO;
    }

    public AllDesktopOverviewDTO getAllDesktopOverviewDTO() {
        return allDesktopOverviewDTO;
    }

    public void setAllDesktopOverviewDTO(AllDesktopOverviewDTO allDesktopOverviewDTO) {
        this.allDesktopOverviewDTO = allDesktopOverviewDTO;
    }
}
