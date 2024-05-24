package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClientQrCodeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.clientqr.ClientQrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientGetQrCodeReq;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 移动客户端获取二维码
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-15 13:55
 *
 * @author wanglianyun
 */
@DispatcherImplemetion(ShineAction.QUERY_MOBILE_CLIENT_QR)
public class MobileClientQueryQrSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileClientQueryQrSPIImpl.class);

    @Autowired
    private ClientQrCodeAPI clientQrCodeAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    protected LoginBusinessServiceFactory loginBusinessServiceFactory;

    private LoginBusinessService loginBusinessService;

    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "cbbDispatcherRequest is not null");

        LOGGER.info("接收到获取二维码信息，请求参数为:{}", JSON.toJSONString(cbbDispatcherRequest.getData()));
        CbbDispatcherDTO dispatcherDTO = new CbbDispatcherDTO();
        BeanUtils.copyProperties(cbbDispatcherRequest, dispatcherDTO);
        CbbResponseShineMessage shineMessage = null;
        try {
            ClientGetQrCodeReq clientGetQrCodeReq = new ClientGetQrCodeReq();
            clientGetQrCodeReq.setClientId(cbbDispatcherRequest.getTerminalId());
            ClientQrCodeDTO clientQrCodeDTO = clientQrCodeAPI.getQrCode(clientGetQrCodeReq);
            LOGGER.info("收到gss返回的二维码信息，二维码信息为：{}", JSON.toJSONString(clientQrCodeDTO));
            shineMessage = ShineMessageUtil.buildResponseMessage(cbbDispatcherRequest, clientQrCodeDTO);
        } catch (BusinessException e) {
            LOGGER.error("请求gss二维码发生业务异常", e);
            QrCodeErrorEnum qrCodeErrorEnum = QrCodeErrorEnum.getQrCodeErrorEnum(e.getKey());
            Integer errorCode = qrCodeErrorEnum == null ? Constants.FAILURE : qrCodeErrorEnum.getCode();
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, errorCode);
        } catch (Exception e) {
            LOGGER.error("请求gss二维码发生异常", e);
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, Constants.FAILURE);
        }
        saveLoginInfo(cbbDispatcherRequest.getTerminalId());
        messageHandlerAPI.response(shineMessage);
    }

    private void saveLoginInfo(String terminalId) {
        try {
            loginBusinessService = loginBusinessServiceFactory.getLoginBusinessService(ShineAction.MOBILE_CLIENT_QR_START_LOGIN);
            loginBusinessService.saveUserLoginInfo(terminalId, null);
        } catch (Exception e) {
            LOGGER.error("锐捷客户端扫码获取gss二维码时，保存终端[{}]登录信息失败", terminalId, e);
        }
    }
}
