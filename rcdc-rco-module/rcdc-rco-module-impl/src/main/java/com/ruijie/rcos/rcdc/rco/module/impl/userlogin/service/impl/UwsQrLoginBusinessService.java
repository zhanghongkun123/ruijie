package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.impl;

import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.modulekit.api.comm.SpiCustomThreadPoolConfig;
import org.springframework.stereotype.Service;

/**
 * Description: uws扫码登录
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-12-09 14:26:00
 *
 * @author zjy
 */
@Service("uwsQrLoginTemplateService")
@SpiCustomThreadPoolConfig(threadPoolName = "custom_user_login_thread_pool")
public class UwsQrLoginBusinessService extends NormalLoginBusinessServiceImpl {

    @Override
    public String getKey() {
        return ShineAction.UWS_QR_START_LOGIN;
    }
}
