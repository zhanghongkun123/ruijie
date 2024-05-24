package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

/**
 * Description: 桌面正态分布计算DTO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 19:28
 *
 * @author zhangyichi
 */
public class DesktopNormalDistributionDTO {

    private Double averageValue = 0.0;

    private Double standardDeviation = 0.0;

    private Integer normalNumLeft = 0;

    private Integer normalNumRight = 0;

    private Integer lowLoad = 0;

    private Integer highLoad  = 0;

    private Integer ultraLowLoad = 0;

    private Integer ultraHighLoad = 0;

    private Integer lowLoadAbnormal = 0;

    private Integer highLoadAbnormal = 0;

    public Double getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(Double averageValue) {
        this.averageValue = averageValue;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public Integer getNormalNumLeft() {
        return normalNumLeft;
    }

    public void setNormalNumLeft(Integer normalNumLeft) {
        this.normalNumLeft = normalNumLeft;
    }

    public Integer getNormalNumRight() {
        return normalNumRight;
    }

    public void setNormalNumRight(Integer normalNumRight) {
        this.normalNumRight = normalNumRight;
    }

    public Integer getLowLoad() {
        return lowLoad;
    }

    public void setLowLoad(Integer lowLoad) {
        this.lowLoad = lowLoad;
    }

    public Integer getHighLoad() {
        return highLoad;
    }

    public void setHighLoad(Integer highLoad) {
        this.highLoad = highLoad;
    }

    public Integer getUltraLowLoad() {
        return ultraLowLoad;
    }

    public void setUltraLowLoad(Integer ultraLowLoad) {
        this.ultraLowLoad = ultraLowLoad;
    }

    public Integer getUltraHighLoad() {
        return ultraHighLoad;
    }

    public void setUltraHighLoad(Integer ultraHighLoad) {
        this.ultraHighLoad = ultraHighLoad;
    }

    public Integer getLowLoadAbnormal() {
        return lowLoadAbnormal;
    }

    public void setLowLoadAbnormal(Integer lowLoadAbnormal) {
        this.lowLoadAbnormal = lowLoadAbnormal;
    }

    public Integer getHighLoadAbnormal() {
        return highLoadAbnormal;
    }

    public void setHighLoadAbnormal(Integer highLoadAbnormal) {
        this.highLoadAbnormal = highLoadAbnormal;
    }
}
