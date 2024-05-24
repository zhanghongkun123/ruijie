package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask.item;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskDTO;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;

import java.util.UUID;

/**
 * Description: 子任务操作批处理对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/17 14:22
 *
 * @author zhangyichi
 */
public class DistributeSubTaskOperateBatchItem extends DefaultBatchTaskItem {

    private DistributeTaskDTO parentTask;

    private DistributeSubTaskDTO subTask;

    private String targetName;

    public DistributeSubTaskOperateBatchItem(UUID itemId, String itemName) {
        super(itemId, itemName);
    }

    public DistributeTaskDTO getParentTask() {
        return parentTask;
    }

    public void setParentTask(DistributeTaskDTO parentTask) {
        this.parentTask = parentTask;
    }

    public DistributeSubTaskDTO getSubTask() {
        return subTask;
    }

    public void setSubTask(DistributeSubTaskDTO subTask) {
        this.subTask = subTask;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
}
