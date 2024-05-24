package com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto;

import com.ruijie.rcos.rcdc.rco.module.def.upgrade.enums.UpgradePackageLevelEnum;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/13 17:40
 *
 * @author ketb
 */
public class PromptVersionInfoDTO {

    private String name;

    private String pkgName;

    private String publicVersion;

    private UpgradePackageLevelEnum level;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getPublicVersion() {
        return publicVersion;
    }

    public void setPublicVersion(String publicVersion) {
        this.publicVersion = publicVersion;
    }

    public UpgradePackageLevelEnum getLevel() {
        return level;
    }

    public void setLevel(UpgradePackageLevelEnum level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
