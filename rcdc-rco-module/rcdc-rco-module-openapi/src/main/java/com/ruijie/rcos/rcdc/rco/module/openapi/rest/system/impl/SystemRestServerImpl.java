package com.ruijie.rcos.rcdc.rco.module.openapi.rest.system.impl;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.system.SystemRestServer;
import org.springframework.stereotype.Service;


/**
 * Description: 系统
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-09-23
 *
 * @author zqj
 */
@Service
public class SystemRestServerImpl implements SystemRestServer {


    @Override
    public Object heartBeat() {
        return "成功";
    }
}
