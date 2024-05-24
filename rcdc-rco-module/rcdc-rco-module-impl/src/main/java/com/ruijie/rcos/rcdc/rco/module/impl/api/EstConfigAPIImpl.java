package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.EstConfigAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbEstConfigDTO;
import com.ruijie.rcos.rcdc.rca.module.def.constants.EstConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: EST配置API实现类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/17 15:52
 *
 * @author yxq
 */
public class EstConfigAPIImpl implements EstConfigAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstConfigAPIImpl.class);

    @Autowired
    private GlobalParameterService globalParameterService;

    @Override
    public CbbEstConfigDTO getEstWanConfig() {
        String paramValue = globalParameterService.findParameter(EstConstants.EST_WAN_CONFIG_KEY);
        LOGGER.debug("查询的广域网EST配置为：[{}]", paramValue);

        return JSON.parseObject(paramValue, CbbEstConfigDTO.class);
    }

    @Override
    public CbbEstConfigDTO getEstLanConfig() {
        String paramValue = globalParameterService.findParameter(EstConstants.EST_LAN_CONFIG_KEY);
        LOGGER.debug("查询的局域网EST配置为：[{}]", paramValue);

        return JSON.parseObject(paramValue, CbbEstConfigDTO.class);
    }

    @Override
    public void editEstWanConfig(CbbEstConfigDTO estConfigDTO) {
        Assert.notNull(estConfigDTO, "estConfigDTO must not be null");

        String paramValue = JSON.toJSONString(estConfigDTO);
        globalParameterService.updateParameter(EstConstants.EST_WAN_CONFIG_KEY, paramValue);
    }

    @Override
    public void editEstLanConfig(CbbEstConfigDTO estConfigDTO) {
        Assert.notNull(estConfigDTO, "estConfigDTO must not be null");

        String paramValue = JSON.toJSONString(estConfigDTO);
        globalParameterService.updateParameter(EstConstants.EST_LAN_CONFIG_KEY, paramValue);
    }

    @Override
    public void resetEstLanConfig() {
        String defaultValue = globalParameterService.findDefaultValue(EstConstants.EST_LAN_CONFIG_KEY);
        globalParameterService.updateParameter(EstConstants.EST_LAN_CONFIG_KEY, defaultValue);
    }

    @Override
    public void resetEstWanConfig() {
        String defaultValue = globalParameterService.findDefaultValue(EstConstants.EST_WAN_CONFIG_KEY);
        globalParameterService.updateParameter(EstConstants.EST_WAN_CONFIG_KEY, defaultValue);
    }
}
