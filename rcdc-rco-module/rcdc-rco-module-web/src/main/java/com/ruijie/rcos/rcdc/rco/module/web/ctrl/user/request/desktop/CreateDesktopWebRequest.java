package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.VdiDesktopConfigVO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import java.util.UUID;

/**
 * 
 * Description: 创建云桌面网络请求报文
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-1-15
 * 
 * @author artom
 */
public class CreateDesktopWebRequest implements WebRequest {

    /**
     * *用户ID
     */
    @NotNull
    private UUID id;
    
    @NotNull
    private VdiDesktopConfigVO vdiDesktopConfig;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public VdiDesktopConfigVO getVdiDesktopConfig() {
        return vdiDesktopConfig;
    }

    public void setVdiDesktopConfig(VdiDesktopConfigVO vdiDesktopConfig) {
        this.vdiDesktopConfig = vdiDesktopConfig;
    }
}
