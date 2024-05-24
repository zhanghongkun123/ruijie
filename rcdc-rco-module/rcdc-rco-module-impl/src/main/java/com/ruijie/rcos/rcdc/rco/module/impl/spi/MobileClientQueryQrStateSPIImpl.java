package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClientQrCodeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.clientqr.ClientQrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientQrCodeMobileReq;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.QrCodeErrorEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 移动客户端获取二维码状态
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-15 13:57
 *
 * @author wanglianyun
 */
@DispatcherImplemetion(ShineAction.QUERY_MOBILE_CLIENT_QR_STATE)
public class MobileClientQueryQrStateSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileClientQueryQrStateSPIImpl.class);

    @Autowired
    private ClientQrCodeAPI clientQrCodeAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "cbbDispatcherRequest is not null");

        LOGGER.info("收到移动客户端获取二维码状态请求，终端id为：{}", cbbDispatcherRequest.getTerminalId());
        CbbDispatcherDTO dispatcherDTO = new CbbDispatcherDTO();
        BeanUtils.copyProperties(cbbDispatcherRequest, dispatcherDTO);
        Assert.hasText(dispatcherDTO.getData(), "data is not null");
        CbbResponseShineMessage shineMessage = null;

        try {
            JSONObject data = JSONObject.parseObject(dispatcherDTO.getData());
            ClientQrCodeMobileReq clientQrCodeMobileReq = new ClientQrCodeMobileReq();
            clientQrCodeMobileReq.setQrCode(data.getString("authCode"));
            ClientQrCodeDTO clientQrCodeDTO = clientQrCodeAPI.queryQrCode(clientQrCodeMobileReq);
            LOGGER.info("收到gss返回的二维码状态信息，状态信息为：{}", JSON.toJSONString(clientQrCodeDTO));
            shineMessage = ShineMessageUtil.buildResponseMessage(cbbDispatcherRequest, clientQrCodeDTO);
        } catch (BusinessException e) {
            LOGGER.error("请求gss二维码状态发生业务异常", e);
            QrCodeErrorEnum qrCodeErrorEnum = QrCodeErrorEnum.getQrCodeErrorEnum(e.getKey());
            Integer errorCode = qrCodeErrorEnum == null ? Constants.FAILURE : qrCodeErrorEnum.getCode();
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, errorCode);
        } catch (Exception e) {
            LOGGER.error("请求gss二维码状态发生异常", e);
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, Constants.FAILURE);
        }
        messageHandlerAPI.response(shineMessage);
    }
}
