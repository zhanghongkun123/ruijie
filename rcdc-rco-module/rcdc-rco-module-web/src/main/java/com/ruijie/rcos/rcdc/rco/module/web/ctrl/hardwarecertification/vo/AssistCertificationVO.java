package com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo;

import java.util.Objects;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cas.qrcode.CasScanCodeAuthBusinessKey;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 创建用户提交的数据对象
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/03/31
 *
 * @author zhang.zhiwen
 */
public class AssistCertificationVO {

    /** 开启硬件特征码 */
    @Nullable
    @ApiModelProperty(value = "开启硬件特征码")
    private Boolean openHardwareCertification;

    /** 最大终端数 */
    @Nullable
    @ApiModelProperty(value = "最大终端数")
    @Range(min = "1", max = "999")
    private Integer maxHardwareNum;

    /**
     * 启用动态口令
     */
    @Nullable
    @ApiModelProperty(value = "启用动态口令")
    private Boolean openOtpCertification;

    /**
     * 启用短信认证
     */
    @Nullable
    @ApiModelProperty(value = "启用短信认证")
    private Boolean openSmsCertification;

    /**
     * 开启Radius服务
     */
    @Nullable
    @ApiModelProperty(value = "开启Radius服务")
    private Boolean openRadiusCertification;

    @Nullable
    public Boolean getOpenRadiusCertification() {
        return openRadiusCertification;
    }

    public void setOpenRadiusCertification(@Nullable Boolean openRadiusCertification) {
        this.openRadiusCertification = openRadiusCertification;
    }

    @Nullable
    public Boolean getOpenHardwareCertification() {
        return openHardwareCertification;
    }

    public void setOpenHardwareCertification(@Nullable Boolean openHardwareCertification) {
        this.openHardwareCertification = openHardwareCertification;
    }

    @Nullable
    public Integer getMaxHardwareNum() {
        return maxHardwareNum;
    }

    public void setMaxHardwareNum(@Nullable Integer maxHardwareNum) {
        this.maxHardwareNum = maxHardwareNum;
    }

    @Nullable
    public Boolean getOpenOtpCertification() {
        return openOtpCertification;
    }

    public void setOpenOtpCertification(@Nullable Boolean openOtpCertification) {
        this.openOtpCertification = openOtpCertification;
    }

    @Nullable
    public Boolean getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(@Nullable Boolean openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
    }

    /**
     * 校验参数
     * @throws BusinessException 异常
     */
    public void checkParam() throws BusinessException {
        // Radius认证和动态口令不能同时开启
        if (Objects.equals(this.getOpenRadiusCertification(), Boolean.TRUE)
                && Objects.equals(this.getOpenOtpCertification(), Boolean.TRUE)) {
            throw new BusinessException(CasScanCodeAuthBusinessKey.RCDC_RCO_NOT_OPEN_OTP_RADIUS_MEANWHILE);
        }
    }
}
