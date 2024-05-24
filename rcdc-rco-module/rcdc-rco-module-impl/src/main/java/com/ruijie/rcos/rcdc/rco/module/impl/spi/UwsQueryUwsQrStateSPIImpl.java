package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsQrCodeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQueryQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.QrCodeErrorEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: uws获取二维码状态
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-12-06 14:56:00
 *
 * @author zjy
 */
@DispatcherImplemetion(ShineAction.QUERY_UWS_QR_STATE)
public class UwsQueryUwsQrStateSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UwsQueryUwsQrStateSPIImpl.class);

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private UwsQrCodeAPI qrCodeAPI;

    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "cbbDispatcherRequest is not null");

        LOGGER.info("接收到获取二维码状态信息，请求终端为:{}", cbbDispatcherRequest.getTerminalId());
        CbbDispatcherDTO dispatcherDTO = new CbbDispatcherDTO();
        BeanUtils.copyProperties(cbbDispatcherRequest, dispatcherDTO);
        Assert.hasText(dispatcherDTO.getData(), "data is not null");

        CbbResponseShineMessage shineMessage;
        try {
            JSONObject data = JSONObject.parseObject(dispatcherDTO.getData());
            CbbQueryQrCodeReqDTO qrCodeMobileReq = new CbbQueryQrCodeReqDTO();
            qrCodeMobileReq.setQrCode(data.getString("authCode"));
            qrCodeMobileReq.setQrCodeType(CbbQrCodeType.UWS);
            CbbQrCodeDTO qrCodeDTO = qrCodeAPI.queryQrCode(qrCodeMobileReq);
            shineMessage = ShineMessageUtil.buildResponseMessage(cbbDispatcherRequest, qrCodeDTO);
        } catch (BusinessException e) {
            LOGGER.error("请求二维码状态发生业务异常, 异常信息 ex : {}", e);
            QrCodeErrorEnum qrCodeErrorEnum = QrCodeErrorEnum.getQrCodeErrorEnum(e.getKey());
            Integer errorCode = qrCodeErrorEnum == null ? Constants.FAILURE : qrCodeErrorEnum.getCode();
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, errorCode);
        } catch (Exception e) {
            LOGGER.error("请求二维码状态发生异常, 异常信息 ex : {}", e);
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, Constants.FAILURE);
        }
        messageHandlerAPI.response(shineMessage);
    }
}
