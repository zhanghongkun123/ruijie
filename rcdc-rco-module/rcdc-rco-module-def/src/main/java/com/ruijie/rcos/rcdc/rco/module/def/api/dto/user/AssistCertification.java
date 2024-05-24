package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;

import com.ruijie.rcos.sk.base.annotation.Range;
import org.springframework.lang.Nullable;

/**
 * Description: 用户二次认证
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/29 19:11
 *
 * @author zdc
 */
public class AssistCertification {

    /** 开启硬件特征码 */
    @Nullable
    private Boolean openHardwareCertification;

    /** 最大终端数 */
    @Nullable
    @Range(min = "1", max = "999")
    private Integer maxHardwareNum;

    /**
     * 启用动态口令
     */
    @Nullable
    private Boolean openOtpCertification;

    /**
     * 启用短信认证
     */
    @Nullable
    private Boolean openSmsCertification;

    /**
     * 开启Radius服务
     */
    @Nullable
    private Boolean openRadiusCertification;

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

    @Nullable
    public Boolean getOpenRadiusCertification() {
        return openRadiusCertification;
    }

    public void setOpenRadiusCertification(@Nullable Boolean openRadiusCertification) {
        this.openRadiusCertification = openRadiusCertification;
    }
}