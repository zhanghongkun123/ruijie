package com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation;

import java.util.Arrays;

import com.ruijie.rcos.base.task.module.def.util.LocalDateTimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.validation.ScheduleTaskValidator;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation.entity.WeekScheduleTask;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.validation.BeanValidationUtil;

/**
 * Description: 周周期定时任务校验处理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年08月26日
 *
 * @author xgx
 */
class WeekScheduleTaskValidator implements ScheduleTaskValidator<RcoScheduleTaskRequest> {
    @Override
    public void validate(RcoScheduleTaskRequest scheduleTaskRequest) throws AnnotationValidationException {
        Assert.notNull(scheduleTaskRequest, "scheduleTaskRequest can not be null");

        WeekScheduleTask weekScheduleTask = new WeekScheduleTask();
        BeanUtils.copyProperties(scheduleTaskRequest, weekScheduleTask);
        BeanValidationUtil.validateBean(WeekScheduleTask.class, weekScheduleTask);

        Arrays.stream(scheduleTaskRequest.getDayOfWeekArr()) //
                .forEach(weekDay -> Assert.state(weekDay > 0 && weekDay <= 7, "星期几参数格式错误"));
        LocalDateTimeUtil.validateScheduleTime(scheduleTaskRequest.getScheduleTime());
    }
}
