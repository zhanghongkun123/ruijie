package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

import java.util.UUID;

/**
 ** 创建云桌面响应
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018-12-12
 * 
 * @author artom
 */
public class CreateDesktopResponse extends DefaultResponse {

    private UUID id;
    
    private String desktopName;

    public CreateDesktopResponse(UUID id, String desktopName) {
        this.id = id;
        this.desktopName = desktopName;
        setStatus(Status.SUCCESS);
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
