package com.ruijie.rcos.rcdc.rco.module.def.cmsupgrade.enums;

/**
 * Description: UwsLauncher状态枚举
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/21
 *
 * @author zwf
 */
public enum ImageTemplateUwsLauncherStateEnum {
    /** 配置文件版本不存在 */
    CONFIG_VERSION_NOT_EXIST(""),

    /** 数据库版本不存在 */
    DB_VERSION_NOT_EXIST(""),

    /** 版本过低 */
    VERSION_LOW("UWS云空间版本未更新"),

    /** 已是最新版本 */
    VERSION_SUCCESS("");

    private String message;

    ImageTemplateUwsLauncherStateEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
