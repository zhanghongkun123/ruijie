package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

/**
 * Description: description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/26 18:33
 *
 * @author BaiGuoliang
 */
public class AllDesktopOverviewDTO {

    private Integer bootUp = 0;

    private Integer total = 0;

    private Integer bootUpAvg;

    private Integer historyBootUpTop;

    public Integer getBootUp() {
        return bootUp;
    }

    public void setBootUp(Integer bootUp) {
        this.bootUp = bootUp;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getBootUpAvg() {
        return bootUpAvg;
    }

    public void setBootUpAvg(Integer bootUpAvg) {
        this.bootUpAvg = bootUpAvg;
    }

    public Integer getHistoryBootUpTop() {
        return historyBootUpTop;
    }

    public void setHistoryBootUpTop(Integer historyBootUpTop) {
        this.historyBootUpTop = historyBootUpTop;
    }
}
