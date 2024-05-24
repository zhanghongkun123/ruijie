package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.impl;

import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.modulekit.api.comm.SpiCustomThreadPoolConfig;
import org.springframework.stereotype.Service;

/**
 * Description: 锐捷客户端扫码
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-20 15:59
 *
 * @author wanglianyun
 */
@Service("mobileClientQrLoginTemplateService")
@SpiCustomThreadPoolConfig(threadPoolName = "custom_user_login_thread_pool")
public class MoblieClientQrLoginBusinessService extends NormalLoginBusinessServiceImpl {

    @Override
    public String getKey() {
        return ShineAction.MOBILE_CLIENT_QR_START_LOGIN;
    }
}
