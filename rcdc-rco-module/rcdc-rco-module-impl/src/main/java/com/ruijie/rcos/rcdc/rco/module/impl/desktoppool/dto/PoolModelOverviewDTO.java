package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/21
 *
 * @author linke
 */
public class PoolModelOverviewDTO implements Serializable {

    @Enumerated(EnumType.STRING)
    private CbbDesktopPoolModel poolModel;

    private Long totalNum;

    private Long runningNum;

    private Long faultNum;

    private Long freeNum;

    public CbbDesktopPoolModel getPoolModel() {
        return poolModel;
    }

    public void setPoolModel(CbbDesktopPoolModel poolModel) {
        this.poolModel = poolModel;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getRunningNum() {
        return runningNum;
    }

    public void setRunningNum(Long runningNum) {
        this.runningNum = runningNum;
    }

    public Long getFaultNum() {
        return faultNum;
    }

    public void setFaultNum(Long faultNum) {
        this.faultNum = faultNum;
    }

    public Long getFreeNum() {
        return freeNum;
    }

    public void setFreeNum(Long freeNum) {
        this.freeNum = freeNum;
    }
}
