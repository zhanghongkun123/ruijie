package com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月20日
 *
 * @author wjp
 */
public class ObtainLicenseInfoResponse {

    private int total;

    private int used;

    private int idvUsed;

    private int voiUsed;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getIdvUsed() {
        return idvUsed;
    }

    public void setIdvUsed(int idvUsed) {
        this.idvUsed = idvUsed;
    }

    public int getVoiUsed() {
        return voiUsed;
    }

    public void setVoiUsed(int voiUsed) {
        this.voiUsed = voiUsed;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }
}
