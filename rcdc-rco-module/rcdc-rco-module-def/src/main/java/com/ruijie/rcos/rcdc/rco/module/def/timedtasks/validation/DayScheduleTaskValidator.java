package com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation;

import com.ruijie.rcos.base.task.module.def.util.LocalDateTimeUtil;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.validation.ScheduleTaskValidator;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation.entity.DayScheduleTask;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.validation.BeanValidationUtil;

/**
 * Description: 天周期定时任务校验处理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年08月26日
 *
 * @author xgx
 */
class DayScheduleTaskValidator implements ScheduleTaskValidator<RcoScheduleTaskRequest> {

    @Override
    public void validate(RcoScheduleTaskRequest scheduleTaskRequest) throws AnnotationValidationException {
        Assert.notNull(scheduleTaskRequest, "scheduleTaskRequest can not be null");
        DayScheduleTask dayScheduleTask = new DayScheduleTask();
        BeanUtils.copyProperties(scheduleTaskRequest, dayScheduleTask);
        BeanValidationUtil.validateBean(DayScheduleTask.class, dayScheduleTask);

        LocalDateTimeUtil.validateScheduleTime(scheduleTaskRequest.getScheduleTime());
    }
}
