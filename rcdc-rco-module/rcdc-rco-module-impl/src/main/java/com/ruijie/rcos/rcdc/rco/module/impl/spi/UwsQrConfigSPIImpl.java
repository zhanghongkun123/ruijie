package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsQrCodeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.QrCodeErrorEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.UserLoginRccmOperationService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: uws获取二维码配置
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-03-09 17:56:00
 *
 * @author zjy
 */
@DispatcherImplemetion(ShineAction.UWS_QR_CONFIG)
public class UwsQrConfigSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UwsQrConfigSPIImpl.class);

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private UwsQrCodeAPI qrCodeAPI;

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

        CbbResponseShineMessage shineMessage;
        try {
            CbbQrCodeConfigDTO qrCodeConfig = qrCodeAPI.getQrCodeConfig(CbbQrCodeType.UWS);
            // UWS扫码开启时,判断rcdc是否开启统一登录且是软终端或VDI
            if (qrCodeConfig.getOpenSwitch() && userLoginRccmOperationService.isUnifiedLoginOn(terminalId)) {
                qrCodeConfig.setOpenSwitch(false);
            }
            shineMessage = ShineMessageUtil.buildResponseMessage(cbbDispatcherRequest, qrCodeConfig);
        } catch (BusinessException e) {
            LOGGER.error("终端:[{}]请求二维码状态发生业务异常, 异常信息 ex : {}", terminalId, e);
            QrCodeErrorEnum qrCodeErrorEnum = QrCodeErrorEnum.getQrCodeErrorEnum(e.getKey());
            Integer errorCode = qrCodeErrorEnum == null ? Constants.FAILURE : qrCodeErrorEnum.getCode();
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, errorCode);
        } catch (Exception e) {
            LOGGER.error("终端:[{}]请求二维码状态发生异常, 异常信息 ex : {}", terminalId, e);
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, Constants.FAILURE);
        }
        messageHandlerAPI.response(shineMessage);
    }
}
