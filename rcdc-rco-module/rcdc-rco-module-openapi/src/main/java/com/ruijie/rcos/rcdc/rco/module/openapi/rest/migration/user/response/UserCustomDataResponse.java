package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.response;

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
public class UserCustomDataResponse {

    private String id;

    private String name;

    private Integer code;

    private String userTip;

    public UserCustomDataResponse() {
    }

    public UserCustomDataResponse(String id, String name, String restErrorCode) {
        this.id = id;
        this.name = name;
        this.code = Integer.parseInt(restErrorCode);
        this.userTip = LocaleI18nResolver.resolve(restErrorCode);
    }

    public UserCustomDataResponse(String id, String name, String businessExceptionKey, String userTip) {
        this.id = id;
        this.name = name;
        this.code = Integer.parseInt(RestErrorCodeMapping.convert(businessExceptionKey));
        this.userTip = userTip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
