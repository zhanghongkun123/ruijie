package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClientQrCodeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientQrCodeMobileReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientQrLoginReq;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.response.MobileClientQrCodeResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.QrCodeServer;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;


/**
 * Description: QrCodeServer实现类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-14 20:48
 *
 * @author wanglianyun
 */
public class QrCodeServerImpl implements QrCodeServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(QrCodeServerImpl.class);

    @Autowired
    private ClientQrCodeAPI clientQrCodeAPI;


    @Override
    public MobileClientQrCodeResponse scanQrCode(String terminalId, ClientQrCodeMobileReq request) {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(request, "request must not be null");

        LOGGER.info("收到移动客户端扫码请求，请求信息为：{}", JSON.toJSON(request));
        MobileClientQrCodeResponse mobileClientQrCodeResponse = new MobileClientQrCodeResponse();
        try {
            clientQrCodeAPI.scanQrCode(request);
        } catch (BusinessException ex) {
            LOGGER.error("移动客户端扫码异常", ex);
            mobileClientQrCodeResponse.setCode(Integer.parseInt(ex.getKey()));
            mobileClientQrCodeResponse.setMsg(ex.getI18nMessage());
            mobileClientQrCodeResponse.setException(ex);
        }

        return mobileClientQrCodeResponse;

    }

    @Override
    public MobileClientQrCodeResponse confirmLogin(String terminalId, ClientQrLoginReq request) {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(request, "request must not be null");

        LOGGER.info("收到移动客户端扫码确认登录请求，请求信息为：{}", JSON.toJSON(request));
        MobileClientQrCodeResponse mobileClientQrCodeResponse = new MobileClientQrCodeResponse();
        try {
            clientQrCodeAPI.confirmQrLogin(request);
        } catch (BusinessException ex) {
            LOGGER.error("移动客户端扫码确认扫码异常", ex);
            mobileClientQrCodeResponse.setCode(Integer.parseInt(ex.getKey()));
            mobileClientQrCodeResponse.setMsg(ex.getI18nMessage());
            mobileClientQrCodeResponse.setException(ex);
        }

        return mobileClientQrCodeResponse;
    }

    @Override
    public MobileClientQrCodeResponse cancelQrCode(String terminalId, ClientQrCodeMobileReq request) {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(request, "request must not be null");

        LOGGER.info("收到移动客户端扫码取消登录请求，请求信息为：{}", JSON.toJSON(request));
        MobileClientQrCodeResponse mobileClientQrCodeResponse = new MobileClientQrCodeResponse();
        try {
            clientQrCodeAPI.cancelQrLogin(request);
        } catch (BusinessException ex) {
            LOGGER.error("移动客户端扫码取消登录异常", ex);
            mobileClientQrCodeResponse.setCode(Integer.parseInt(ex.getKey()));
            mobileClientQrCodeResponse.setMsg(ex.getI18nMessage());
            mobileClientQrCodeResponse.setException(ex);
        }

        return mobileClientQrCodeResponse;
    }
}
