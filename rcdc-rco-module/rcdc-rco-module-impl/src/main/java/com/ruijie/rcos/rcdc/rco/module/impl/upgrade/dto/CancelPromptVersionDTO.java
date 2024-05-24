package com.ruijie.rcos.rcdc.rco.module.impl.upgrade.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/15 16:48
 *
 * @author ketb
 */
public class CancelPromptVersionDTO {

    @NotNull
    private String pkgName;

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }
}
