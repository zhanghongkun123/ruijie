package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: 桌面数据统计对象
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/28
 *
 * @author Jarman
 */
public class DesktopStatisticsResponse extends DefaultResponse {

    private DesktopStatisticsItem vdi;

    private DesktopStatisticsItem idv;

    public DesktopStatisticsItem getVdi() {
        return vdi;
    }

    public void setVdi(DesktopStatisticsItem vdi) {
        this.vdi = vdi;
    }

    public DesktopStatisticsItem getIdv() {
        return idv;
    }

    public void setIdv(DesktopStatisticsItem idv) {
        this.idv = idv;
    }
}
