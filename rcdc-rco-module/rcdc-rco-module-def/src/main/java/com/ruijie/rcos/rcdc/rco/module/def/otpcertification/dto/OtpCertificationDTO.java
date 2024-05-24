package com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacMfaConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacTotpHashAlgorithm;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.enums.OtpTypeEnum;

/**
 * 动态口令策略DTO
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年5月17日
 *
 * @author lihengjing
 */
public class OtpCertificationDTO {

    /**
     * 启用动态口令
     */
    private Boolean openOtp;

    /**
     * otp类型 时间/事件
     */
    private OtpTypeEnum otpType;

    /**
     * otp算法类型
     */
    private IacTotpHashAlgorithm algorithm;

    /**
     * totp间隔时间
     */
    private Integer period;

    /**
     * otp 口令位数
     */
    private Integer digits;

    /**
     * otp 系统名称
     */
    private String systemName;

    /**
     * otp 系统域名
     */
    private String systemHost;

    /**
     * 是否开启标签页
     */
    private Boolean hasOtpCodeTab;

    /**
     * 时间差
     */
    private Long timeDifferent;

    /**
     * 是否对管理员生效
     */
    private Boolean enableAdmin;

    public OtpCertificationDTO() {
    }

    public OtpCertificationDTO(IacMfaConfigDTO iacMfaConfigDTO) {
        this.openOtp = iacMfaConfigDTO.getEnableMfa();
        this.otpType = iacMfaConfigDTO.getOtpType() == null ? OtpTypeEnum.TOTP : OtpTypeEnum.valueOf(iacMfaConfigDTO.getOtpType().name());
        this.period = iacMfaConfigDTO.getPeriod();
        this.algorithm = iacMfaConfigDTO.getAlgorithm();
        this.digits = iacMfaConfigDTO.getDigits();
        this.timeDifferent = iacMfaConfigDTO.getTimeDifferent();
        this.systemHost = iacMfaConfigDTO.getSystemHost();
        this.hasOtpCodeTab = Boolean.TRUE.equals(iacMfaConfigDTO.getEnableMfaLogin());
    }

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

    public IacTotpHashAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(IacTotpHashAlgorithm algorithm) {
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

    public Long getTimeDifferent() {
        return timeDifferent;
    }

    public void setTimeDifferent(Long timeDifferent) {
        this.timeDifferent = timeDifferent;
    }

    /**
     * 转换IAC身份中心动态口令配置对象
     * @return IAC身份中心动态口令配置对象
     */
    public IacMfaConfigDTO convertToIacMfaConfig() {

        IacMfaConfigDTO iacMfaConfigDTO = new IacMfaConfigDTO();
        iacMfaConfigDTO.setSubSystem(SubSystem.CDC);
        iacMfaConfigDTO.setEnableMfa(this.openOtp);
        iacMfaConfigDTO.setEnableMfaLogin(this.hasOtpCodeTab);
        iacMfaConfigDTO.setPeriod(this.period);
        iacMfaConfigDTO.setSystemHost(this.systemHost);
        iacMfaConfigDTO.setSystemName(this.systemName);

        return iacMfaConfigDTO;
    }

    public Boolean getEnableAdmin() {
        return enableAdmin;
    }

    public void setEnableAdmin(Boolean enableAdmin) {
        this.enableAdmin = enableAdmin;
    }
}
