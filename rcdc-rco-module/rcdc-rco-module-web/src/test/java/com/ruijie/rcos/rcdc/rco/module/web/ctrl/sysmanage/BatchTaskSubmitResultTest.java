package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage;

import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年03月12日
 *
 * @author xgx
 */
public class BatchTaskSubmitResultTest implements BatchTaskSubmitResult {

    @Override
    public UUID getTaskId() {
        return UUID.randomUUID();
    }

    @Override
    public String getTaskName() {
        return "getTaskName";
    }

    @Override
    public String getTaskDesc() {
        return "getTaskDesc";
    }

    @Override
    public BatchTaskStatus getTaskStatus() {
        return BatchTaskStatus.SUCCESS;
    }
}
