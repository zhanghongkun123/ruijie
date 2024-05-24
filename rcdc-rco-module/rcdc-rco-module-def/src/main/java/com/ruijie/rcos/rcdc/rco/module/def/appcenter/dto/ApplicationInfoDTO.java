package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年01月04日
 *
 * @author zhk
 */
public class ApplicationInfoDTO {

    private UUID id;

    private String name;

    private String appType;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
