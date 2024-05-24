package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rca.module.def.request.RcacOperateAppVmRestServerRequest;
import com.ruijie.rcos.rcdc.rca.module.def.spi.RcacClientUserSetVmSPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/7 16:17
 *
 * @author zhangyichi
 */
public class RcacClientUserSetVmSPIImpl implements RcacClientUserSetVmSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcacClientUserSetVmSPIImpl.class);


    @Override
    public UUID operateAppVm(RcacOperateAppVmRestServerRequest restRequest) throws BusinessException {

        return UUID.randomUUID();
    }

}
