package com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums;


import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.TimedTaskBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.util.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: 周期类型枚举
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年11月01日
 *
 * @author luojianmo
 */
public enum TaskCycleEnum {
    /**
     * 一次
     */
    ONCE(Pattern.compile("^(((\\d|([1-5]\\d)) ){2}(\\d|1\\d|2[0-3]) ([1-9]|[1-2]\\d|3[0-1]) ([1-9]|1[0-2]) \\? [1-9]\\d{3})$")),
    /**
     * 每小时
     */
    HOUR(Pattern.compile("^(\\d|[1-5]\\d) (\\d|[1-5]\\d) 0\\/(1?[0-9]|2[0-3]) \\* \\* \\? \\*$")),
    /**
     * 每天
     */
    DAY(Pattern.compile("^((\\d|([1-5]\\d)) ){2}(\\d|1\\d|2[0-3]) \\* \\* \\? \\*$")),
    /**
     * 每周
     */
    WEEK(Pattern.compile("^((\\d|([1-5]\\d)) ){2}(\\d|1\\d|2[0-3]) \\? \\* (([1-7],){0,6}[1-7]) \\*$")),
    /**
     * 每月
     */
    MONTH(Pattern.compile("^((\\d|([1-5]\\d)) ){2}(\\d|1\\d|2[0-3]) ([1-9]|[1-2]\\d|3[0-1]) \\* \\? \\*$"));

    private final Pattern pattern;

    TaskCycleEnum(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * cron表达式转换为 TaskCycleEnum
     * 
     * @param cronExpression cron表达式
     * @return TaskCycleEnum
     * @throws BusinessException 业务异常
     */
    public static TaskCycleEnum getCycle(String cronExpression) throws BusinessException {
        Assert.notNull(cronExpression, "cronExpression can not be null");
        TaskCycleEnum[] taskCycleEnumArr = values();

        for (int i = 0; i < taskCycleEnumArr.length; ++i) {
            TaskCycleEnum cycle = taskCycleEnumArr[i];
            Matcher matcher = cycle.pattern.matcher(cronExpression);
            if (matcher.find()) {
                return cycle;
            }
        }

        throw new BusinessException(TimedTaskBusinessKey.RCDC_RCO_TIMED_TASK_CRON_EXPRESSION_PARES_FAIL, cronExpression);
    }
}

