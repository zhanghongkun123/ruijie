package com.ruijie.rcos.rcdc.rco.module.def.api.request.qr;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacQrCodeType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.Size;

/**
 * Description: 二维码配置
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-24 15:03:00
 *
 * @author zjy
 */
public class QrCodeConfigReq {

    @NotNull
    private CbbQrCodeType qrCodeType;

    @NotNull
    private Boolean switchStatus;

    @Nullable
    @Range(min = "1")
    private Long expireTime;

    @Nullable
    private String advanceConfig;

    @Size(max = 1024)
    @Nullable
    private String contentPrefix;

    public Boolean getSwitchStatus() {
        return switchStatus;
    }

    public void setSwitchStatus(Boolean switchStatus) {
        this.switchStatus = switchStatus;
    }

    public CbbQrCodeType getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(CbbQrCodeType qrCodeType) {
        this.qrCodeType = qrCodeType;
    }

    @Nullable
    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(@Nullable Long expireTime) {
        this.expireTime = expireTime;
    }

    @Nullable
    public String getAdvanceConfig() {
        return advanceConfig;
    }

    public void setAdvanceConfig(@Nullable String advanceConfig) {
        this.advanceConfig = advanceConfig;
    }

    @Nullable
    public String getContentPrefix() {
        return contentPrefix;
    }

    public void setContentPrefix(@Nullable String contentPrefix) {
        this.contentPrefix = contentPrefix;
    }
}
