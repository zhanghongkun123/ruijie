package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyHttpConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyResultParserConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.thiraparty.ThirdPartyAuthOriginEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * 第三方认证基础配置
 *
 * Description: 第三方认证基础配置
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/13 16:56
 *
 * @author zjy
 */
public class ThirdPartyConfigBaseRequest implements WebRequest {

    /**
     * 第三方认证开关
     */
    @ApiModelProperty(value = "第三方认证开关", required = true)
    @NotNull
    private Boolean thirdPartyEnable;

    /**
     * 认证源
     */
    @ApiModelProperty(value = "认证源", required = true)
    @Nullable
    private ThirdPartyAuthOriginEnum authOrigin;


    /**
     * http相关配置
     */
    @ApiModelProperty(value = "http相关配置", required = true)
    @Nullable
    private BaseThirdPartyHttpConfigDTO httpConfig;

    /**
     * 结果解析相关配置
     */
    @ApiModelProperty(value = "结果解析相关配置", required = true)
    @Nullable
    private BaseThirdPartyResultParserConfigDTO resultParserConfig;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", required = false)
    @Nullable
    private String note;

    public Boolean getThirdPartyEnable() {
        return thirdPartyEnable;
    }

    public void setThirdPartyEnable(Boolean thirdPartyEnable) {
        this.thirdPartyEnable = thirdPartyEnable;
    }

    @Nullable
    public ThirdPartyAuthOriginEnum getAuthOrigin() {
        return authOrigin;
    }

    public void setAuthOrigin(@Nullable ThirdPartyAuthOriginEnum authOrigin) {
        this.authOrigin = authOrigin;
    }

    @Nullable
    public BaseThirdPartyHttpConfigDTO getHttpConfig() {
        return httpConfig;
    }

    public void setHttpConfig(@Nullable BaseThirdPartyHttpConfigDTO httpConfig) {
        this.httpConfig = httpConfig;
    }

    @Nullable
    public BaseThirdPartyResultParserConfigDTO getResultParserConfig() {
        return resultParserConfig;
    }

    public void setResultParserConfig(@Nullable BaseThirdPartyResultParserConfigDTO resultParserConfig) {
        this.resultParserConfig = resultParserConfig;
    }

    @Nullable
    public String getNote() {
        return note;
    }

    public void setNote(@Nullable String note) {
        this.note = note;
    }
}
