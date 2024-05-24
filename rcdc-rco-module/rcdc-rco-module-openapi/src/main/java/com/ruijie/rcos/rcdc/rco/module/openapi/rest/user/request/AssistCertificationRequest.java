package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import com.ruijie.rcos.sk.base.annotation.Range;
import org.springframework.lang.Nullable;

/**
 * Description: 辅助认证
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author TD
 */
public class AssistCertificationRequest {

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
     * 开启短信认证
     */
    @Nullable
    private Boolean openSmsCertification;

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
}
