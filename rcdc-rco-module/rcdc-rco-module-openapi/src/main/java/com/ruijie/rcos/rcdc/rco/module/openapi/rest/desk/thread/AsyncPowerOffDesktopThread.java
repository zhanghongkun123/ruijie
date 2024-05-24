package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread;

import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.util.UUID;

/**
 * Description: 异步线程关闭云桌面
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/13 11:03
 *
 * @author lyb
 */
public class AsyncPowerOffDesktopThread extends AbstractAsyncOperateDesktopThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncPowerOffDesktopThread.class);

    public AsyncPowerOffDesktopThread(UUID deskId,
                                      AsyncTaskEnum action,
                                      OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                      UserDesktopOperateAPI cloudDesktopOperateAPI) throws BusinessException {
        super(deskId, action, openApiTaskInfoAPI, cloudDesktopOperateAPI);
    }

    @Override
    public void run() {
        try {
            cloudDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(deskId, Boolean.TRUE, customTaskId));
            saveTaskSuccess();
        } catch (BusinessException e) {
            LOGGER.error("AsyncShutdownDesktopThread error!", e);
            saveTaskException(e);
        }
    }
}
