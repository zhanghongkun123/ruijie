package com.ruijie.rcos.rcdc.rco.module.impl.timedtasks.handler;

import com.ruijie.rcos.base.task.module.def.util.LocalDateTimeUtil;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.dto.CronConvertDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.timedtasks.util.CronConvertUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.quartz.CronExpressionBuilder;
import org.springframework.util.Assert;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Description: Cron表达式转换 每小时处理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年9月26日
 *
 * @author zjy
 */
public class HourCronExpressionConvert implements CronExpressionConvert<RcoScheduleTaskRequest> {

    @Override
    public String generateExpression(RcoScheduleTaskRequest rcoScheduleTaskRequest) {
        Assert.notNull(rcoScheduleTaskRequest, "rcoScheduleTaskRequest can not be null");
        String scheduleTime = rcoScheduleTaskRequest.getScheduleTime();
        LocalDateTimeUtil.validateScheduleTime(scheduleTime);
        LocalTime localTime = LocalTime.parse(scheduleTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
        CronExpressionBuilder builder = CronExpressionBuilder.newBuilder();
        builder.setHour("/" + String.valueOf(localTime.getHour()));
        builder.setMinute(String.valueOf(localTime.getMinute()));
        builder.setSecond(String.valueOf(localTime.getSecond()));
        builder.setYear("*");
        builder.setMonth("*");
        builder.setDayOfMonth("*");
        builder.setDayOfWeek("?");
        return builder.build();
    }

    @Override
    public CronConvertDTO parseCronExpression(List<String> cronList) throws BusinessException {
        Assert.notEmpty(cronList, "cronList can not be null");
        CronConvertDTO cronConvertDTO = new CronConvertDTO();
        cronConvertDTO.setScheduleTime(CronConvertUtils.getSchedulePeriod(cronList));
        return cronConvertDTO;
    }
}