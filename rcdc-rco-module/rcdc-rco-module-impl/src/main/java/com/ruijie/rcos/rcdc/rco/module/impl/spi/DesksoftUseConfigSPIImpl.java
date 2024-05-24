package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesksoftUseConfigNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesksoftUseConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.enums.GuesttoolMessageResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 收到GuestTool请求获取RCDC上报开关信息信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/15
 *
 * @author linrenjian
 */

@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_DESKSOFT_USE_CONFIG)
public class DesksoftUseConfigSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesksoftUseConfigSPIImpl.class);


    @Autowired
    private DesksoftUseConfigNotifyAPI desksoftUseConfigNotifyAPI;


    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(request.getDto(), "dto can not be null");

        LOGGER.info("收到GuestTool请求获取RCDC上报开关信息信息:{}", JSON.toJSONString(request));

        return getCbbGuesttoolMessageDTO(request.getDto(), GuesttoolMessageResultTypeEnum.SUCCESS);
    }

    private CbbGuesttoolMessageDTO getCbbGuesttoolMessageDTO(CbbGuesttoolMessageDTO dto, GuesttoolMessageResultTypeEnum resultTypeEnum) {
        final CbbGuesttoolMessageDTO responseDto = new CbbGuesttoolMessageDTO();
        responseDto.setCmdId(dto.getCmdId());
        responseDto.setPortId(dto.getPortId());
        responseDto.setDeskId(dto.getDeskId());
        responseDto.setTerminalId(dto.getTerminalId());
        // 获取CMC 软件软件使用是否上报开关
        DesksoftUseConfigDTO desksoftUseConfigDTO = desksoftUseConfigNotifyAPI.getGlobalCmcStrategy();
        // 配置默认code、message、content值
        GuesttoolMessageContent messageContent = new GuesttoolMessageContent();
        messageContent.setCode(resultTypeEnum.getCode());
        messageContent.setMessage(resultTypeEnum.getMessage());
        messageContent.setContent(desksoftUseConfigDTO);

        responseDto.setBody(JSON.toJSONString(messageContent));
        return responseDto;
    }


}
