package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.NtpMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.NtpResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.rest.tang.RccpTangClient;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-04-26 17:51
 *
 * @author wanglianyun
 */


public class NtpMgmtAPIImpl implements NtpMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(NtpMgmtAPIImpl.class);

    @Autowired
    private RccpTangClient rccpTangClient;

    @Override
    public NtpResponse getNtpServer() throws BusinessException {
        return rccpTangClient.getNtpServer();
    }
}
