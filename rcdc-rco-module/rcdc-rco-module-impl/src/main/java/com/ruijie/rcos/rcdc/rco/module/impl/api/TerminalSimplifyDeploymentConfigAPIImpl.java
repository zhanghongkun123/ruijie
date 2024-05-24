package com.ruijie.rcos.rcdc.rco.module.impl.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalSimplifyDeploymentConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalSimplifyDeploymentConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Description: 终端极简部署模式API
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/6 16:19
 *
 * @author linrenjian
 */
public class TerminalSimplifyDeploymentConfigAPIImpl implements TerminalSimplifyDeploymentConfigAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalSimplifyDeploymentConfigAPIImpl.class);

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private ViewTerminalDAO viewTerminalDAO;


    @Override
    public void modifyTerminalSimplifyDeploymentConfig(TerminalSimplifyDeploymentConfigDTO configDTO) {
        Assert.notNull(configDTO, "configDTO must not be null");
        LOGGER.info("修改终端极简部署模式为[{}]", configDTO.getEnableTerminalSimplifyDeployment());
        String value = JSON.toJSONString(configDTO);
        globalParameterService.updateParameter(Constants.TERMINAL_SIMPLIFY_DEPLOYMENT_CONFIG, value);
    }

    @Override
    public TerminalSimplifyDeploymentConfigDTO getTerminalSimplifyDeploymentConfig() {

        String terminalSimplifyDeploymentConfig = globalParameterService.findParameter(Constants.TERMINAL_SIMPLIFY_DEPLOYMENT_CONFIG);
        return JSONObject.parseObject(terminalSimplifyDeploymentConfig, TerminalSimplifyDeploymentConfigDTO.class);
    }

}
