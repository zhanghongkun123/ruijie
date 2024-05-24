package com.ruijie.rcos.rcdc.rco.module.impl.servermodel.service.impl;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ConfigCenterMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.configcenter.ServerModelResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UpdateParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums.ServerModelEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.servermodel.ServerModelBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.servermodel.service.ServerModelService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: ServerModelServiceImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-09-04
 *
 * @author wjp
 */
@Service
public class ServerModelServiceImpl implements ServerModelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerModelServiceImpl.class);

    @Autowired
    private ConfigCenterMgmtAPI configCenterMgmtAPI;

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    private static final String SERVER_MODEL = "server_model";

    private static final String CONFIG_TYPE = "public";

    private static final String CONFIG_NAME = "product_type";

    @Override
    public ServerModelEnum getServerModel() throws BusinessException {
        LOGGER.info("调用平台组件获取服务器部署信息");
        ServerModelResponse serverModelDTO = configCenterMgmtAPI.getServerModel(CONFIG_TYPE, CONFIG_NAME);
        String serverModel = serverModelDTO.getConfigContents();
        LOGGER.info("调用平台组件获取服务器部署信息，信息为<{}>", serverModel);
        for (ServerModelEnum serverModelEnum : ServerModelEnum.values()) {
            if (serverModelEnum.getName().equals(serverModel)) {
                return serverModelEnum;
            }
        }
        LOGGER.error("获取服务器部署信息失败：未找到对应的模式{}", serverModel);
        throw new BusinessException(ServerModelBusinessKey.RCDC_RCO_GET_SERVER_MODEL_INFO_ERROR);
    }

    @Override
    public FindParameterResponse getServerModelFlag() {
        return rcoGlobalParameterAPI.findParameter(new FindParameterRequest(SERVER_MODEL));
    }

    @Override
    public void updateServerModelFlag(String serverModel) {
        Assert.hasText(serverModel, "serverModel can not empty");
        rcoGlobalParameterAPI.updateParameter(new UpdateParameterRequest(SERVER_MODEL, serverModel));
    }

    @Override
    public void initServerModelFlag() {
        LOGGER.warn("执行初始化服务器部署模式标识");
        rcoGlobalParameterAPI.updateParameter(new UpdateParameterRequest(SERVER_MODEL, ServerModelEnum.INIT_SERVER_MODEL.getName()));
    }
}
