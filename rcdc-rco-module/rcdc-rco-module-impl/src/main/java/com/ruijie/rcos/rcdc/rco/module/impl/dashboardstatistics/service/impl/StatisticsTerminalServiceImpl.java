package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.service.impl;

import com.alibaba.fastjson.util.TypeUtils;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.dto.TerminalOnlineSituationStatisticsDTO;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.TerminalOnlineSituationStatisticsRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.TerminalOnlineSituationStatisticsResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewTerminalSearchDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao.TerminalOnlineSituationDayRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao.TerminalOnlineSituationHourRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.service.StatisticsTerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalSearchEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.HibernateUtil;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: StatisticsTerminalServiceImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
@Service
public class StatisticsTerminalServiceImpl implements StatisticsTerminalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsTerminalServiceImpl.class);

    @Autowired
    private ComputerBusinessDAO computerBusinessDAO;

    @Autowired
    private ViewTerminalSearchDAO viewTerminalSearchDAO;

    @Autowired
    private TerminalOnlineSituationDayRecordDAO terminalOnlineSituationDayRecordDAO;

    @Autowired
    private TerminalOnlineSituationHourRecordDAO terminalOnlineSituationHourRecordDAO;

    private static final int TWENTY_FOUR_HOURS_AGO = 24;

    private static final int ONE_MONTH_AGO = 30;

    private static final int ONE_YEAR_AGO = 365;

    private static final String DATE_FORMAT_DAY = "yyyy-MM-dd";

    private static final ExecutorService STATISTICS_TERMINAL_THREAD_POOL =
            ThreadExecutors.newBuilder(StatisticsTerminalServiceImpl.class.getName()).maxThreadNum(10).queueSize(10).build();

    private static final int TERMINAL_SELECT_NUMS = 5000;

    @Override
    public TerminalOnlineSituationStatisticsResponse statisticsTerminalHistoryOnlineSituationByDay(TerminalOnlineSituationStatisticsRequest request) {
        Assert.notNull(request, "request is not null");

        List<String> terminalIdList = buildTerminalIdList(request);
        String platform = request.getPlatform().toString();
        List<TerminalOnlineSituationStatisticsDTO> terminalOnlineSituationStatisticsDTOList = getLastOneDayData(terminalIdList, platform);
        terminalOnlineSituationStatisticsDTOList = terminalOnlineSituationStatisticsDTOList.stream().map(v -> {
            String date = DateUtil.getHourRangeByHourKey(v.getDateTime());
            return new TerminalOnlineSituationStatisticsDTO(date, v.getCount());
        }).collect(Collectors.toList());
        terminalOnlineSituationStatisticsDTOList = format(terminalOnlineSituationStatisticsDTOList, getLastOneDay());
        TerminalOnlineSituationStatisticsResponse terminalOnlineSituationStatisticsResponse = new TerminalOnlineSituationStatisticsResponse();
        terminalOnlineSituationStatisticsResponse.setTerminalOnlineSituationStatisticsList(terminalOnlineSituationStatisticsDTOList);
        return terminalOnlineSituationStatisticsResponse;
    }

    @Override
    public TerminalOnlineSituationStatisticsResponse statisticsTerminalHistoryOnlineSituationByMonth(
            TerminalOnlineSituationStatisticsRequest request) {
        Assert.notNull(request, "request is not null");

        List<String> terminalIdList = buildTerminalIdList(request);
        String platform = request.getPlatform().toString();
        List<TerminalOnlineSituationStatisticsDTO> terminalOnlineSituationStatisticsDTOList = getLastOneMonthData(terminalIdList, platform);
        Date currentDate = new Date();
        terminalOnlineSituationStatisticsDTOList.add(new TerminalOnlineSituationStatisticsDTO(DateFormatUtils.format(currentDate, DATE_FORMAT_DAY),
                getTodayData(terminalIdList, platform)));
        terminalOnlineSituationStatisticsDTOList = format(terminalOnlineSituationStatisticsDTOList, getLastOneMonth(currentDate));
        TerminalOnlineSituationStatisticsResponse terminalOnlineSituationStatisticsResponse = new TerminalOnlineSituationStatisticsResponse();
        terminalOnlineSituationStatisticsResponse.setTerminalOnlineSituationStatisticsList(terminalOnlineSituationStatisticsDTOList);
        return terminalOnlineSituationStatisticsResponse;
    }

    @Override
    public TerminalOnlineSituationStatisticsResponse statisticsTerminalHistoryOnlineSituationByYear(
            TerminalOnlineSituationStatisticsRequest request) {
        Assert.notNull(request, "request is not null");

        List<String> terminalIdList = buildTerminalIdList(request);
        String platform = request.getPlatform().toString();
        List<TerminalOnlineSituationStatisticsDTO> terminalOnlineSituationStatisticsDTOList = getLastOneYearData(terminalIdList, platform);
        Date currentDate = new Date();
        terminalOnlineSituationStatisticsDTOList.add(new TerminalOnlineSituationStatisticsDTO(DateFormatUtils.format(currentDate, DATE_FORMAT_DAY),
                getTodayData(terminalIdList, platform)));
        terminalOnlineSituationStatisticsDTOList = format(terminalOnlineSituationStatisticsDTOList, getLastOneYear(currentDate));
        TerminalOnlineSituationStatisticsResponse terminalOnlineSituationStatisticsResponse = new TerminalOnlineSituationStatisticsResponse();
        terminalOnlineSituationStatisticsResponse.setTerminalOnlineSituationStatisticsList(terminalOnlineSituationStatisticsDTOList);
        return terminalOnlineSituationStatisticsResponse;
    }

    private List<String> buildTerminalIdList(TerminalOnlineSituationStatisticsRequest request) {
        List<String> terminalIdList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(request.getGroupIdList())) {
            LOGGER.debug("groupIdList={}", request.getGroupIdList().toArray());
            List<UUID> groupIdList = HibernateUtil.handleQueryIncludeList(request.getGroupIdList());
            List<ViewTerminalSearchEntity> terminalSearchEntityList = viewTerminalSearchDAO.findByGroupIdIn(groupIdList);
            terminalIdList.addAll(terminalSearchEntityList.stream().map(ViewTerminalSearchEntity::getTerminalId).collect(Collectors.toList()));
            List<ComputerEntity> computerEntityList = computerBusinessDAO.findByTerminalGroupIdList(groupIdList);
            terminalIdList.addAll(computerEntityList.stream().map(ComputerEntity::getMac).collect(Collectors.toList()));
        }
        LOGGER.debug("terminalIdList={}", terminalIdList.toArray());
        return terminalIdList;
    }

    private List<TerminalOnlineSituationStatisticsDTO> getLastOneDayData(List<String> terminalIdList, String platform) {
        List<Object[]> resultList;
        if (CollectionUtils.isEmpty(terminalIdList)) {
            resultList = terminalOnlineSituationHourRecordDAO.countLastOneDayData(platform);
        } else {
            resultList = selectByTerminalId(terminalIdList, platform, (terminalIds, currPlatform) ->
                    terminalOnlineSituationHourRecordDAO.countLastOneDayDataByTerminalId(terminalIds, currPlatform));
        }
        return convertFor(resultList);
    }

    private List<String> getLastOneDay() {
        List<String> resultList = Lists.newArrayList();
        Date currentDate = new Date();
        for (int i = 0; i < TWENTY_FOUR_HOURS_AGO; i++) {
            Date date = DateUtils.addHours(currentDate, -i);
            String showDate = DateUtil.getHourRangeByHourKey(DateUtil.getStatisticHourKey(date));
            resultList.add(showDate);
        }
        LOGGER.debug("resultList={}", resultList.toArray());
        return resultList;
    }

    private List<TerminalOnlineSituationStatisticsDTO> getLastOneMonthData(List<String> terminalIdList, String platform) {
        List<Object[]> resultList;
        if (CollectionUtils.isEmpty(terminalIdList)) {
            resultList = terminalOnlineSituationDayRecordDAO.countLastOneMonthData(platform);
        } else {
            resultList = selectByTerminalId(terminalIdList, platform, (terminalIds, currPlatform) ->
                    terminalOnlineSituationDayRecordDAO.countLastOneMonthDataByTerminalId(terminalIds, currPlatform));
        }
        return convertFor(resultList);
    }

    private List<String> getLastOneMonth(Date currentDate) {
        List<String> resultList = Lists.newArrayList();
        for (int i = 0; i < ONE_MONTH_AGO; i++) {
            Date date = DateUtils.addDays(currentDate, -i);
            resultList.add(DateFormatUtils.format(date, DATE_FORMAT_DAY));
        }
        LOGGER.debug("resultList={}", resultList.toArray());
        return resultList;
    }

    private List<TerminalOnlineSituationStatisticsDTO> getLastOneYearData(List<String> terminalIdList, String platform) {
        List<Object[]> resultList;
        if (CollectionUtils.isEmpty(terminalIdList)) {
            resultList = terminalOnlineSituationDayRecordDAO.countLastOneYearData(platform);
        } else {
            resultList = selectByTerminalId(terminalIdList, platform, (terminalIds, currPlatform) ->
                    terminalOnlineSituationDayRecordDAO.countLastOneYearDataByTerminalId(terminalIds, currPlatform));
        }
        return convertFor(resultList);
    }

    private List<String> getLastOneYear(Date currentDate) {
        List<String> resultList = Lists.newArrayList();
        for (int i = 0; i < ONE_YEAR_AGO; i++) {
            Date date = DateUtils.addDays(currentDate, -i);
            resultList.add(DateFormatUtils.format(date, DATE_FORMAT_DAY));
        }
        LOGGER.debug("resultList={}", resultList.toArray());
        return resultList;
    }

    private String getTodayData(List<String> terminalIdList, String platform) {
        List<BigInteger> resultList;
        if (CollectionUtils.isEmpty(terminalIdList)) {
            resultList = terminalOnlineSituationHourRecordDAO.countTodayData(platform);
        } else {
            resultList = selectByTerminalId(terminalIdList, platform, (terminalIds, currPlatform) ->
                    terminalOnlineSituationHourRecordDAO.countTodayDataByTerminalId(terminalIds, currPlatform));
        }
        return resultList.get(0).toString();
    }

    private List<TerminalOnlineSituationStatisticsDTO> format(List<TerminalOnlineSituationStatisticsDTO> terminalOnlineSituationStatisticsDTOList,
            List<String> dateList) {
        dateList.forEach(date -> {
            if (terminalOnlineSituationStatisticsDTOList.stream().noneMatch(dto -> date.contains(dto.getDateTime()))) {
                terminalOnlineSituationStatisticsDTOList.add(new TerminalOnlineSituationStatisticsDTO(date, StringUtils.EMPTY));
            }
        });
        return terminalOnlineSituationStatisticsDTOList.stream().sorted(Comparator.comparing(TerminalOnlineSituationStatisticsDTO::getDateTime))
                .collect(Collectors.toList());
    }

    private List<TerminalOnlineSituationStatisticsDTO> convertFor(List<Object[]> countResult) {
        return countResult.stream().filter(this::verifyArray).map(args -> {
            String count = TypeUtils.castToString(args[0]);
            String dateTime = TypeUtils.castToString(args[1]);
            return new TerminalOnlineSituationStatisticsDTO(dateTime, count);
        }).collect(Collectors.toList());
    }

    private Boolean verifyArray(Object[] args) {
        return ArrayUtils.isNotEmpty(args) && args.length == 2 && Objects.nonNull(args[0]) && Objects.nonNull(args[1]);
    }

    /**
     * 根据终端列表并发查询统计信息
     *
     * @param terminalIdList 需要查询的终端列表
     * @param platform       部署类型
     * @param function       查询回调实现
     * @param <T>            返回结果类型
     * @return 返回统计信息
     */
    private <T> List<T> selectByTerminalId(List<String> terminalIdList, String platform, BiFunction<List<String>, String, List<T>> function) {

        final int terminalSize = terminalIdList.size();
        long floor = (long) Math.floor(new BigDecimal(terminalSize).divide(new BigDecimal(TERMINAL_SELECT_NUMS), RoundingMode.FLOOR).doubleValue());

        if (floor == 0) {
            return function.apply(terminalIdList, platform);
        }

        List<T> resultList = new ArrayList<>();
        long limit = (terminalSize + floor - 1) / floor;
        List<List<String>> groupNameList = Stream.iterate(0, n -> n + 1)
                .limit(floor)
                .map(index -> terminalIdList.stream().skip(index * limit).limit(limit).collect(Collectors.toList()))
                .collect(Collectors.toList());

        List<CompletableFuture<List<T>>> futureList = new ArrayList<>();
        groupNameList.forEach(groupList ->
                futureList.add(CompletableFuture.supplyAsync(() -> function.apply(groupList, platform), STATISTICS_TERMINAL_THREAD_POOL)));

        // 等待调用结束
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture<?>[0])).join();

        // 统计返回结果
        for (CompletableFuture<List<T>> future : futureList) {
            try {
                resultList.addAll(future.get());
            } catch (InterruptedException | ExecutionException ex) {
                LOGGER.error("获取异步结果列表失败", ex);
            }
        }
        return resultList;
    }
}
