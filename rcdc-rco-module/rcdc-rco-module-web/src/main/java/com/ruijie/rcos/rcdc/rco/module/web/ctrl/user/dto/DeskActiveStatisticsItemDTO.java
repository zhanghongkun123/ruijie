package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月23日
 * 
 * @author zouqi
 */
public class DeskActiveStatisticsItemDTO {
    
    private int total;
    
    private int used;

    private int vdiUsed;

    private int idvUsed;

    private int voiUsed;

    private int rcaUsed;

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

    public int getVdiUsed() {
        return vdiUsed;
    }

    public void setVdiUsed(int vdiUsed) {
        this.vdiUsed = vdiUsed;
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

    public int getRcaUsed() {
        return rcaUsed;
    }

    public void setRcaUsed(int rcaUsed) {
        this.rcaUsed = rcaUsed;
    }
}
