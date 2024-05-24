package com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin.impl;


import com.ruijie.rcos.rcdc.rco.module.def.api.QrLoginAPI;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin.QrLoginRestServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin.enums.QrLoginRespEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin.request.QrLoginRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin.response.QrLoginRestServerResponse;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 代理服务器,登录缓存
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/28 20:01
 *
 * @author zhang.zhiwen
 */
@Service
public class QrLoginRestServerImpl implements QrLoginRestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(QrLoginRestServerImpl.class);

    @Autowired
    private QrLoginAPI qrLoginAPI;

    @Override
    public QrLoginRestServerResponse saveCache(QrLoginRestServerRequest request) {
        Assert.notNull(request, "QrLoginRestServerRequest is not null");
        UUID id = request.getId();
        String userName = request.getUserName();
        try {
            // 存储用户缓存
            qrLoginAPI.saveQrLoginId(id, userName);
            return new QrLoginRestServerResponse(QrLoginRespEnum.SUCCESS);
        } catch (Exception e) {
            LOGGER.error("代理扫码登录：缓存失败。userName = {}", userName, e);
            return new QrLoginRestServerResponse(QrLoginRespEnum.FAIL);
        }
    }
}
