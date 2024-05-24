package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;/**

/**
 * 云桌面正太分布DTO
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月9日
 *
 * @author bairuqiang
 */
public class NormalDistributionDTO {

    private Double averageValue;

    private Double normalNumLeftLine;

    private Double normalNumRightLine;

    private Double lowLoadLine;

    private Double highLoadLine;

    private Double ultraLowLoadLine;

    private Double ultraHighLoadLine;

    public Double getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(Double averageValue) {
        this.averageValue = averageValue;
    }

    public Double getNormalNumLeftLine() {
        return normalNumLeftLine;
    }

    public void setNormalNumLeftLine(Double normalNumLeftLine) {
        this.normalNumLeftLine = normalNumLeftLine;
    }

    public Double getNormalNumRightLine() {
        return normalNumRightLine;
    }

    public void setNormalNumRightLine(Double normalNumRightLine) {
        this.normalNumRightLine = normalNumRightLine;
    }

    public Double getLowLoadLine() {
        return lowLoadLine;
    }

    public void setLowLoadLine(Double lowLoadLine) {
        this.lowLoadLine = lowLoadLine;
    }

    public Double getHighLoadLine() {
        return highLoadLine;
    }

    public void setHighLoadLine(Double highLoadLine) {
        this.highLoadLine = highLoadLine;
    }

    public Double getUltraLowLoadLine() {
        return ultraLowLoadLine;
    }

    public void setUltraLowLoadLine(Double ultraLowLoadLine) {
        this.ultraLowLoadLine = ultraLowLoadLine;
    }

    public Double getUltraHighLoadLine() {
        return ultraHighLoadLine;
    }

    public void setUltraHighLoadLine(Double ultraHighLoadLine) {
        this.ultraHighLoadLine = ultraHighLoadLine;
    }
}
