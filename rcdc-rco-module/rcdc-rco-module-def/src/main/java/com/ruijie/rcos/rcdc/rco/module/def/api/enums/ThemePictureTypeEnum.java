package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/19 20:39
 *
 * @author conghaifeng
 */
public enum ThemePictureTypeEnum {

    RCDC_LOGIN_LOGO("loginLogo.png"),

    RCDC_ADMIN_LOGO("adminLogo.png"),

    RCDC_BACKGROUND("background.png"),

    RCDC_LARGE_SCREEN_LOGO("largeScreenLogo.png");
    
    private String name;

    ThemePictureTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
