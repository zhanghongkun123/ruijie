package com.ruijie.rcos.rcdc.rco.module.openapi.rest.uwsdocking.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.UwsBaseDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.uwsdocking.UwsCommonRestServer;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: 普通用户服务实现类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-20 13:59:00
 *
 * @author zjy
 */
@Service
public class UwsCommonRestServerImpl implements UwsCommonRestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UwsCommonRestServerImpl.class);

    @Autowired
    private UwsDockingAPI uwsDockingAPI;


    @Override
    public UwsBaseDTO initCmApp() {
        uwsDockingAPI.initCmApp();
        uwsDockingAPI.initCmISO();
        return new UwsBaseDTO(CommonMessageCode.SUCCESS);
    }
}
