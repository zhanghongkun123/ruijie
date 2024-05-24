package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.deskoperate.CbbRestoreDeskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/30
 *
 * @author xiao'yong'deng
 */
public class AsyncRestoreDesktopThread extends AbstractAsyncOperateDesktopThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncRestoreDesktopThread.class);

    public AsyncRestoreDesktopThread(UUID deskId,
                                     AsyncTaskEnum action,
                                     OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                     UserDesktopOperateAPI cloudDesktopOperateAPI) throws BusinessException {
        super(deskId, action, openApiTaskInfoAPI, cloudDesktopOperateAPI);

    }

    @Override
    public void run() {
        try {
            cloudDesktopOperateAPI.restore(new CbbRestoreDeskRequest(deskId));
            saveTaskSuccess();
        } catch (BusinessException e) {
            LOGGER.error("AsyncRestoreDesktopThread error! deskId:{0}", e, deskId);
            saveTaskException(e);
        }
    }
}
