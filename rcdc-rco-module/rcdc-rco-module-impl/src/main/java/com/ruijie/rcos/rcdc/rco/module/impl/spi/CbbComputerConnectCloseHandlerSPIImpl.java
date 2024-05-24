package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rco.module.common.connect.DefaultConnectorListener;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/13 16:03
 *
 * @author zhangsiming
 */
@DispatcherImplemetion(DefaultConnectorListener.COMPUTER_CONNECT_CLOSE)
public class CbbComputerConnectCloseHandlerSPIImpl implements CbbDispatcherHandlerSPI {

    @Autowired
    private ComputerBusinessService computerBusinessService;

    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "cbbDispatcherRequest can not be null");
        String computerId = cbbDispatcherRequest.getTerminalId();
        computerBusinessService.offline(UUID.fromString(computerId));

    }
}
