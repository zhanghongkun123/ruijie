package com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums.TaskCycleEnum;
import com.ruijie.rcos.rcdc.rco.module.def.validation.ScheduleTaskValidator;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 定时任务校验处理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年08月26日
 *
 * @author xgx
 */
public class CycleScheduleTaskValidatorHandler {
    private static final ConcurrentHashMap<TaskCycleEnum, ScheduleTaskValidator> SCHEDULE_TASK_VALIDATOR_MAP = new ConcurrentHashMap<>(5);

    static {
        SCHEDULE_TASK_VALIDATOR_MAP.put(TaskCycleEnum.ONCE, new OnceScheduleTaskValidator());
        SCHEDULE_TASK_VALIDATOR_MAP.put(TaskCycleEnum.HOUR, new HourScheduleTaskValidator());
        SCHEDULE_TASK_VALIDATOR_MAP.put(TaskCycleEnum.DAY, new DayScheduleTaskValidator());
        SCHEDULE_TASK_VALIDATOR_MAP.put(TaskCycleEnum.WEEK, new WeekScheduleTaskValidator());
        SCHEDULE_TASK_VALIDATOR_MAP.put(TaskCycleEnum.MONTH, new MonthScheduleTaskValidator());
    }

    /**
     * 校验定时任务配置
     *
     * @param scheduleTaskRequest 定时任务请求对象
     * @throws BusinessException 业务异常
     * @throws AnnotationValidationException 参数校验异常
     */
    public static void validateScheduleTask(RcoScheduleTaskRequest scheduleTaskRequest) throws BusinessException, AnnotationValidationException {
        Assert.notNull(scheduleTaskRequest, "scheduleTaskRequest can not be null");
        Optional.ofNullable(SCHEDULE_TASK_VALIDATOR_MAP.get(scheduleTaskRequest.getTaskCycle())) //
                .orElseThrow(() -> new IllegalArgumentException("获取周期类型为[" + scheduleTaskRequest.getTaskCycle() + "]定时任务参数校验器失败"))//
                .validate(scheduleTaskRequest);
    }

    private CycleScheduleTaskValidatorHandler() {

    }
}
