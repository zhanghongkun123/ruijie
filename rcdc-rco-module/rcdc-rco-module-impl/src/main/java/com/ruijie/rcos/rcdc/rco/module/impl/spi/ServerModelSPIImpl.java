package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.backup.module.def.spi.ServerModelSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description: 获取服务器模式
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/8 11:50
 *
 * @author coderLee23
 */
public class ServerModelSPIImpl implements ServerModelSPI {

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Override
    public String getServerModel() {
        return serverModelAPI.getServerModel();
    }
}
