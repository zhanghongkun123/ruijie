package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.IdArrWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.NetworkConfigVO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * 
 * Description: 修改云桌面网络请求报文
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月22日
 * 
 * @author artom
 */
public class EditNeworkWebRequest extends IdArrWebRequest {

    @NotNull
    private NetworkConfigVO network;

    public NetworkConfigVO getNetwork() {
        return network;
    }

    public void setNetwork(NetworkConfigVO network) {
        this.network = network;
    }

}
