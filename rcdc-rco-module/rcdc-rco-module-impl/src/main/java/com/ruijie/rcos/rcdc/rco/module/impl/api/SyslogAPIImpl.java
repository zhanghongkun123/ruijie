package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.task.module.def.util.LocalDateTimeUtil;
import com.ruijie.rcos.rcdc.rco.module.def.api.SyslogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SyslogScheduleConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SyslogScheduleDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


/**
 * Description: syslog API实现类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 10:27
 *
 * @author yxq
 */
public class SyslogAPIImpl implements SyslogAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyslogAPIImpl.class);

    /**
     * 时间格式
     */
    private static final String TIME_PATTERN = "HH:mm:ss";

    /**
     * cron表达式的分隔符
     */
    private static final String CRON_SEPARATOR = " ";


    @Autowired
    private GlobalParameterService globalParameterService;


    @Override
    public SyslogScheduleDTO findSyslogScheduleDetail() {
        SyslogScheduleDTO syslogScheduleDTO = new SyslogScheduleDTO();

        SyslogScheduleConfigDTO syslogScheduleConfigDTO = getSyslogScheduleConfig();
        BeanUtils.copyProperties(syslogScheduleConfigDTO, syslogScheduleDTO);

        if (StringUtils.isNotBlank(syslogScheduleConfigDTO.getScheduleCron())) {
            // 如果是按天发送 将数据库中的cron表达式转为 HH:mm:ss
            String scheduleTime = convertCronExpressionToTime(syslogScheduleConfigDTO.getScheduleCron());
            syslogScheduleDTO.setScheduleTime(scheduleTime);
        }

        return syslogScheduleDTO;

    }

    @Override
    public void editSyslogSchedule(SyslogScheduleDTO syslogScheduleDTO) throws BusinessException {
        Assert.notNull(syslogScheduleDTO, "syslogScheduleDTO must not null");
        Assert.notNull(syslogScheduleDTO.getSendCycleType(), "scheduleType must not null");

        SyslogScheduleConfigDTO syslogScheduleConfigDTO = new SyslogScheduleConfigDTO();

        BeanUtils.copyProperties(syslogScheduleDTO, syslogScheduleConfigDTO);

        // 校验日期格式是否正确
        if (StringUtils.isNotBlank(syslogScheduleDTO.getScheduleTime())) {
            LocalDateTimeUtil.validateScheduleTime(syslogScheduleDTO.getScheduleTime());
            String scheduleCron = generateCronExpression(syslogScheduleDTO.getScheduleTime());
            syslogScheduleConfigDTO.setScheduleCron(scheduleCron);
        }

        LOGGER.info("生成的syslog发送配置数据库存储对象：[{}]", JSON.toJSONString(syslogScheduleConfigDTO));
        globalParameterService.updateParameter(Constants.RCDC_RCO_SEND_SYSLOG_SCHEDULE_CONFIG, JSON.toJSONString(syslogScheduleConfigDTO));

    }


    private synchronized String convertCronExpressionToTime(String scheduleCron) {
        Assert.notNull(scheduleCron, "scheduleCron must not null");

        String[] cronTimeArr = scheduleCron.split(CRON_SEPARATOR);
        int hour = Integer.parseInt(cronTimeArr[2]);
        int minute = Integer.parseInt(cronTimeArr[1]);
        int second = Integer.parseInt(cronTimeArr[0]);
        LocalTime localTime = LocalTime.of(hour, minute, second);
        return localTime.format(DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    private synchronized String generateCronExpression(String scheduleTime) {
        Assert.notNull(scheduleTime, "scheduleTime must not null");

        LocalTime localTime = LocalTime.parse(scheduleTime, DateTimeFormatter.ofPattern(TIME_PATTERN));

        String second = String.valueOf(localTime.getSecond());
        String minute = String.valueOf(localTime.getMinute());
        String hour = String.valueOf(localTime.getHour());

        return second + CRON_SEPARATOR + minute + CRON_SEPARATOR + hour + CRON_SEPARATOR + "* * ?";
    }

    @Override
    public SyslogScheduleConfigDTO getSyslogScheduleConfig() {
        String paramValue = globalParameterService.findParameter(Constants.RCDC_RCO_SEND_SYSLOG_SCHEDULE_CONFIG);
        LOGGER.info("查询的syslog发送配置：[{}]", paramValue);
        return JSONObject.parseObject(paramValue, SyslogScheduleConfigDTO.class);
    }
}
