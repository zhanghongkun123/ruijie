package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rca.module.def.spi.RcaServerModelSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description: 服务器模式spi实现
 * Copyright: Copyright (c) 2022
 * Company: RuiJie Co., Ltd.
 * Create Time: 2022/6/7 3:42 下午
 *
 * @author zhouhuan
 */
public class RcaServerModelSPIImpl implements RcaServerModelSPI {

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Override
    public String getServerModel() {
        return serverModelAPI.getServerModel();
    }
}
