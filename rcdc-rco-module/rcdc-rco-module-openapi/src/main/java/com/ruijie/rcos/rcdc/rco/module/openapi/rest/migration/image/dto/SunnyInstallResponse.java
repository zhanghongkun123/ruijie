package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto;

import com.ruijie.rcos.rcdc.rco.module.openapi.service.RestErrorCodeMapping;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author chenl
 */
public class SunnyInstallResponse {

    private Integer code = 0;

    private String userTip = "success";

    public SunnyInstallResponse() {
    }

    public SunnyInstallResponse(String restErrorCode) {
        this.code = Integer.parseInt(restErrorCode);
        this.userTip = LocaleI18nResolver.resolve(restErrorCode);
    }

    public SunnyInstallResponse(String businessExceptionKey, String userTip) {

        this.code = Integer.parseInt(RestErrorCodeMapping.convert(businessExceptionKey));
        this.userTip = userTip;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getUserTip() {
        return userTip;
    }

    public void setUserTip(String userTip) {
        this.userTip = userTip;
    }
}
