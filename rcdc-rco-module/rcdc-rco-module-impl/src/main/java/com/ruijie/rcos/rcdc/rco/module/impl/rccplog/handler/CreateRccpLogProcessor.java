package com.ruijie.rcos.rcdc.rco.module.impl.rccplog.handler;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.RccpLogMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.log.CreateLogRequest;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/15 10:32
 *
 * @author ketb
 */
@Service
public class CreateRccpLogProcessor implements StateTaskHandle.StateProcessor {

    @Autowired
    private RccpLogMgmtAPI logMgmtAPI;

    @Override
    public StateTaskHandle.ProcessResult doProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context is not null");
        final CreateLogRequest createLogRequest = context.get(CreateRccpLogHandler.CREATE_ID_CONTEXT, CreateLogRequest.class);
        logMgmtAPI.collectLog(createLogRequest);
        return StateTaskHandle.ProcessResult.next();
    }
}
