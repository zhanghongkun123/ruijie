package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

import java.util.List;

/**
 * Description: 服务器线性回归预测DTO
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月10日
 *
 * @author brq
 */
public class ServerForecastDTO {

    private Integer estimate = 0;

    private Integer forecastDays;

    private Double threshold;

    private List<RegressionLineDTO> regressionLineList;

    public Integer getEstimate() {
        return estimate;
    }

    public void setEstimate(Integer estimate) {
        this.estimate = estimate;
    }

    public Integer getForecastDays() {
        return forecastDays;
    }

    public void setForecastDays(Integer forecastDays) {
        this.forecastDays = forecastDays;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public List<RegressionLineDTO> getRegressionLineList() {
        return regressionLineList;
    }

    public void setRegressionLineList(List<RegressionLineDTO> regressionLineList) {
        this.regressionLineList = regressionLineList;
    }
}
