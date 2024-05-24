package com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.dto;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/27 9:52
 *
 * @author linrenjian
 */
public class ObtainEduLicenseInfoDTO {

    /**
     * 总数
     */
    private Integer total;

    /**
     * 使用数
     */
    private Integer used;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }
}
