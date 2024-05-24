package com.ruijie.rcos.rcdc.rco.module.openapi.rest.dashboardstatistics.response;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-10-26
 *
 * @author zqj
 */
public class StatisticsOnlineVDIDeskResponse {
    /**
     * 在线使用中桌面数
     */
    private Long onlineTotal;

    public StatisticsOnlineVDIDeskResponse(Long onlineTotal) {
        this.onlineTotal = onlineTotal;
    }

    public Long getOnlineTotal() {
        return onlineTotal;
    }

    public void setOnlineTotal(Long onlineTotal) {
        this.onlineTotal = onlineTotal;
    }
}
