package com.ruijie.rcos.rcdc.rco.module.openapi.rest.dashboardstatistics.response;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-10-26
 *
 * @author zqj
 */
public class StatisticsUserAndDeskResponse {
    /**
     * 用户总条数
     */
    private Long userTotal;

    /**
     * 运行中桌面数
     */
    private Long deskRunNum;

    /**
     * 云桌面总数
     */
    private Long deskTotal;

    public StatisticsUserAndDeskResponse(Long userTotal, Long deskRunNum, Long deskTotal) {
        this.userTotal = userTotal;
        this.deskRunNum = deskRunNum;
        this.deskTotal = deskTotal;
    }

    public Long getUserTotal() {
        return userTotal;
    }

    public void setUserTotal(Long userTotal) {
        this.userTotal = userTotal;
    }

    public Long getDeskRunNum() {
        return deskRunNum;
    }

    public void setDeskRunNum(Long deskRunNum) {
        this.deskRunNum = deskRunNum;
    }

    public Long getDeskTotal() {
        return deskTotal;
    }

    public void setDeskTotal(Long deskTotal) {
        this.deskTotal = deskTotal;
    }
}
