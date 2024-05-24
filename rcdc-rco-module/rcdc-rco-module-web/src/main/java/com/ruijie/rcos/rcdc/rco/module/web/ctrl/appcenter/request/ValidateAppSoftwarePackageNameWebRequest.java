package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月14日
 * 
 * @author chenl
 */
public class ValidateAppSoftwarePackageNameWebRequest implements WebRequest {

    @NotBlank
    @TextMedium
    @TextName
    private String appSoftwarePackageName;

    @Nullable
    private UUID id;

    public String getAppSoftwarePackageName() {
        return appSoftwarePackageName;
    }

    public void setAppSoftwarePackageName(String appSoftwarePackageName) {
        this.appSoftwarePackageName = appSoftwarePackageName;
    }

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }
}
