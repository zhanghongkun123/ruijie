package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.ruijie.rcos.rcdc.rco.module.def.api.ConfigurationWizardAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ConfigurationWizardDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.GetConfigurationWizardRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SetConfigurationWizardRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetConfigurationWizardResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoGlobalParameterEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;


/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/4/1 <br>
 *
 * @author yyz
 */
public class ConfigurationWizardAPIImpl implements ConfigurationWizardAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationWizardAPIImpl.class);

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private GlobalParameterService globalParameterService;

    private static final String CONFIGURATION_WIZARD_PARAM_KEY = "configuration_wizard";

    @Override
    public GetConfigurationWizardResponse getConfigurationWizard(GetConfigurationWizardRequest request) {
        Assert.notNull(request, "request is not null");

        final GetConfigurationWizardResponse response = new GetConfigurationWizardResponse();

        ConfigurationWizardDTO configurationWizard = getConfigurationWizard();
        response.setShow(configurationWizard.getShow());
        response.setIndex(configurationWizard.getIndex());
        response.setIsJoinUserExperiencePlan(configurationWizard.isJoinUserExperiencePlan());
        response.setCustomDataArr(configurationWizard.getCustomDataArr());
        return response;
    }

    @Override
    public GetConfigurationWizardResponse getConfigurationWizardResponse() {
        final GetConfigurationWizardResponse response = new GetConfigurationWizardResponse();

        ConfigurationWizardDTO configurationWizard = getConfigurationWizard();
        response.setShow(configurationWizard.getShow());
        response.setIndex(configurationWizard.getIndex());
        response.setIsJoinUserExperiencePlan(configurationWizard.isJoinUserExperiencePlan());
        response.setCustomDataArr(configurationWizard.getCustomDataArr());
        return response;
    }

    @Override
    public DefaultResponse setConfigurationWizard(SetConfigurationWizardRequest request) {
        Assert.notNull(request, "request is not null");

        final ConfigurationWizardDTO configurationWizardDTO = new ConfigurationWizardDTO();
        configurationWizardDTO.setIndex(request.getIndex());
        configurationWizardDTO.setShow(request.getShow());
        configurationWizardDTO.setJoinUserExperiencePlan(request.getIsJoinUserExperiencePlan());
        configurationWizardDTO.setCustomDataArr(request.getCustomDataArr());
        setConfigurationWizard(configurationWizardDTO);
        return DefaultResponse.Builder.success();
    }

    /**
     *  获取配置
     * @return ConfigurationWizardDTO
     */
    public ConfigurationWizardDTO getConfigurationWizard() {

        String parameter = globalParameterAPI.findParameter(CONFIGURATION_WIZARD_PARAM_KEY);

        try {
            return JSON.parseObject(parameter, ConfigurationWizardDTO.class);
        } catch (JSONException e) {
            LOGGER.error(" 获取配置向导信息失败：{}，信息：{}", e.getMessage(), parameter);
            throw new IllegalStateException("获取配置向导信息失败：" + parameter, e);
        }
    }

    /**
     * 设置配置
     * @param configurationWizard configurationWizard
     */
    public void setConfigurationWizard(ConfigurationWizardDTO configurationWizard) {
        Assert.notNull(configurationWizard, "configurationWizard is not null");

        String paramValue = JSON.toJSON(configurationWizard).toString();
        globalParameterAPI.updateParameter(CONFIGURATION_WIZARD_PARAM_KEY, paramValue);

    }

    @Override
    public void markAsS2Version() {
        String paramValue = globalParameterService.findParameter(Constants.RCDC_RCO_MARK_AS_S2_VERSION);
        if (paramValue != null) {
            LOGGER.info("当前版本已标识为S2版本，无需重复标识");
            return;
        }
        Date createTime = new Date();
        RcoGlobalParameterEntity globalParameterEntity = new RcoGlobalParameterEntity();
        globalParameterEntity.setParamKey(Constants.RCDC_RCO_MARK_AS_S2_VERSION);
        globalParameterEntity.setParamValue(Boolean.TRUE.toString());
        globalParameterEntity.setDefaultValue(Boolean.TRUE.toString());
        globalParameterEntity.setCreateTime(createTime);
        globalParameterEntity.setUpdateTime(createTime);
        LOGGER.info("将当前版本标识为S2版本，全局配置信息为：[{}]", JSON.toJSONString(globalParameterEntity));
        globalParameterService.saveParameter(globalParameterEntity);
    }
}
