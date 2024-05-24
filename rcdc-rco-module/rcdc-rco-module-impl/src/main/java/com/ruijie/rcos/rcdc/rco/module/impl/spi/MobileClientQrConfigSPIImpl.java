package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClientQrCodeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.clientqr.ClientQrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.QrCodeErrorEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.UserLoginRccmOperationService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 移动客户端扫码获取二维码配置
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-15 13:53
 *
 * @author wanglianyun
 */
@DispatcherImplemetion(ShineAction.MOBILE_CLIENT_QR_CONFIG)
public class MobileClientQrConfigSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileClientQrConfigSPIImpl.class);

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private ClientQrCodeAPI clientQrCodeAPI;

    @Autowired
    private UserLoginRccmOperationService userLoginRccmOperationService;

    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "cbbDispatcherRequest is not null");

        String terminalId = cbbDispatcherRequest.getTerminalId();
        LOGGER.info("接收到获取二维码配置信息，请求终端为:{}", terminalId);
        CbbDispatcherDTO dispatcherDTO = new CbbDispatcherDTO();
        BeanUtils.copyProperties(cbbDispatcherRequest, dispatcherDTO);
        Assert.hasText(dispatcherDTO.getData(), "data is not null");

        CbbResponseShineMessage shineMessage = null;
        try {
            ClientQrCodeConfigDTO qrCodeConfig = clientQrCodeAPI.getQrCodeConfig(terminalId);
            LOGGER.info("收到gss返回的二维码配置，配置信息为：{}", JSON.toJSONString(qrCodeConfig));
            shineMessage = ShineMessageUtil.buildResponseMessage(cbbDispatcherRequest, qrCodeConfig);
        } catch (BusinessException e) {
            LOGGER.error("终端:[{}]请求gss二维码状态发生业务异常", terminalId, e);
            QrCodeErrorEnum qrCodeErrorEnum = QrCodeErrorEnum.getQrCodeErrorEnum(e.getKey());
            Integer errorCode = qrCodeErrorEnum == null ? Constants.FAILURE : qrCodeErrorEnum.getCode();
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, errorCode);
        } catch (Exception e) {
            LOGGER.error("终端:[{}]请求gss二维码状态发生异常", terminalId, e);
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, Constants.FAILURE);
        }
        messageHandlerAPI.response(shineMessage);
    }
}
