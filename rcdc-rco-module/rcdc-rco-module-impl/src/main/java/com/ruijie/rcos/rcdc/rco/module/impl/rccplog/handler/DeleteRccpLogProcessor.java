package com.ruijie.rcos.rcdc.rco.module.impl.rccplog.handler;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.RccpLogMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.log.DeleteLogRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/15 10:32
 *
 * @author ketb
 */
@Service
public class DeleteRccpLogProcessor implements StateTaskHandle.StateProcessor {

    @Autowired
    private RccpLogMgmtAPI logMgmtAPI;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Override
    public StateTaskHandle.ProcessResult doProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context is not null");
        final UUID deleteLogId = context.get(DeleteRccpLogHandler.DELETE_ID_CONTEXT, UUID.class);
        CloudPlatformDTO cloudPlatform = cloudPlatformManageAPI.getDefaultCloudPlatform();
        DeleteLogRequest deleteLogRequest = new DeleteLogRequest();
        deleteLogRequest.setPlatformId(cloudPlatform.getId());
        deleteLogRequest.setPlatformType(cloudPlatform.getType());
        deleteLogRequest.setId(deleteLogId);
        deleteLogRequest.setTaskId(UUID.randomUUID());
        logMgmtAPI.deleteLog(deleteLogRequest);
        return StateTaskHandle.ProcessResult.next();
    }
}
