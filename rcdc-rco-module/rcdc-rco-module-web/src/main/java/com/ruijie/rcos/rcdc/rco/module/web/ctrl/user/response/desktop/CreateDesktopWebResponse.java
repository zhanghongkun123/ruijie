package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.desktop;

import java.util.UUID;

/**
 * 
 * Description: 创建云桌面响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月15日
 * 
 * @author artom
 */
public class CreateDesktopWebResponse {

    private UUID id;
    
    private String desktopName;

    public CreateDesktopWebResponse() {
        //
    }

    public CreateDesktopWebResponse(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

}
