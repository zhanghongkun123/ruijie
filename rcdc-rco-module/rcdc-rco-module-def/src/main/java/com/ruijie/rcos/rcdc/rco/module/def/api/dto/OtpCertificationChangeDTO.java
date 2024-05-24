package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 动态口令策略变更DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/6
 *
 * @author WuShengQiang
 */
public class OtpCertificationChangeDTO {

    /**
     * 启用动态口令
     */
    @NotNull
    private Boolean openOtp;

    /**
     * 是否开启标签页
     */
    @NotNull
    private Boolean hasOtpCodeTab;

    public Boolean getOpenOtp() {
        return openOtp;
    }

    public void setOpenOtp(Boolean openOtp) {
        this.openOtp = openOtp;
    }

    public Boolean getHasOtpCodeTab() {
        return hasOtpCodeTab;
    }

    public void setHasOtpCodeTab(Boolean hasOtpCodeTab) {
        this.hasOtpCodeTab = hasOtpCodeTab;
    }
}
