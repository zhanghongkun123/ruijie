package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopNetworkDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * 
 * Description: 获取云桌面策略详情响应
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月27日
 * 
 * @author nt
 */
public class DesktopNetworkDetailResponse extends DefaultResponse {

    private DesktopNetworkDTO network;

    public DesktopNetworkDTO getNetwork() {
        return network;
    }

    public void setNetwork(DesktopNetworkDTO network) {
        this.network = network;
    }

}
