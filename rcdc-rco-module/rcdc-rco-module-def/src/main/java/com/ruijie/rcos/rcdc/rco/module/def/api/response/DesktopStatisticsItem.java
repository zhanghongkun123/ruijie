package com.ruijie.rcos.rcdc.rco.module.def.api.response;

/**
 * Description: 云桌面统计项
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/28
 *
 * @author Jarman
 */
public class DesktopStatisticsItem {

    /**
     * 桌面总数量 online + offline
     */
    private Integer total = 0;

    /**
     * 桌面在线数量
     */
    private Integer online = 0;

    /**
     * 桌面离线数量
     */
    private Integer offline = 0;

    /**
     * 桌面休眠数
     */
    private Integer sleep = 0;

    /**
     * 从未登录的桌面数量
     */
    private Integer neverLogin = 0;


    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public Integer getOffline() {
        return offline;
    }

    public void setOffline(Integer offline) {
        this.offline = offline;
    }

    public Integer getSleep() {
        return sleep;
    }

    public void setSleep(Integer sleep) {
        this.sleep = sleep;
    }

    public Integer getNeverLogin() {
        return neverLogin;
    }

    public void setNeverLogin(Integer neverLogin) {
        this.neverLogin = neverLogin;
    }
}
