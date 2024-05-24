package com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response;

/**
 *
 * Description: Function Description 获取IDV UP证书梳理
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月20日
 *
 * @author linrenjian
 */
public class ObtainIdvUpLicenseInfoResponse {

    /**
     * 总数
     */
    private int total;

    /**
     * 总使用数
     */
    private int used;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }
}
