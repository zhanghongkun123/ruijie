package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums.ServerModelEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.servermodel.ServerModelBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.servermodel.service.ServerModelService;
import com.ruijie.rcos.sk.base.config.ConfigFacadeHolder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.12.29
 *
 * @author linhj
 */
@Service
public class EstClientService {

    @Autowired
    private ServerModelService serverModelService;

    // vdi 配置大小
    private final static String CONFIG_VDI_LIMIT_KEY = "rcdc.estclient.limit.vdi.num";

    // idv 配置大小
    private final static String CONFIG_IDV_LIMIT_KEY = "rcdc.estclient.limit.idv.num";

    // min 配置大小
    private final static String CONFIG_MINI_LIMIT_KEY = "rcdc.estclient.limit.mini.num";

    // 默认限制大小
    private final static Integer CONFIG_DEFAULT_VALUE = 15;

    /**
     * 获取 EstClient 限制大小
     *
     * @return 限制数量
     * @throws BusinessException 获取部署模式失败
     */
    public int estClientLimit() throws BusinessException {
        ServerModelEnum serverModel;
        FindParameterResponse response = serverModelService.getServerModelFlag();
        if (StringUtils.isNotBlank(response.getValue()) && !response.getValue().equals(ServerModelEnum.INIT_SERVER_MODEL.getName())) {
            serverModel = ServerModelEnum.getByName(response.getValue());
        } else {
            serverModel = serverModelService.getServerModel();
        }
        switch (serverModel) {
            case MINI_SERVER_MODEL:
                return obtainConfig(CONFIG_MINI_LIMIT_KEY);
            case IDV_SERVER_MODEL:
                return obtainConfig(CONFIG_IDV_LIMIT_KEY);
            case VDI_SERVER_MODEL:
                return obtainConfig(CONFIG_VDI_LIMIT_KEY);
            default:
                throw new BusinessException(ServerModelBusinessKey.RCDC_RCO_GET_SERVER_MODEL_INFO_ERROR);
        }
    }

    private Integer obtainConfig(String key) {
        return Optional.ofNullable(ConfigFacadeHolder.getFacade().read(key))
                .map(Integer::parseInt).orElse(CONFIG_DEFAULT_VALUE);
    }
}
