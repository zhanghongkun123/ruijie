package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.usbadvancesetting;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月23日
 * 
 * @author zhuangchenwu
 */
public class CameraConfigStrategyVO {

    private Integer bandwidth;
    
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
