package com.ruijie.rcos.rcdc.rco.module.impl.timedtasks.handler;

import com.google.common.base.Splitter;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.TimedTaskBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.dto.CronConvertDTO;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums.TaskCycleEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: Cron表达式转换 处理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年11月01日
 *
 * @author luojianmo
 */
public class CronExpressionConvertHandler {

    private CronExpressionConvertHandler() {
        throw new IllegalStateException("CronExpressionConvertHandler Utility class");
    }

    private static final ConcurrentHashMap<TaskCycleEnum, CronExpressionConvert> CRON_EXPRESSION_CONVERT_MAP = new ConcurrentHashMap<>(5);

    static {
        CRON_EXPRESSION_CONVERT_MAP.put(TaskCycleEnum.ONCE, new OnceCronExpressionConvert());
        CRON_EXPRESSION_CONVERT_MAP.put(TaskCycleEnum.HOUR, new HourCronExpressionConvert());
        CRON_EXPRESSION_CONVERT_MAP.put(TaskCycleEnum.DAY, new DayCronExpressionConvert());
        CRON_EXPRESSION_CONVERT_MAP.put(TaskCycleEnum.WEEK, new WeekCronExpressionConvert());
        CRON_EXPRESSION_CONVERT_MAP.put(TaskCycleEnum.MONTH, new MonthCronExpressionConvert());
    }


    /**
     * 生成Cron表达式
     *
     * @param scheduleTaskRequest 入参
     * @return 响应：CRON表达式
     * @throws BusinessException 业务异常
     */
    public static String generateExpression(RcoScheduleTaskRequest scheduleTaskRequest) throws BusinessException {
        Assert.notNull(scheduleTaskRequest, "scheduleTaskRequest can not be null");
        return Optional.ofNullable(CRON_EXPRESSION_CONVERT_MAP.get(scheduleTaskRequest.getTaskCycle()))
                .orElseThrow(() -> new BusinessException(TimedTaskBusinessKey.RCDC_RCO_TIMED_TASK_CRON_EXPRESSION_GENERATE_FAIL,
                        scheduleTaskRequest.getTaskCycle().toString()))
                .generateExpression(scheduleTaskRequest);
    }

    /**
     * 解析Cron表达式
     *
     * @param cronExpression Cron表达式
     * @return 响应
     * @throws BusinessException 业务异常
     */
    public static CronConvertDTO parseCronExpression(String cronExpression) throws BusinessException {
        Assert.hasText(cronExpression, "cronExpression can not be empty");
        TaskCycleEnum taskCycleEnum = TaskCycleEnum.getCycle(cronExpression);
        List<String> cronList = Splitter.on(" ").splitToList(cronExpression);
        CronConvertDTO cronConvertDTO = Optional.ofNullable(CRON_EXPRESSION_CONVERT_MAP.get(taskCycleEnum))
                .orElseThrow(() -> new BusinessException(TimedTaskBusinessKey.RCDC_RCO_TIMED_TASK_CRON_EXPRESSION_PARES_FAIL_BY_CYCLE,
                        taskCycleEnum.toString(), cronExpression))
                .parseCronExpression(cronList);
        cronConvertDTO.setTaskCycle(taskCycleEnum);
        return cronConvertDTO;
    }
}
