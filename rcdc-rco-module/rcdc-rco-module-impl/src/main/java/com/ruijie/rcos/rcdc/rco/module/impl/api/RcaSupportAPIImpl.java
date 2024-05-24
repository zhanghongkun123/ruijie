package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.AppTerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.RcacRestClient;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.request.AppTerminalDetailRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.response.AppTerminalDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/22 10:27
 *
 * @author zhangyichi
 */
public class RcaSupportAPIImpl implements RcaSupportAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaSupportAPIImpl.class);

    @Autowired
    private RcacRestClient rcacRestClient;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Override
    public AppTerminalDTO getAppTerminalDetail(Integer appTerminalId) throws BusinessException {
        Assert.notNull(appTerminalId, "appTerminalId cannot be null!");

        AppTerminalDetailRequest request = new AppTerminalDetailRequest(appTerminalId);
        AppTerminalDetailResponse detail = rcacRestClient.getAppTerminalDetail(request);
        AppTerminalDTO terminalDTO = detail.getContent();
        if (terminalDTO == null || StringUtils.isEmpty(terminalDTO.getState())) {
            LOGGER.error("云应用客户端[{}]不存在", appTerminalId);
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_APP_TERMINAL_NOT_EXIST);
        }
        return terminalDTO;
    }

    @Override
    public void modifyVirtualApplicationState(Boolean enable) throws BusinessException {
        Assert.notNull(enable, "enable cannot be null!");

        LOGGER.info("全局表中虚拟应用开关修改为：[{}]", enable);
        globalParameterService.updateParameter(Constants.VIRTUAL_APPLICATION_STATE, enable.toString());
    }

    @Override
    public Boolean getVirtualApplicationState() {
        return Boolean.valueOf(globalParameterService.findParameter(Constants.VIRTUAL_APPLICATION_STATE));
    }
}
