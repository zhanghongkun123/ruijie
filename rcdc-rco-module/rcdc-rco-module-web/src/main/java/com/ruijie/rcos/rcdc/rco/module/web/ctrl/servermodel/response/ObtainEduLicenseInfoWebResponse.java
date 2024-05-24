package com.ruijie.rcos.rcdc.rco.module.web.ctrl.servermodel.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: ObtainEduLicenseInfoWebResponse
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/27 15:46
 *
 * @author linrenjian
 */
public class ObtainEduLicenseInfoWebResponse {

    @ApiModelProperty(value = "CPU教育版总数")
    private Integer cpuEduTotal;

    @ApiModelProperty(value = "VOI教育版总数")
    private Integer voiEduTotal;

    public Integer getCpuEduTotal() {
        return cpuEduTotal;
    }

    public void setCpuEduTotal(Integer cpuEduTotal) {
        this.cpuEduTotal = cpuEduTotal;
    }

    public Integer getVoiEduTotal() {
        return voiEduTotal;
    }

    public void setVoiEduTotal(Integer voiEduTotal) {
        this.voiEduTotal = voiEduTotal;
    }
}
