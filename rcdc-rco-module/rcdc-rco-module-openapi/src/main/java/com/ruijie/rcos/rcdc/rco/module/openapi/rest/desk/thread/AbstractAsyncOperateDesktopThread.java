package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread;

import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: 异步线程操作云桌面抽象类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/13 11:07
 *
 * @author lyb
 */
public abstract class AbstractAsyncOperateDesktopThread extends AbstractAsyncTaskThread {

    protected UUID deskId;

    protected UserDesktopOperateAPI cloudDesktopOperateAPI;

    public AbstractAsyncOperateDesktopThread(UUID deskId,
                                             AsyncTaskEnum action,
                                             OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                             UserDesktopOperateAPI cloudDesktopOperateAPI) throws BusinessException {
        super(deskId, action, openApiTaskInfoAPI);
        setDeskId(deskId);
        setCloudDesktopOperateAPI(cloudDesktopOperateAPI);
    }

    public void setCloudDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        this.cloudDesktopOperateAPI = cloudDesktopOperateAPI;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
}
