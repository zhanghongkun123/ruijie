package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread;

import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread.AbstractAsyncTaskThread;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: 异步线程管理用户抽象类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author lyb
 */
public abstract class AbstractAsyncUserMgmtThread extends AbstractAsyncTaskThread {


    public AbstractAsyncUserMgmtThread(UUID businessId, AsyncTaskEnum action,
                                       OpenApiTaskInfoAPI openApiTaskInfoAPI) throws BusinessException {
        super(businessId, action, openApiTaskInfoAPI);
    }

}
