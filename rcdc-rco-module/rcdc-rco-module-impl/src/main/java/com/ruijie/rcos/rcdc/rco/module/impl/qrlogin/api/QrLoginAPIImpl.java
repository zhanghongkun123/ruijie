package com.ruijie.rcos.rcdc.rco.module.impl.qrlogin.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.QrLoginAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.qrlogin.service.QrLoginService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: QrLoginAPIImpl
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年5月6日
 *
 * @author zhang.zhiwen
 */
public class QrLoginAPIImpl implements QrLoginAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(QrLoginAPIImpl.class);

    @Autowired
    private QrLoginService qrLoginService;

    @Override
    public void saveQrLoginId(UUID id, String userName) {
        Assert.notNull(id, "id can not null");
        Assert.hasText(userName, "userName can not null");
        qrLoginService.saveQrLoginId(id, userName);
    }
}
