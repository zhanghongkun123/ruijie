package com.ruijie.rcos.rcdc.rco.module.impl.servermodel.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.ServerModelSPI;
import org.springframework.beans.factory.annotation.Autowired;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums.ServerModelEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.servermodel.service.ServerModelService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: CBB通过RCO获取服务器部署模式
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年1月4日
 *
 * @author lifeng
 */
public class CloudDesktopServerModelSPIImpl implements ServerModelSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDesktopServerModelSPIImpl.class);

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

}
