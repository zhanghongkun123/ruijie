package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.EstClientMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EstClientService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description: EST Client 管理
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.12.29
 *
 * @author linhj
 */
public class EstClientMgmtAPIImpl implements EstClientMgmtAPI {

    @Autowired
    private EstClientService estClientService;

    @Override
    public int estClientLimit() throws BusinessException {
        return estClientService.estClientLimit();
    }
}
