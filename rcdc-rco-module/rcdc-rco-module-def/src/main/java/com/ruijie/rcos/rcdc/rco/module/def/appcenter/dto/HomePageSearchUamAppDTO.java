package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/17 15:14
 *
 * @author coderLee23
 */
public class HomePageSearchUamAppDTO extends BasePermissionDTO {

    /**
     * 支持应用名称模糊查询，忽略大小写
     */
    private String appName;

    /**
     * 过滤已被交付组添加的应用
     */
    private AppTypeEnum appType;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public AppTypeEnum getAppType() {
        return appType;
    }

    public void setAppType(AppTypeEnum appType) {
        this.appType = appType;
    }
}
