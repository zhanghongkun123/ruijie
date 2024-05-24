package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesksoftUseConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CmcConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 获取CMC 配置
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/01
 *
 * @author linrenjian
 */
@DispatcherImplemetion(ShineAction.GET_CMC_CONFIG_STATUS)
public class GetCmcConfigServiceSPIImpl implements CbbDispatcherHandlerSPI {

    public static final Logger LOGGER = LoggerFactory.getLogger(GetCmcConfigServiceSPIImpl.class);

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request can not be null");
        // 获取CMC 软件软件使用是否上报开关
        String parameter = globalParameterService.findParameter(Constants.RCDC_GT_DESKSOFT_MSG_STATUS);
        DesksoftUseConfigDTO desksoftUseConfigDTO = new DesksoftUseConfigDTO();
        Boolean enableCmc = Boolean.valueOf(parameter);
        desksoftUseConfigDTO.setDesksoftMsgStatus(enableCmc);
        desksoftUseConfigDTO.setCmcStatus(enableCmc ? CmcConstants.CMC_ON : CmcConstants.CMC_OFF);
        response(request, Constants.SUCCESS, desksoftUseConfigDTO);
    }


    private void response(CbbDispatcherRequest request, Integer code, DesksoftUseConfigDTO desksoftUseConfigDTO) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, desksoftUseConfigDTO);
        } catch (Exception e) {
            LOGGER.error("终端{}获取CMC 软件软件使用是否上报开关失败，e={}", request.getTerminalId(), e);
        }
    }
}
