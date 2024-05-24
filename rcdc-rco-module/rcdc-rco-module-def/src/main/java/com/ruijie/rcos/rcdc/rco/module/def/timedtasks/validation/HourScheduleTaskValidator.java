package com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation;

import com.ruijie.rcos.base.task.module.def.util.LocalDateTimeUtil;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation.entity.DayScheduleTask;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation.entity.HourScheduleTask;
import com.ruijie.rcos.rcdc.rco.module.def.validation.ScheduleTaskValidator;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.validation.BeanValidationUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

/**
 * Description: 小时周期定时任务校验处理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月25日
 *
 * @author zjy
 */
class HourScheduleTaskValidator implements ScheduleTaskValidator<RcoScheduleTaskRequest> {

    @Override
    public void validate(RcoScheduleTaskRequest scheduleTaskRequest) throws AnnotationValidationException {
        Assert.notNull(scheduleTaskRequest, "scheduleTaskRequest can not be null");

    }
}
