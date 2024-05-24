package com.ruijie.rcos.rcdc.rco.module.impl.timedtasks.handler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.ruijie.rcos.rcdc.rco.module.impl.timedtasks.util.CronConvertUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.util.Assert;

import com.ruijie.rcos.base.task.module.def.util.LocalDateTimeUtil;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.dto.CronConvertDTO;
import com.ruijie.rcos.sk.base.quartz.CronExpressionBuilder;

/**
 * Description: Cron表达式转换 每次处理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年11月01日
 *
 * @author luojianmo
 */
public class OnceCronExpressionConvert implements CronExpressionConvert<RcoScheduleTaskRequest> {

    @Override
    public String generateExpression(RcoScheduleTaskRequest rcoScheduleTaskRequest) {
        Assert.notNull(rcoScheduleTaskRequest, "rcoScheduleTaskRequest can not be null");
        String scheduleTime = rcoScheduleTaskRequest.getScheduleTime();
        String scheduleDate = rcoScheduleTaskRequest.getScheduleDate();
        Assert.notNull(scheduleDate, "scheduleDate can not be null");
        LocalDateTimeUtil.validateScheduleTime(scheduleTime);
        LocalDateTimeUtil.validateScheduleDate(scheduleDate);
        LocalTime localTime = LocalTime.parse(scheduleTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalDate localDate = LocalDate.parse(scheduleDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        CronExpressionBuilder builder = CronExpressionBuilder.newBuilder();
        builder.setHour(String.valueOf(localTime.getHour()));
        builder.setMinute(String.valueOf(localTime.getMinute()));
        builder.setSecond(String.valueOf(localTime.getSecond()));
        builder.setYear(String.valueOf(localDate.getYear()));
        builder.setMonth(String.valueOf(localDate.getMonthValue()));
        builder.setDayOfMonth(String.valueOf(localDate.getDayOfMonth()));
        builder.setDayOfWeek("?");
        return builder.build();
    }

    @Override
    public CronConvertDTO parseCronExpression(List<String> cronList) throws BusinessException {
        Assert.notNull(cronList, "cronList can not be null");
        CronConvertDTO cronConvertDTO = new CronConvertDTO();
        cronConvertDTO.setScheduleTime(CronConvertUtils.getScheduleTime(cronList));
        cronConvertDTO.setScheduleDate(CronConvertUtils.getScheduleDate(cronList));
        return cronConvertDTO;
    }
}
