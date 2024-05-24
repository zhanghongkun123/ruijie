package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.param;

import java.util.UUID;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.NetworkConfigVO;

/**
 * 编辑云桌面的网络策略参数类
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月22日
 * 
 * @author zhuangchenwu
 */
public class EditNetworkBatchParam {

    private UUID[] idArr;
    
    private NetworkConfigVO network;
    
    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

    public NetworkConfigVO getNetwork() {
        return network;
    }

    public void setNetwork(NetworkConfigVO network) {
        this.network = network;
    }

}
