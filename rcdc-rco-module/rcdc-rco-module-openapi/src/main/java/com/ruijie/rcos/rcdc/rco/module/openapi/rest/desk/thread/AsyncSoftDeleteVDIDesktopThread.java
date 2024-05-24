package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread;

import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MoveDesktopToRecycleBinRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.util.UUID;

/**
 * Description: 异步线程软删除云桌面
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/13 11:21
 *
 * @author lyb
 */
public class AsyncSoftDeleteVDIDesktopThread extends AbstractAsyncDesktopMgmtThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSoftDeleteVDIDesktopThread.class);

    private MoveDesktopToRecycleBinRequest request;

    public AsyncSoftDeleteVDIDesktopThread(UUID deskId, AsyncTaskEnum action, OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                           UserDesktopMgmtAPI userDesktopMgmtAPI) throws BusinessException {
        super(deskId, action, openApiTaskInfoAPI, userDesktopMgmtAPI);
        MoveDesktopToRecycleBinRequest request = new MoveDesktopToRecycleBinRequest();
        request.setDesktopId(deskId);
        request.setCustomTaskId(customTaskId);
        setRequest(request);
    }

    @Override
    public void run() {
        try {
            userDesktopMgmtAPI.moveDesktopToRecycleBin(request);
            saveTaskSuccess();
            LOGGER.info("OpenAPI软删除云桌面成功，云桌面id={}", request.getDesktopId());
        } catch (BusinessException e) {
            LOGGER.error("AsyncSoftDeleteVDIDesktopThread error!", e);
            saveTaskException(e);
        }
    }

    public void setRequest(MoveDesktopToRecycleBinRequest request) {
        this.request = request;
    }
}
