package com.ruijie.rcos.rcdc.rco.module.web.ctrl.upgrade.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;


/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/22 10:46
 *
 * @author ketb
 */
public class CancelPromptVersionWebRequest implements WebRequest {

    @NotNull
    @ApiModelProperty(value = "安装包名", required = true)
    private String pkgName;

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }
}
