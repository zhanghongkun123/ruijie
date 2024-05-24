package com.ruijie.rcos.rcdc.rco.module.web.ctrl.task.batchtask;

import com.ruijie.rcos.base.task.module.def.api.ScheduleTaskAPI;
import com.ruijie.rcos.base.task.module.def.dto.ScheduleTaskDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.SysmanagerBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年03月22日
 *
 * @author xgx
 */
public class BatchDeleteScheduleTaskHandle extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchDeleteScheduleTaskHandle.class);

    private BaseAuditLogAPI auditLogAPI;

    private final ScheduleTaskAPI scheduleTaskAPI;


    public BatchDeleteScheduleTaskHandle(Iterator<BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI,
            ScheduleTaskAPI scheduleTaskAPI) {
        super(iterator);
        Assert.notNull(auditLogAPI, "auditLogAPI can not be null");
        Assert.notNull(scheduleTaskAPI, "scheduleTaskAPI can not be null");
        this.auditLogAPI = auditLogAPI;
        this.scheduleTaskAPI = scheduleTaskAPI;


    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "item can not be null");
        UUID id = item.getItemID();
        ScheduleTaskDTO scheduleTaskDTO = null;
        try {

            scheduleTaskDTO = scheduleTaskAPI.queryById(id);

            scheduleTaskAPI.delete(id);

            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_SCHEDULE_TASK_DO_SUCCESS, scheduleTaskDTO.getTaskName());
            return DefaultBatchTaskItemResult.success(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_SCHEDULE_TASK_ITEM_SUCCESS,
                    scheduleTaskDTO.getTaskName());
        } catch (BusinessException e) {
            LOGGER.error("删除日志配置失败", e);
            String name = scheduleTaskDTO == null ? id.toString() : scheduleTaskDTO.getTaskName();
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_SCHEDULE_TASK_DO_FAIL, e, name, e.getI18nMessage());
            return DefaultBatchTaskItemResult.fail(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_SCHEDULE_TASK_ITEM_FAIL, name, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_SCHEDULE_TASK_RESULT);
    }
}
