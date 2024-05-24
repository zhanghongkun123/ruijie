package com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月20日
 *
 * @author linrenjian
 */
public class ObtainVOILicenseInfoResponse {

    /**
     * 总数
     */
    private int total;

    /**
     * 总使用数
     */
    private int used;

    /**
     * 教育版VOI证书数
     */
    private int eduVoiNumber;


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

    public int getEduVoiNumber() {
        return eduVoiNumber;
    }

    public void setEduVoiNumber(int eduVoiNumber) {
        this.eduVoiNumber = eduVoiNumber;
    }
}
