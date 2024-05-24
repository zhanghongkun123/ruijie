package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 硬件特征码、动态口令、CAS认证开关
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/6 16:07
 *
 * @author yxq
 */
@ApiModel("查询硬件特征码、动态口令、CAS认证开关VO")
public class CertificationStrategyVO {

    @ApiModelProperty(value = "是否开启动态口令")
    private Boolean openOtp;

    @ApiModelProperty(value = "是否开启CAS扫码认证")
    private Boolean openCas;

    @ApiModelProperty(value = "是否开启硬件特征码")
    private Boolean openHardware;

    @ApiModelProperty(value = "是否启用短信认证")
    private Boolean openSmsCertification;

    @ApiModelProperty(value = "是否开启radius认证")
    private Boolean openRadiusCertification;

    @ApiModelProperty(value = "是否开启第三方认证")
    private Boolean openThirdPartyCertification;

    public Boolean getOpenRadiusCertification() {
        return openRadiusCertification;
    }

    public void setOpenRadiusCertification(Boolean openRadiusCertification) {
        this.openRadiusCertification = openRadiusCertification;
    }

    public Boolean getOpenOtp() {
        return openOtp;
    }

    public void setOpenOtp(Boolean openOtp) {
        this.openOtp = openOtp;
    }

    public Boolean getOpenCas() {
        return openCas;
    }

    public void setOpenCas(Boolean openCas) {
        this.openCas = openCas;
    }

    public Boolean getOpenHardware() {
        return openHardware;
    }

    public void setOpenHardware(Boolean openHardware) {
        this.openHardware = openHardware;
    }

    public Boolean getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(Boolean openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
    }

    public Boolean getOpenThirdPartyCertification() {
        return openThirdPartyCertification;
    }

    public void setOpenThirdPartyCertification(Boolean openThirdPartyCertification) {
        this.openThirdPartyCertification = openThirdPartyCertification;
    }
}
