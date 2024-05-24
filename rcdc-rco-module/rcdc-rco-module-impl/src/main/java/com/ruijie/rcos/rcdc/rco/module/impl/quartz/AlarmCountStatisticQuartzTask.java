package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.ListAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.dto.AlarmDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.PlatformAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AlarmTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.AlarmCountDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AlarmCountDayEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月26日
 * 
 * //@Service
 * //@Quartz(scheduleTypeCode = "rco_alarm_cont_statistic", scheduleName =
 * BusinessKey.RCDC_RCO_BIGSCREEN_QUARTZ_ALARM_COUNT_STATISTIC,
 * // cron = "0 10 0 * * ? *")
 *
 * @author wanmulin
 */
public class AlarmCountStatisticQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmCountStatisticQuartzTask.class);

    private static final Integer PAGE_INIT_VAL = 0;

    @Autowired
    private BaseAlarmAPI baseAlarmAPI;

    @Autowired
    private AlarmCountDayDAO alarmCountDayDAO;

    @Autowired
    private PlatformAPI platformAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws BusinessException {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        LOGGER.info("获取服务器/桌面告警数定时任务开始===");
        List<LocalDate> desktopRetryList = check(AlarmTypeEnum.DESKTOP);
        if (!desktopRetryList.isEmpty()) {
            // 若一周内数据存在缺失，则重新获取
            LOGGER.info("需要获取的云桌面告警数日期，desktopRetryList={}", JSON.toJSONString(desktopRetryList));
            for (LocalDate date : desktopRetryList) {
                save(date, AlarmTypeEnum.DESKTOP);
            }
        }

        List<LocalDate> serverRetryList = check(AlarmTypeEnum.SERVER);
        if (!serverRetryList.isEmpty()) {
            // 若一周内数据存在缺失，则重新获取
            LOGGER.info("需要获取的服务器告警数日期，serverRetryList={}", JSON.toJSONString(serverRetryList));
            for (LocalDate date : serverRetryList) {
                save(date, AlarmTypeEnum.SERVER);
            }
        }
        LOGGER.info("获取服务器/桌面告警数定时任务结束===");
    }

    private List<LocalDate> check(AlarmTypeEnum alarmType) throws BusinessException {
        // 获取最大补偿天数
        int retryDays = getRetryDays();
        if (retryDays == 0) {
            return Lists.newArrayList();
        }
        LocalDate now = LocalDate.now();
        LocalDate startTime = now.minusDays(retryDays);
        LocalDate endTime = now.minusDays(1);

        List<AlarmCountDayEntity> deskTopAlarmCountList = alarmCountDayDAO.findByAlarmTypeAndStatisticTimeBetween(alarmType,
                DateUtil.localDateToDate(startTime), DateUtil.localDateToDate(endTime));

        List<LocalDate> deskTopTimeList =
                deskTopAlarmCountList.stream().map(o -> DateUtil.dateToLocalDate(o.getStatisticTime())).collect(Collectors.toList());

        List<LocalDate> retryDateList = Lists.newArrayList();
        for (int i = 1; i <= retryDays; i++) {
            retryDateList.add(now.minusDays(i));
        }
        return retryDateList.stream().filter(o -> !deskTopTimeList.contains(o)).collect(Collectors.toList());
    }

    private int getRetryDays() throws BusinessException {
        long systemWorkTime = platformAPI.getSystemTime().getSystemWorkTime();
        return Math.min(Constants.DEFAULT_RETRY_DAYS, DateUtil.computeDayInterval(systemWorkTime));
    }

    private void save(LocalDate dateTime, AlarmTypeEnum type) {
        ListAlarmRequest apiRequest = getListAlarmRequest(dateTime, type);
        int countSum = 0;

        apiRequest.setEnableQueryHistory(false);
        DefaultPageResponse<AlarmDTO> apiResponse = baseAlarmAPI.listAlarmList(apiRequest);
        if (null != apiResponse) {
            countSum += apiResponse.getTotal();
        } else {
            LOGGER.error("未获取到[{}]当前告警信息", type.name());
        }

        apiRequest.setEnableQueryHistory(true);
        apiResponse = baseAlarmAPI.listAlarmList(apiRequest);
        if (null != apiResponse) {
            countSum += apiResponse.getTotal();
        } else {
            LOGGER.error("未获取到[{}]历史告警信息", type.name());
        }

        AlarmCountDayEntity entity = new AlarmCountDayEntity();
        entity.setCreateTime(new Date());
        entity.setStatisticTime(DateUtil.localDateToDate(dateTime));
        entity.setAlarmType(type);
        entity.setAlarmCount(countSum);

        alarmCountDayDAO.save(entity);
    }

    private ListAlarmRequest getListAlarmRequest(LocalDate dateTime, AlarmTypeEnum type) {
        ListAlarmRequest apiRequest = new ListAlarmRequest();
        apiRequest.setStartTime(DateUtil.localDateToDate(dateTime));
        apiRequest.setEndTime(DateUtil.localDateToDate(dateTime.plusDays(1L)));
        apiRequest.setTimeField("firstAlarmTime");
        apiRequest.setPage(PAGE_INIT_VAL);
        apiRequest.setAlarmTypeArr(new String[] {type.getName()});
        return apiRequest;
    }
}
