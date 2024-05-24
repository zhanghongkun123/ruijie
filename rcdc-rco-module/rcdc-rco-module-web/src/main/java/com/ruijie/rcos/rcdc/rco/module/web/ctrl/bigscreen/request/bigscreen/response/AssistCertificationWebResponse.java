package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * Description: 辅助认证策略响应
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月17日
 *
 * @author lihengjing
 */
@ApiModel("辅助认证响应体")
public class AssistCertificationWebResponse {
    /**
     * 启用动态口令
     */
    @ApiModelProperty("动态口令开关配置:true 是,false 否")
    private Boolean openOtp;

    /**
     * 启用硬件特征码
     */
    @ApiModelProperty("件特征码开关配置:true 是,false 否")
    private Boolean openHardware;

    /**
     * 是否开启动态口令标签页
     */
    @ApiModelProperty("动态口令标签页开关配置:true 是,false 否")
    private Boolean hasOtpCodeTab;

    @ApiModelProperty(value = "是否启用短信认证")
    private Boolean openSmsCertification;

    @ApiModelProperty(value = "是否开启Radius认证")
    private Boolean openRadiusCertification;

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

    public Boolean getOpenHardware() {
        return openHardware;
    }

    public void setOpenHardware(Boolean openHardware) {
        this.openHardware = openHardware;
    }

    public Boolean getHasOtpCodeTab() {
        return hasOtpCodeTab;
    }

    public void setHasOtpCodeTab(Boolean hasOtpCodeTab) {
        this.hasOtpCodeTab = hasOtpCodeTab;
    }

    public Boolean getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(Boolean openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
    }
}
