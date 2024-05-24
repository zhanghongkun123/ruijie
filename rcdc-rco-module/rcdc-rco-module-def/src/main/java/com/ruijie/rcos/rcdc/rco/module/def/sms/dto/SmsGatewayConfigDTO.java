package com.ruijie.rcos.rcdc.rco.module.def.sms.dto;

import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.SmsPlatformType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import org.springframework.lang.Nullable;

/**
 * Description: 短信网关配置DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/8
 *
 * @author TD
 */
public class SmsGatewayConfigDTO {

    /**
     * 网关开关
     */
    @NotNull
    private Boolean enable;

    /**
     * 国家码
     */
    @Nullable
    @TextShort
    private String countryCode;

    /**
     * 短信平台类型
     */
    @NotNull
    private String platformType;

    /**
     * HTTP类型的平台配置参数
     */
    @Nullable
    private String httpConfig;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Nullable
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(@Nullable String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    @Nullable
    public String getHttpConfig() {
        return httpConfig;
    }

    public void setHttpConfig(@Nullable String httpConfig) {
        this.httpConfig = httpConfig;
    }
}
