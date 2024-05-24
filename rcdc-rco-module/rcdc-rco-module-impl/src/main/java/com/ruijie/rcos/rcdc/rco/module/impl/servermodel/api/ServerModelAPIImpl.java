package com.ruijie.rcos.rcdc.rco.module.impl.servermodel.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums.ServerModelEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.servermodel.service.ServerModelService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: ServerModelAPIImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年9月4日
 *
 * @author wjp
 */
public class ServerModelAPIImpl implements ServerModelAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerModelAPIImpl.class);

    @Autowired
    private ServerModelService serverModelService;

    @Override
    public String getServerModel() {
        String serverModel = serverModelService.getServerModelFlag().getValue();
        LOGGER.debug("获取服务器模式成功。部署模式 = {}", serverModel);
        return serverModel;
    }

    @Override
    public boolean isVdiModel() {
        return getServerModel().equals(ServerModelEnum.VDI_SERVER_MODEL.getName());
    }

    @Override
    public boolean isIdvModel() {
        return getServerModel().equals(ServerModelEnum.IDV_SERVER_MODEL.getName());
    }

    @Override
    public boolean isMiniModel() {
        return getServerModel().equals(ServerModelEnum.MINI_SERVER_MODEL.getName());
    }

    @Override
    public boolean isInitModel() {
        return getServerModel().equals(ServerModelEnum.INIT_SERVER_MODEL.getName());
    }

}
