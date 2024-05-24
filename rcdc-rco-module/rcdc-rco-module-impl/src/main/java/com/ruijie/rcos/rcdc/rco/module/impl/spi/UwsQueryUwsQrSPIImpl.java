package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsQrCodeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbGetQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.QrCodeErrorEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessServiceFactory;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: uws获取二维码
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-12-06 14:56:00
 *
 * @author zjy
 */
@DispatcherImplemetion(ShineAction.QUERY_UWS_QR)
public class UwsQueryUwsQrSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UwsQueryUwsQrSPIImpl.class);

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private UwsQrCodeAPI qrCodeAPI;

    @Autowired
    protected LoginBusinessServiceFactory loginBusinessServiceFactory;

    private LoginBusinessService loginBusinessService;

    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "cbbDispatcherRequest is not null");

        LOGGER.info("接收到获取二维码信息，请求参数为:{}", JSON.toJSONString(cbbDispatcherRequest.getData()));
        CbbDispatcherDTO dispatcherDTO = new CbbDispatcherDTO();
        BeanUtils.copyProperties(cbbDispatcherRequest, dispatcherDTO);

        CbbGetQrCodeReqDTO qrCodeReq = new CbbGetQrCodeReqDTO();
        qrCodeReq.setQrCodeType(CbbQrCodeType.UWS);
        qrCodeReq.setClientId(cbbDispatcherRequest.getTerminalId());
        CbbResponseShineMessage shineMessage;
        try {
            CbbQrCodeDTO qrCode = qrCodeAPI.getQrCode(qrCodeReq);
            shineMessage = ShineMessageUtil.buildResponseMessage(cbbDispatcherRequest, qrCode);
        } catch (BusinessException e) {
            LOGGER.error("请求二维码发生业务异常, 异常信息 ex : {}", e);
            QrCodeErrorEnum qrCodeErrorEnum = QrCodeErrorEnum.getQrCodeErrorEnum(e.getKey());
            Integer errorCode = qrCodeErrorEnum == null ? Constants.FAILURE : qrCodeErrorEnum.getCode();
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, errorCode);
        } catch (Exception e) {
            LOGGER.error("请求二维码发生异常, 异常信息 ex : {}", e);
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, Constants.FAILURE);
        }
        saveLoginInfo(cbbDispatcherRequest.getTerminalId());
        messageHandlerAPI.response(shineMessage);
    }

    private void saveLoginInfo(String terminalId) {
        try {
            loginBusinessService = loginBusinessServiceFactory.getLoginBusinessService(ShineAction.UWS_QR_START_LOGIN);
            loginBusinessService.saveUserLoginInfo(terminalId, null);
        } catch (Exception e) {
            LOGGER.error("获取UWS二维码时，保存终端[{}]登录信息失败", terminalId, e);
        }
    }
}
