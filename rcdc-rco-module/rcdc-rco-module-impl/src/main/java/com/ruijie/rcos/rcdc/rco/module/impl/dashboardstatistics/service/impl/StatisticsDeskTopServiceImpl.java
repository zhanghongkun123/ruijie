package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.service.impl;

import com.alibaba.fastjson.util.TypeUtils;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopOnlineSituationStatisticsDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DesktopOnlineSituationStatisticsRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.DesktopOnlineSituationStatisticsResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.service.StatisticsDeskTopService;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao.DesktopOnlineSituationDayRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao.DesktopOnlineSituationHourRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/17
 *
 * @author xiao'yong'deng
 */
@Service
public class StatisticsDeskTopServiceImpl implements StatisticsDeskTopService {

    @Autowired
    private DesktopOnlineSituationDayRecordDAO desktopOnlineSituationDayRecordDAO;

    @Autowired
    private DesktopOnlineSituationHourRecordDAO desktopOnlineSituationHourRecordDAO;

    private static final int TWENTY_FOUR_HOURS_AGO = 24;

    private static final int ONE_MONTH_AGO = 30;

    private static final int ONE_YEAR_AGO = 365;

    private static final String DATE_FORMAT_DAY = "yyyy-MM-dd";

    @Override
    public DesktopOnlineSituationStatisticsResponse statisticsDesktopHistoryOnlineSituationByDay(DesktopOnlineSituationStatisticsRequest request) {
        Assert.notNull(request, "request is not null");

        List<Object[]> resultList = desktopOnlineSituationHourRecordDAO.countLastOneDayData();
        List<DesktopOnlineSituationStatisticsDTO> desktopOnlineSituationList = convertFor(resultList);
        desktopOnlineSituationList = desktopOnlineSituationList.stream().map(v -> {
            String date = DateUtil.getHourRangeByHourKey(v.getDateTime());
            return new DesktopOnlineSituationStatisticsDTO (date, v.getOnlineCount(), v.getSleepCount());
        }).collect(Collectors.toList());
        desktopOnlineSituationList = format(desktopOnlineSituationList, getLastOneDay());
        DesktopOnlineSituationStatisticsResponse response = new DesktopOnlineSituationStatisticsResponse();
        response.setDesktopOnlineSituationStatisticsList(desktopOnlineSituationList);
        return response;
    }

    @Override
    public DesktopOnlineSituationStatisticsResponse statisticsDesktopHistoryOnlineSituationByMonth(DesktopOnlineSituationStatisticsRequest request) {
        Assert.notNull(request, "request is not null");

        List<Object[]> resultList = desktopOnlineSituationDayRecordDAO.countLastOneMonthData();
        List<DesktopOnlineSituationStatisticsDTO> desktopOnlineSituationList = convertFor(resultList);
        desktopOnlineSituationList = format(desktopOnlineSituationList, getLastOneMonth(new Date()));
        DesktopOnlineSituationStatisticsResponse response = new DesktopOnlineSituationStatisticsResponse();
        response.setDesktopOnlineSituationStatisticsList(desktopOnlineSituationList);
        return response;
    }

    @Override
    public DesktopOnlineSituationStatisticsResponse statisticsDesktopHistoryOnlineSituationByYear(DesktopOnlineSituationStatisticsRequest request) {
        Assert.notNull(request, "request is not null");

        List<Object[]> resultList = desktopOnlineSituationDayRecordDAO.countLastOneYearData();
        List<DesktopOnlineSituationStatisticsDTO> desktopOnlineSituationList = convertFor(resultList);
        desktopOnlineSituationList = format(desktopOnlineSituationList, getLastOneYear(new Date()));
        DesktopOnlineSituationStatisticsResponse response = new DesktopOnlineSituationStatisticsResponse();
        response.setDesktopOnlineSituationStatisticsList(desktopOnlineSituationList);
        return response;
    }

    private List<DesktopOnlineSituationStatisticsDTO> convertFor(List<Object[]> countResult) {
        return countResult.stream().filter(this::verifyArray).map(args -> {
            String dateTime = TypeUtils.castToString(args[0]);
            String onlineCount = TypeUtils.castToString(args[1]);
            String sleepCount = TypeUtils.castToString(args[2]);
            return new DesktopOnlineSituationStatisticsDTO(dateTime, onlineCount, sleepCount);
        }).collect(Collectors.toList());
    }

    private Boolean verifyArray(Object[] args) {
        return ArrayUtils.isNotEmpty(args) && args.length == 3 && Objects.nonNull(args[0]) && Objects.nonNull(args[1]) && Objects.nonNull(args[2]);
    }

    private List<String> getLastOneDay() {
        List<String> resultList = Lists.newArrayList();
        Date currentDate = new Date();
        for (int i = 0; i < TWENTY_FOUR_HOURS_AGO; i++) {
            Date date = DateUtils.addHours(currentDate, -i);
            String showDate = DateUtil.getHourRangeByHourKey(DateUtil.getStatisticHourKey(date));
            resultList.add(showDate);
        }
        return resultList;
    }

    private List<String> getLastOneMonth(Date currentDate) {
        List<String> resultList = Lists.newArrayList();
        for (int i = 0; i < ONE_MONTH_AGO; i++) {
            Date date = DateUtils.addDays(currentDate, -i);
            resultList.add(DateFormatUtils.format(date, DATE_FORMAT_DAY));
        }
        return resultList;
    }

    private List<String> getLastOneYear(Date currentDate) {
        List<String> resultList = Lists.newArrayList();
        for (int i = 0; i < ONE_YEAR_AGO; i++) {
            Date date = DateUtils.addDays(currentDate, -i);
            resultList.add(DateFormatUtils.format(date, DATE_FORMAT_DAY));
        }
        return resultList;
    }

    private List<DesktopOnlineSituationStatisticsDTO> format(List<DesktopOnlineSituationStatisticsDTO> desktopOnlineSituationStatisticsDTOList,
            List<String> dateList) {
        dateList.forEach(date -> {
            if (desktopOnlineSituationStatisticsDTOList.stream().noneMatch(dto -> date.contains(dto.getDateTime()))) {
                desktopOnlineSituationStatisticsDTOList.add(new DesktopOnlineSituationStatisticsDTO(date, StringUtils.EMPTY, StringUtils.EMPTY));
            }
        });
        return desktopOnlineSituationStatisticsDTOList.stream().sorted(Comparator.comparing(DesktopOnlineSituationStatisticsDTO::getDateTime))
                .collect(Collectors.toList());
    }
}
