package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;


import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopNetworkDTO;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月27日
 * 
 * @author nt
 */
public class DesktopNetworkDetailVO {
    
    private DesktopNetworkDTO network;

    public DesktopNetworkDetailVO(DesktopNetworkDTO network) {
        this.network = network;
    }

    public DesktopNetworkDTO getNetwork() {
        return network;
    }

    public void setNetwork(DesktopNetworkDTO network) {
        this.network = network;
    }
    
}
