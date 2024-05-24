package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbadvancedsetting;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月16日
 * 
 * @author zhuangchenwu
 */
public class UpdateCameraConfigStrategyWebRequest implements WebRequest {

    @NotNull
    @Range(min = "0", max = "10240")
    private Integer bandwidth;
    
    @Nullable
    private Boolean enableOpenVirtualCamera;

    public Integer getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Integer bandwidth) {
        this.bandwidth = bandwidth;
    }

    public Boolean getEnableOpenVirtualCamera() {
        return enableOpenVirtualCamera;
    }

    public void setEnableOpenVirtualCamera(Boolean enableOpenVirtualCamera) {
        this.enableOpenVirtualCamera = enableOpenVirtualCamera;
    }
    
}
