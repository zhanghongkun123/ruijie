package com.ruijie.rcos.rcdc.rco.module.def.cmsupgrade.enums;

/**
 * Description: CmLauncher状态枚举
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public enum ImageTemplateCmLauncherStateEnum {

    /** 配置文件版本不存在 */
    CONFIG_VERSION_NOT_EXIST(""),

    /** 数据库版本不存在 */
    DB_VERSION_NOT_EXIST(""),

    /** 版本过低 */
    VERSION_LOW("CMS云空间版本未更新"),

    /** 已是最新版本 */
    VERSION_SUCCESS("");

    private String message;

    ImageTemplateCmLauncherStateEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
