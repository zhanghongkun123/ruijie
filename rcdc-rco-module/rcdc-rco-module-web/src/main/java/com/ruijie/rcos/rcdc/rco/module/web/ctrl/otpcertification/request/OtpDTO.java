package com.ruijie.rcos.rcdc.rco.module.web.ctrl.otpcertification.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.enums.OtpAlgorithmEnum;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.enums.OtpTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import io.swagger.annotations.ApiModelProperty;

/**
 *
 * Description: 动态口令策略
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月17日
 *
 * @author lihengjing
 */
public class OtpDTO {

    /**
     * 启用动态口令
     */
    @ApiModelProperty(value = "是否启用动态口令", required = true)
    @NotNull
    private Boolean openOtp;

    /**
     * otp类型 时间/事件
     */
    @ApiModelProperty(value = "otp类型 时间/事件")
    @Nullable
    private OtpTypeEnum otpType;

    /**
     * otp算法类型
     */
    @ApiModelProperty(value = "otp算法类型")
    @Nullable
    private OtpAlgorithmEnum algorithm;

    /**
     * totp间隔时间
     */
    @ApiModelProperty(value = "totp间隔时间")
    @Nullable
    private Integer period;

    /**
     * otp 口令位数
     */
    @ApiModelProperty(value = "otp口令位数")
    @Nullable
    private Integer digits;

    /**
     * otp 系统名称
     */
    @ApiModelProperty(value = "otp系统名称")
    @Nullable
    private String systemName;

    /**
     * otp 系统域名
     */
    @ApiModelProperty(value = "otp系统域名")
    @Nullable
    private String systemHost;

    /**
     * 开启
     */
    @ApiModelProperty(value = "开启动态标签页", required = true)
    @NotNull
    private Boolean hasOtpCodeTab;

    @ApiModelProperty(value = "系统时间")
    @NotNull
    private Long systemTime;

    public Boolean getOpenOtp() {
        return openOtp;
    }

    public void setOpenOtp(Boolean openOtp) {
        this.openOtp = openOtp;
    }

    public OtpTypeEnum getOtpType() {
        return otpType;
    }

    public void setOtpType(OtpTypeEnum otpType) {
        this.otpType = otpType;
    }

    public OtpAlgorithmEnum getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(OtpAlgorithmEnum algorithm) {
        this.algorithm = algorithm;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getDigits() {
        return digits;
    }

    public void setDigits(Integer digits) {
        this.digits = digits;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemHost() {
        return systemHost;
    }

    public void setSystemHost(String systemHost) {
        this.systemHost = systemHost;
    }

    public Boolean getHasOtpCodeTab() {
        return hasOtpCodeTab;
    }

    public void setHasOtpCodeTab(Boolean hasOtpCodeTab) {
        this.hasOtpCodeTab = hasOtpCodeTab;
    }

    public Long getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(Long systemTime) {
        this.systemTime = systemTime;
    }
}
