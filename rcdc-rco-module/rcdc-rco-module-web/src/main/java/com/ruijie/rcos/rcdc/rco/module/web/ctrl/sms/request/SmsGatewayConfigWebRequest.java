package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms.request;

import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.HttpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.SmsPlatformType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 短信服务器请求参数
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/11
 *
 * @author TD
 */
public class SmsGatewayConfigWebRequest implements WebRequest {

    /**
     * 国家码
     */
    @Nullable
    @TextShort
    @ApiModelProperty(value = "国家码", required = true)
    private String countryCode;

    /**
     * 短信平台类型
     */
    @NotNull
    @ApiModelProperty(value = "短信平台类型", required = true)
    private SmsPlatformType platformType;

    /**
     * HTTP类型的平台配置参数
     */
    @Nullable
    @ApiModelProperty(value = "短信平台类型，短信平台为HTTP必填")
    private HttpConfigDTO httpConfig;

    @Nullable
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(@Nullable String countryCode) {
        this.countryCode = countryCode;
    }

    public SmsPlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(SmsPlatformType platformType) {
        this.platformType = platformType;
    }

    @Nullable
    public HttpConfigDTO getHttpConfig() {
        return httpConfig;
    }

    public void setHttpConfig(@Nullable HttpConfigDTO httpConfig) {
        this.httpConfig = httpConfig;
    }
}
