package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsQrCodeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.QrCodeErrorEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: uws二维码登录
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-12-06 14:56:00
 *
 * @author zjy
 */
@DispatcherImplemetion(ShineAction.UWS_QR_START_LOGIN)
public class UwsQtStartLoginSPIImpl extends AbstractLoginSPI implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UwsQtStartLoginSPIImpl.class);

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private UwsQrCodeAPI qrCodeAPI;

    @Autowired
    private UserService userServiceImpl;

    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "cbbDispatcherRequest is not null");

        LOGGER.info("接收到二维码登录信息，请求参数为:{}", JSON.toJSONString(cbbDispatcherRequest.getData()));
        CbbDispatcherDTO dispatcherDTO = new CbbDispatcherDTO();
        BeanUtils.copyProperties(cbbDispatcherRequest, dispatcherDTO);
        Assert.hasText(dispatcherDTO.getData(), "data is not null");

        CbbQrCodeDTO qrCodeDTO;
        try {
            JSONObject data = JSONObject.parseObject(dispatcherDTO.getData());
            CbbQrCodeReqDTO qrCodeClientReq = new CbbQrCodeReqDTO();
            qrCodeClientReq.setClientId(cbbDispatcherRequest.getTerminalId());
            qrCodeClientReq.setQrCodeType(CbbQrCodeType.UWS);
            qrCodeClientReq.setQrCode(data.getString("authCode"));
            qrCodeDTO = qrCodeAPI.qrLogin(qrCodeClientReq);
        } catch (BusinessException e) {
            LOGGER.error("请求二维码登录发生业务异常", e);
            QrCodeErrorEnum qrCodeErrorEnum = QrCodeErrorEnum.getQrCodeErrorEnum(e.getKey());
            Integer errorCode = qrCodeErrorEnum == null ? Constants.FAILURE : qrCodeErrorEnum.getCode();
            messageHandlerAPI.response(ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, errorCode));
            return;
        } catch (Exception e) {
            LOGGER.error("请求二维码登录发生异常", e);
            messageHandlerAPI.response(ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, Constants.FAILURE));
            return;
        }

        // 复用Shine登录
        JSONObject userInfo = new JSONObject();
        try {
            JSONObject userDataJson = JSONObject.parseObject(qrCodeDTO.getUserData());
            RcoViewUserEntity userEntity = userServiceImpl.getUserInfoByName(userDataJson.getString("userName"));
            userInfo.put("userName", userEntity.getUserName());
            userInfo.put("password", userEntity.getPassword());
        } catch (Exception ex) {
            LOGGER.error("获取用户信息发生异常", ex);
            messageHandlerAPI.response(ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, Constants.FAILURE));
            return;
        }
        cbbDispatcherRequest.setData(userInfo.toJSONString());
        super.dispatch(cbbDispatcherRequest);
    }
}
