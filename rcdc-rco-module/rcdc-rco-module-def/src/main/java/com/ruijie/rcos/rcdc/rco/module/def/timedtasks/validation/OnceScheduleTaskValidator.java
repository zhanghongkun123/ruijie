package com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation;

import com.ruijie.rcos.base.task.module.def.util.LocalDateTimeUtil;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.TimedTaskBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation.entity.OnceScheduleTask;
import com.ruijie.rcos.rcdc.rco.module.def.validation.ScheduleTaskValidator;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskState;
import com.ruijie.rcos.sk.base.validation.BeanValidationUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Description: 一次性定时任务校验处理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年08月26日
 *
 * @author xgx
 */
class OnceScheduleTaskValidator implements ScheduleTaskValidator<RcoScheduleTaskRequest> {
    @Override
    public void validate(RcoScheduleTaskRequest scheduleTaskRequest) throws AnnotationValidationException, BusinessException {
        Assert.notNull(scheduleTaskRequest, "scheduleTaskRequest can not be null");

        OnceScheduleTask onceScheduleTask = new OnceScheduleTask();
        BeanUtils.copyProperties(scheduleTaskRequest, onceScheduleTask);
        BeanValidationUtil.validateBean(OnceScheduleTask.class, onceScheduleTask);

        LocalDateTimeUtil.validateScheduleDate(scheduleTaskRequest.getScheduleDate());
        LocalDateTimeUtil.validateScheduleTime(scheduleTaskRequest.getScheduleTime());

        LocalDate localDate = LocalDate.parse(scheduleTaskRequest.getScheduleDate(), DateTimeFormatter.ofPattern(LocalDateTimeUtil.YYYY_MM_DD));
        LocalTime localTime = LocalTime.parse(scheduleTaskRequest.getScheduleTime(), DateTimeFormatter.ofPattern(LocalDateTimeUtil.HH_MM_SS));
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        if (QuartzTaskState.START == scheduleTaskRequest.getQuartzTaskState() && LocalDateTime.now().isAfter(localDateTime)) {
            throw new BusinessException(TimedTaskBusinessKey.RCDC_RCO_TIMED_TASK_QUARTZ_ONCE_CYCLE_TIME_EXPIRE);
        }
    }
}
