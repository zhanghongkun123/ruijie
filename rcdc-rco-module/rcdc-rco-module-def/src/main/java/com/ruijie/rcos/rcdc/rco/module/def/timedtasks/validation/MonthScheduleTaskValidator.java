package com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.base.task.module.def.util.LocalDateTimeUtil;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation.entity.MonthScheduleTask;
import com.ruijie.rcos.rcdc.rco.module.def.validation.ScheduleTaskValidator;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.validation.BeanValidationUtil;

/**
 * Description: 周周期定时任务校验处理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年11月1日
 *
 * @author luojianmo
 */
public class MonthScheduleTaskValidator implements ScheduleTaskValidator<RcoScheduleTaskRequest> {

    @Override
    public void validate(RcoScheduleTaskRequest scheduleTaskRequest) throws AnnotationValidationException {
        Assert.notNull(scheduleTaskRequest, "scheduleTaskRequest can not be null");

        MonthScheduleTask monthScheduleTask = new MonthScheduleTask();
        BeanUtils.copyProperties(scheduleTaskRequest, monthScheduleTask);
        BeanValidationUtil.validateBean(MonthScheduleTask.class, monthScheduleTask);

        LocalDateTimeUtil.validateScheduleTime(scheduleTaskRequest.getScheduleTime());

    }
}
