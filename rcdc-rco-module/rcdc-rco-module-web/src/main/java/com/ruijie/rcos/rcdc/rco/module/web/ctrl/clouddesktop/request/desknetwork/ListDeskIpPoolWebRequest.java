package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desknetwork;

import java.util.UUID;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

/**
 * Description: 分页查询IP池web请求 
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月13日
 * 
 * @author zouqi
 */
public class ListDeskIpPoolWebRequest extends PageWebRequest {
    
    /**
     * 网络策略ID
     */
    @NotNull
    private UUID deskNetworkId;

    public UUID getDeskNetworkId() {
        return deskNetworkId;
    }

    public void setDeskNetworkId(UUID deskNetworkId) {
        this.deskNetworkId = deskNetworkId;
    }
}
