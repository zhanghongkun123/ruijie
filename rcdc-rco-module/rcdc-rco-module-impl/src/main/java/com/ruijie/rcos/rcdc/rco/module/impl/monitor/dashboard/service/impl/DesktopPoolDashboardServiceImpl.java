package com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.ex.CbbDesktopNotExistsException;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.DesktopPoolStatisticsResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.StatisticDesktopConnectFaultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.StatisticDayDesktopSessionLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.StatisticDesktopPoolDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.DesktopSessionLogState;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.DesktopStatisticsTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.RelateTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.request.StatisticDesktopPoolHistoryInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.response.StatisticDesktopPoolHistoryResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.*;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopSessionLogDayRecordEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopSessionLogEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopSessionLogHourRecordEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewStatisticDesktopCountEntity;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.CompensateSessionType;
import com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.service.DesktopPoolDashboardService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: 桌面池报表Service实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/18 16:55
 *
 * @author yxq
 */
@Service
public class DesktopPoolDashboardServiceImpl implements DesktopPoolDashboardService {

    /**
     * 数据库数据保留时间
     */
    private static final int LOG_TABLE_DATA_KEEP_DAY = -180;

    public static final int TWENTY_THREE_HOURS_AGO = 23;

    public static final int ONE_WEEK_DAYS = 6;

    public static final int ONE_MONTH_DAYS = 29;

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolDashboardServiceImpl.class);

    @Autowired
    private DesktopSessionLogHourRecordDAO desktopSessionLogHourRecordDAO;

    @Autowired
    private DesktopSessionLogDayRecordDAO desktopSessionLogDayRecordDAO;

    @Autowired
    private DesktopSessionLogDAO desktopSessionLogDAO;

    @Autowired
    private UserConnectDesktopFaultLogDAO userConnectDesktopFaultLogDAO;

    @Autowired
    private ViewStatisticDesktopCountDAO viewStatisticDesktopCountDAO;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private DesktopPoolService desktopPoolService;

    @Autowired
    private DesktopAPI desktopAPI;

    @Override
    public StatisticDesktopPoolHistoryResponse statisticByHourStep(StatisticDesktopPoolHistoryInfoRequest request) {
        Assert.notNull(request, "request must not be null");

        Date endTime = request.getEndTime();
        String endHourKey = DateUtil.getStatisticHourKey(endTime);
        String startHourKey = DateUtil.getStatisticHourKey(DateUtils.addHours(endTime, -TWENTY_THREE_HOURS_AGO));

        List<DesktopPoolStatisticsResultDTO> usedRateResultList = new ArrayList<>();
        List<DesktopPoolStatisticsResultDTO> connectFaultResultList = new ArrayList<>();
        UUID deskPoolId = request.getDesktopPoolId();

        if (Objects.nonNull(deskPoolId)) {
            // 指定某个桌面池查询
            usedRateResultList = desktopSessionLogHourRecordDAO.obtainByHourKeyAndTypeAndRelatedId(startHourKey, endHourKey,
                    DesktopStatisticsTypeEnum.SINGLE_USED_RATE, deskPoolId);
            connectFaultResultList = desktopSessionLogHourRecordDAO.obtainByHourKeyAndTypeAndRelatedId(startHourKey, endHourKey,
                    DesktopStatisticsTypeEnum.CONNECT_FAIL, deskPoolId);
        } else if (Objects.isNull(request.getDesktopPoolType()) && Objects.isNull(request.getDesktopSourceType())) {
            // 查询全部桌面池
            usedRateResultList = desktopSessionLogHourRecordDAO.obtainTotalUsedRateByHourKey(startHourKey, endHourKey,
                    DesktopStatisticsTypeEnum.TOTAL_USED_RATE);
            connectFaultResultList = desktopSessionLogHourRecordDAO.obtainConnectFaultCountByHourKey(startHourKey, endHourKey,
                    DesktopStatisticsTypeEnum.CONNECT_FAIL);
        } else if (CbbDesktopPoolType.VDI == request.getDesktopSourceType()) {
            // 根据desktopPoolType和desktopSourceType判断是查询VDI还是第三方的全部静态池/全部动态池
            DesktopStatisticsTypeEnum statisticsTypeEnum = DesktopPoolType.STATIC == request.getDesktopPoolType() ?
                    DesktopStatisticsTypeEnum.TOTAL_VDI_STATIC_USED_RATE : DesktopStatisticsTypeEnum.TOTAL_VDI_DYNAMIC_USED_RATE;
            usedRateResultList = desktopSessionLogHourRecordDAO.obtainTotalUsedRateByHourKey(startHourKey,
                    endHourKey, statisticsTypeEnum);

            statisticsTypeEnum = DesktopPoolType.STATIC == request.getDesktopPoolType() ?
                    DesktopStatisticsTypeEnum.TOTAL_VDI_STATIC_CONNECT_FAIL :
                    DesktopStatisticsTypeEnum.TOTAL_VDI_DYNAMIC_CONNECT_FAIL;
            connectFaultResultList = desktopSessionLogHourRecordDAO.obtainConnectFaultCountByHourKey(startHourKey,
                    endHourKey, statisticsTypeEnum);
        } else if (CbbDesktopPoolType.THIRD == request.getDesktopSourceType()) {
            DesktopStatisticsTypeEnum statisticsTypeEnum = DesktopPoolType.STATIC == request.getDesktopPoolType() ?
                    DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_STATIC_USED_RATE :
                    DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_DYNAMIC_USED_RATE;
            usedRateResultList = desktopSessionLogHourRecordDAO.obtainTotalUsedRateByHourKey(startHourKey,
                    endHourKey, statisticsTypeEnum);

            statisticsTypeEnum = DesktopPoolType.STATIC == request.getDesktopPoolType() ?
                    DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_STATIC_CONNECT_FAIL :
                    DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_DYNAMIC_CONNECT_FAIL;
            connectFaultResultList = desktopSessionLogHourRecordDAO.obtainConnectFaultCountByHourKey(startHourKey,
                    endHourKey, statisticsTypeEnum);
        }

        // 如果结束时间是当前时间的话，需要补充这个小时的数据
        Date currentDate = new Date();
        if (DateUtil.getStatisticHourKey(currentDate).equals(endHourKey)) {
            connectFaultResultList.add(getThisHourConnectFault(deskPoolId, request.getDesktopPoolType(), currentDate));
        }
        // 把hourKey转换成时间范围
        connectFaultResultList = connectFaultResultList.stream().map(dto -> new DesktopPoolStatisticsResultDTO(
                DateUtil.getHourRangeByHourKey(dto.getDateTime()), dto.getCount())).collect(Collectors.toList());
        usedRateResultList = usedRateResultList.stream().map(dto -> new DesktopPoolStatisticsResultDTO(
                DateUtil.getHourRangeByHourKey(dto.getDateTime()), dto.getCount())).collect(Collectors.toList());

        StatisticDesktopPoolHistoryResponse response = new StatisticDesktopPoolHistoryResponse();
        response.setConnectFaultStatisticsResultList(format(connectFaultResultList, getLastOneDayHourKey(endTime)));
        response.setDeskUseRateStatisticsResultList(format(usedRateResultList, getLastOneDayHourKey(endTime)));

        return response;
    }


    @Override
    public StatisticDesktopPoolHistoryResponse statisticByDayStep(StatisticDesktopPoolHistoryInfoRequest request, int statisticDays) {
        Assert.notNull(request, "request must not be null");

        Date endTime = request.getEndTime();
        String startDayKey = DateUtil.getStatisticDayKey(DateUtils.addDays(endTime, -statisticDays));
        String endDayKey = DateUtil.getStatisticDayKey(endTime);

        // 统计过去n天每天的最大使用率
        List<DesktopPoolStatisticsResultDTO> usedRateResultList = new ArrayList<>();
        // 统计过去n天每天的错误次数
        List<DesktopPoolStatisticsResultDTO> connectFaultResultList = new ArrayList<>();
        UUID deskPoolId = request.getDesktopPoolId();
        if (Objects.nonNull(deskPoolId)) {
            // 单个桌面池
            usedRateResultList = desktopSessionLogDayRecordDAO.obtainByDayKeyAndTypeAndRelatedId(startDayKey, endDayKey,
                    DesktopStatisticsTypeEnum.SINGLE_USED_RATE, deskPoolId);
            connectFaultResultList = desktopSessionLogDayRecordDAO.obtainByDayKeyAndTypeAndRelatedId(startDayKey, endDayKey,
                    DesktopStatisticsTypeEnum.CONNECT_FAIL, deskPoolId);
        } else if (Objects.isNull(request.getDesktopPoolType()) && Objects.isNull(request.getDesktopSourceType())) {
            // 全部桌面池
            usedRateResultList = desktopSessionLogDayRecordDAO.obtainTotalUsedRateByDayKey(startDayKey, endDayKey,
                    DesktopStatisticsTypeEnum.TOTAL_USED_RATE);
            connectFaultResultList = desktopSessionLogDayRecordDAO.obtainTotalConnectFaultCountByDayKey(startDayKey, endDayKey,
                    DesktopStatisticsTypeEnum.CONNECT_FAIL);
        } else if (CbbDesktopPoolType.VDI == request.getDesktopSourceType()) {
            // VDI桌面池
            DesktopStatisticsTypeEnum statisticsTypeEnum = DesktopPoolType.STATIC == request.getDesktopPoolType() ?
                    DesktopStatisticsTypeEnum.TOTAL_VDI_STATIC_USED_RATE :
                    DesktopStatisticsTypeEnum.TOTAL_VDI_DYNAMIC_USED_RATE;
            usedRateResultList = desktopSessionLogDayRecordDAO.obtainTotalUsedRateByDayKey(startDayKey, endDayKey, statisticsTypeEnum);

            statisticsTypeEnum = DesktopPoolType.STATIC == request.getDesktopPoolType() ?
                    DesktopStatisticsTypeEnum.TOTAL_VDI_STATIC_CONNECT_FAIL :
                    DesktopStatisticsTypeEnum.TOTAL_VDI_DYNAMIC_CONNECT_FAIL;
            connectFaultResultList = desktopSessionLogDayRecordDAO.obtainTotalConnectFaultCountByDayKey(startDayKey, endDayKey, statisticsTypeEnum);
        } else if (CbbDesktopPoolType.THIRD == request.getDesktopSourceType()) {
            // 第三方桌面池
            DesktopStatisticsTypeEnum statisticsTypeEnum = DesktopPoolType.STATIC == request.getDesktopPoolType() ?
                    DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_STATIC_USED_RATE :
                    DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_DYNAMIC_USED_RATE;
            usedRateResultList = desktopSessionLogDayRecordDAO.obtainTotalUsedRateByDayKey(startDayKey, endDayKey, statisticsTypeEnum);

            statisticsTypeEnum = DesktopPoolType.STATIC == request.getDesktopPoolType() ?
                    DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_STATIC_CONNECT_FAIL :
                    DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_DYNAMIC_CONNECT_FAIL;
            connectFaultResultList = desktopSessionLogDayRecordDAO.obtainTotalConnectFaultCountByDayKey(startDayKey, endDayKey, statisticsTypeEnum);
        }

        // 如果结束时间是今天，需要补充上今天的数据
        Date currentDate = new Date();
        if (DateUtil.getStatisticDayKey(currentDate).equals(endDayKey)) {
            connectFaultResultList.add(getTodayConnectFaultCount(deskPoolId, request.getDesktopPoolType(), currentDate));
            usedRateResultList.add(getTodayUsedRate(deskPoolId, request.getDesktopSourceType(), request.getDesktopPoolType(), currentDate));
        }

        // 格式化
        StatisticDesktopPoolHistoryResponse response = new StatisticDesktopPoolHistoryResponse();
        response.setConnectFaultStatisticsResultList(format(connectFaultResultList, getDayKeyList(endTime, statisticDays)));
        response.setDeskUseRateStatisticsResultList(format(usedRateResultList, getDayKeyList(endTime, statisticDays)));

        return response;
    }

    private DesktopPoolStatisticsResultDTO getTodayUsedRate(UUID deskPoolId, CbbDesktopPoolType cbbDesktopPoolType,
                                                            DesktopPoolType desktopPoolType, Date currentDate) {
        String dayKey = DateUtil.getStatisticDayKey(currentDate);
        Double count = null;
        // 如果为空，则查询全部桌面池的信息，不为空则查询单个桌面池的信息
        if (Objects.nonNull(deskPoolId)) {
            count = desktopSessionLogHourRecordDAO.obtainMaxUsedRateInDay(dayKey, deskPoolId, DesktopStatisticsTypeEnum.SINGLE_USED_RATE);
        } else if (Objects.isNull(desktopPoolType) && Objects.isNull(cbbDesktopPoolType)) {
            // 全部
            count = desktopSessionLogHourRecordDAO.obtainTotalMaxUsedRateInDay(dayKey, DesktopStatisticsTypeEnum.TOTAL_USED_RATE);
        } else if (CbbDesktopPoolType.VDI == cbbDesktopPoolType) {
            // VDI全部静态池或全部动态池
            DesktopStatisticsTypeEnum statisticsTypeEnum = desktopPoolType == DesktopPoolType.STATIC ?
                    DesktopStatisticsTypeEnum.TOTAL_VDI_STATIC_USED_RATE : DesktopStatisticsTypeEnum.TOTAL_VDI_DYNAMIC_USED_RATE;
            count = desktopSessionLogHourRecordDAO.obtainTotalMaxUsedRateInDay(dayKey, statisticsTypeEnum);
        } else if (CbbDesktopPoolType.THIRD == cbbDesktopPoolType) {
            // 第三方全部静态池或全部动态池
            DesktopStatisticsTypeEnum statisticsTypeEnum = desktopPoolType == DesktopPoolType.STATIC ?
                    DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_STATIC_USED_RATE : DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_DYNAMIC_USED_RATE;
            count = desktopSessionLogHourRecordDAO.obtainTotalMaxUsedRateInDay(dayKey, statisticsTypeEnum);
        }
        LOGGER.debug("补充当天桌面池使用率为：{}", count);
        return new DesktopPoolStatisticsResultDTO(DateUtil.getStatisticDayKey(currentDate), Optional.ofNullable(count).orElse(0.0));
    }

    private DesktopPoolStatisticsResultDTO getTodayConnectFaultCount(UUID deskPoolId, DesktopPoolType desktopPoolType, Date currentDate) {
        int count;
        if (Objects.nonNull(deskPoolId)) {
            count = userConnectDesktopFaultLogDAO.countByRelatedIdAndFaultTimeAfter(deskPoolId, DateUtil.getDayStartTime(currentDate));
        } else if (Objects.isNull(desktopPoolType)) {
            // 全部
            count = userConnectDesktopFaultLogDAO.countByFaultTimeAfter(DateUtil.getDayStartTime(currentDate));
        } else {
            // 全部静态池或全部动态池
            count = userConnectDesktopFaultLogDAO.countByDesktopPoolTypeAndFaultTimeAfter(desktopPoolType, DateUtil.getDayStartTime(currentDate));
        }
        LOGGER.debug("补充当天会话连接失败数量为：{}", count);
        return new DesktopPoolStatisticsResultDTO(DateUtil.getStatisticDayKey(currentDate), (double) count);
    }

    private DesktopPoolStatisticsResultDTO getThisHourConnectFault(UUID deskPoolId, DesktopPoolType desktopPoolType, Date currentDate) {
        int count;
        if (Objects.nonNull(deskPoolId)) {
            count = userConnectDesktopFaultLogDAO.countByRelatedIdAndFaultTimeAfter(deskPoolId, DateUtil.getHourStart(currentDate));
        } else if (Objects.isNull(desktopPoolType)) {
            // 全部桌面池
            count = userConnectDesktopFaultLogDAO.countByFaultTimeAfter(DateUtil.getHourStart(currentDate));
        } else {
            // 全部静态池或者全部动态池
            count = userConnectDesktopFaultLogDAO.countByDesktopPoolTypeAndFaultTimeAfter(desktopPoolType, DateUtil.getHourStart(currentDate));
        }

        LOGGER.debug("补充当前小时会话连接失败数量为：{}", count);
        return new DesktopPoolStatisticsResultDTO(DateUtil.getStatisticHourKey(currentDate), (double) count);
    }

    private List<DesktopPoolStatisticsResultDTO> format(List<DesktopPoolStatisticsResultDTO> resultDTOList, List<String> dateList) {
        dateList.forEach(date -> {
            if (resultDTOList.stream().noneMatch(dto -> dto.getDateTime().equals(date))) {
                resultDTOList.add(new DesktopPoolStatisticsResultDTO(date, 0.0));
            }
        });

        return resultDTOList.stream().sorted(Comparator.comparing(DesktopPoolStatisticsResultDTO::getDateTime)).distinct()
                .collect(Collectors.toList());
    }

    private List<String> getDayKeyList(Date endTime, int day) {
        List<String> resultList = Lists.newArrayList();
        for (int i = 0; i <= day; i++) {
            String showDate = DateUtil.getStatisticDayKey(DateUtils.addDays(endTime, -i));
            resultList.add(showDate);
        }
        return resultList;
    }

    private List<String> getLastOneDayHourKey(Date endTime) {
        List<String> resultList = Lists.newArrayList();
        for (int i = 0; i <= TWENTY_THREE_HOURS_AGO; i++) {
            String hourKey = DateUtil.getStatisticHourKey(DateUtils.addHours(endTime, -i));
            resultList.add(DateUtil.getHourRangeByHourKey(hourKey));
        }
        return resultList;
    }

    @Override
    public List<StatisticDesktopPoolDesktopCountDTO> getAllDesktopPoolUseInfo() {
        return viewStatisticDesktopCountDAO.findAll().stream().map(ViewStatisticDesktopCountEntity::convertFor).collect(Collectors.toList());
    }

    @Override
    public List<StatisticDesktopConnectFaultDTO> getConnectFaultCountList(Date startTime, Date endTime) {
        Assert.notNull(startTime, "startTime must not be null");
        Assert.notNull(endTime, "endTime must not be null");

        return userConnectDesktopFaultLogDAO.countConnectFaultCount(startTime, endTime).stream().map(StatisticDesktopConnectFaultDTO::convertFor)
                .collect(Collectors.toList());
    }

    @Override
    public List<StatisticDayDesktopSessionLogDTO> getMaxUsedRateList(String dayKey, List<DesktopStatisticsTypeEnum> typeList) {
        Assert.notNull(dayKey, "dayKey must not be null");
        Assert.notNull(typeList, "typeList must not be null");

        return desktopSessionLogHourRecordDAO.obtainMaxUsedRateByDay(dayKey, typeList);
    }

    @Override
    public List<StatisticDayDesktopSessionLogDTO> getDayConnectFaultCountList(String dayKey, DesktopStatisticsTypeEnum type) {
        Assert.notNull(dayKey, "dayKey must not be null");
        Assert.notNull(type, "type must not be null");

        return desktopSessionLogHourRecordDAO.obtainTotalConnectFaultNumByDay(dayKey, type);
    }

    @Override
    public StatisticDesktopPoolDesktopCountDTO getCurrentAllDesktopPoolInfo() {
        ViewStatisticDesktopCountEntity entity = viewStatisticDesktopCountDAO.obtainTotalInfo();
        return Optional.ofNullable(entity).map(ViewStatisticDesktopCountEntity::convertFor).orElseGet(StatisticDesktopPoolDesktopCountDTO::new);
    }

    @Override
    public StatisticDesktopPoolDesktopCountDTO getCurrentDesktopPoolInfo(UUID deskPoolId) {
        Assert.notNull(deskPoolId, "deskPoolId must not be null");

        ViewStatisticDesktopCountEntity entity = viewStatisticDesktopCountDAO.findByDesktopPoolId(deskPoolId);
        return Optional.ofNullable(entity).map(ViewStatisticDesktopCountEntity::convertFor).orElseGet(StatisticDesktopPoolDesktopCountDTO::new);
    }

    @Override
    public StatisticDesktopPoolDesktopCountDTO getCurrentAllDesktopPoolInfo(@Nullable DesktopPoolType desktopPoolType, CbbDesktopPoolType cbbDesktopPoolType) {
        Assert.notNull(cbbDesktopPoolType, "cbbDesktopPoolType must not be null");

        ViewStatisticDesktopCountEntity entity;
        if (Objects.isNull(desktopPoolType)) {
            entity = viewStatisticDesktopCountDAO.statisticByCbbDesktopPoolType(cbbDesktopPoolType);
        } else {
            entity = viewStatisticDesktopCountDAO.statisticByDesktopPoolTypeAndCbbDesktopPoolType(desktopPoolType, cbbDesktopPoolType);
        }

        return Optional.ofNullable(entity).map(ViewStatisticDesktopCountEntity::convertFor).orElseGet(StatisticDesktopPoolDesktopCountDTO::new);
    }

    @Override
    public void clearExpiredData(Date currentDate) {
        Assert.notNull(currentDate, "currentDate must not be null");

        Date todayStartTime = DateUtil.getDayStartTime(currentDate);
        try {
            // 桌面log表保存180天
            desktopSessionLogDAO.deleteByCreateTimeBefore(DateUtils.addDays(todayStartTime, LOG_TABLE_DATA_KEEP_DAY));
        } catch (Exception e) {
            LOGGER.error("定时任务清空桌面会话登录表过期记录失败", e);
        }

        try {
            // 连接失败log表保存180天
            userConnectDesktopFaultLogDAO.deleteByFaultTimeBefore(DateUtils.addDays(todayStartTime, LOG_TABLE_DATA_KEEP_DAY));
        } catch (Exception e) {
            LOGGER.error("定时任务清空桌面分配失败表过期记录失败", e);
        }

        try {
            // 桌面hour log表保存180天
            desktopSessionLogHourRecordDAO.deleteByDayKeyBefore(DateUtil.getStatisticDayKey(
                    DateUtils.addDays(todayStartTime, LOG_TABLE_DATA_KEEP_DAY)));
        } catch (Exception e) {
            LOGGER.error("定时任务清空桌面会话登录hour表过期记录失败", e);
        }

        try {
            // 桌面day log表保存180天
            desktopSessionLogDayRecordDAO.deleteByDayKeyBefore(DateUtil.getStatisticDayKey(
                    DateUtils.addDays(todayStartTime, LOG_TABLE_DATA_KEEP_DAY)));
        } catch (Exception e) {
            LOGGER.error("定时任务清空桌面会话登录day表过期记录失败", e);
        }
    }

    @Override
    public void statisticDayData(String monthKey, String dayKey) {
        Assert.notNull(monthKey, "monthKey must not be null");
        Assert.notNull(dayKey, "dayKey must not be null");

        LOGGER.debug("统计报表相关信息，dayKey={}，monthKey={}", dayKey, monthKey);
        // 统计每个桌面池一天使用率信息
        List<StatisticDayDesktopSessionLogDTO> maxUsedRateList = getMaxUsedRateList(dayKey,
                Lists.newArrayList(DesktopStatisticsTypeEnum.SINGLE_USED_RATE, DesktopStatisticsTypeEnum.TOTAL_VDI_DYNAMIC_USED_RATE,
                        DesktopStatisticsTypeEnum.TOTAL_VDI_STATIC_USED_RATE, DesktopStatisticsTypeEnum.TOTAL_USED_RATE));
        // 统计每个桌面池连连接失败数量
        List<StatisticDayDesktopSessionLogDTO> connectFaultCountList = new ArrayList<>();
        connectFaultCountList.addAll(getDayConnectFaultCountList(dayKey, DesktopStatisticsTypeEnum.CONNECT_FAIL));

        // 全部VDI静态池
        connectFaultCountList.addAll(getDayConnectFaultCountList(dayKey, DesktopStatisticsTypeEnum.TOTAL_VDI_STATIC_CONNECT_FAIL));
        // 全部VDI动态池
        connectFaultCountList.addAll(getDayConnectFaultCountList(dayKey, DesktopStatisticsTypeEnum.TOTAL_VDI_DYNAMIC_CONNECT_FAIL));

        // 全部第三方静态池
        connectFaultCountList.addAll(getDayConnectFaultCountList(dayKey, DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_STATIC_CONNECT_FAIL));
        // 全部第三方动态池
        connectFaultCountList.addAll(getDayConnectFaultCountList(dayKey, DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_DYNAMIC_CONNECT_FAIL));

        // 构造实体列表
        List<DesktopSessionLogDayRecordEntity> resultList = new ArrayList<>(maxUsedRateList.size() + connectFaultCountList.size());
        Stream.of(maxUsedRateList, connectFaultCountList).flatMap(Collection::stream).forEach(dto -> {
            DesktopSessionLogDayRecordEntity entity = new DesktopSessionLogDayRecordEntity(dayKey, monthKey);
            entity.setRelatedType(dto.getRelatedType());
            entity.setRelatedId(dto.getRelatedId());
            entity.setType(dto.getType());
            entity.setCount(dto.getCount());
            resultList.add(entity);
        });
        // 入库
        desktopSessionLogDayRecordDAO.saveAll(resultList);
    }

    @Override
    public void statisticHourConnectFaultData(Date date) {
        Assert.notNull(date, "date must not be null");

        // 获取相关的key
        String hourKey = DateUtil.getStatisticHourKey(date);
        String dayKey = DateUtil.getStatisticDayKey(date);
        Date startTime = DateUtil.getHourStart(date);
        Date endTime = DateUtil.getHourEnd(date);

        // 过去一小时连接失败数
        List<StatisticDesktopConnectFaultDTO> connectFaultCountList = getConnectFaultCountList(startTime, endTime);
        if (CollectionUtils.isEmpty(connectFaultCountList)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("时间段{}-{}，无连接失败数记录", dayKey, hourKey);
            }
            return;
        }
        List<DesktopSessionLogHourRecordEntity> entityList = connectFaultCountList.stream().map(dto -> {
            DesktopSessionLogHourRecordEntity entity = new DesktopSessionLogHourRecordEntity(hourKey, dayKey);
            entity.setCount(TypeUtils.castToDouble(dto.getCount()));
            entity.setRelatedId(dto.getRelatedId());
            entity.setRelatedType(dto.getRelatedType());
            entity.setType(DesktopStatisticsTypeEnum.CONNECT_FAIL);
            return entity;
        }).collect(Collectors.toList());

        // 入库
        desktopSessionLogHourRecordDAO.saveAll(entityList);

        // 统计VDI静态池全部和动态池全部
        addPoolTypeTotalFaultRecord(hourKey, dayKey, CbbDesktopPoolType.VDI, DesktopPoolType.STATIC, connectFaultCountList);
        addPoolTypeTotalFaultRecord(hourKey, dayKey, CbbDesktopPoolType.VDI, DesktopPoolType.DYNAMIC, connectFaultCountList);

        // 统计第三方静态池全部和动态池全部
        addPoolTypeTotalFaultRecord(hourKey, dayKey, CbbDesktopPoolType.THIRD, DesktopPoolType.STATIC, connectFaultCountList);
        addPoolTypeTotalFaultRecord(hourKey, dayKey, CbbDesktopPoolType.THIRD, DesktopPoolType.DYNAMIC, connectFaultCountList);
    }

    private void addPoolTypeTotalFaultRecord(String hourKey, String dayKey, CbbDesktopPoolType cbbDesktopPoolType,
                                             DesktopPoolType desktopPoolType,
                                             List<StatisticDesktopConnectFaultDTO> connectFaultCountList) {
        int count = connectFaultCountList.stream().filter(item -> {
            try {
                return item.getDesktopPoolType() == desktopPoolType
                        && cbbDesktopPoolType == desktopPoolService.getDesktopPoolBasicById(item.getRelatedId()).getPoolType();
            } catch (BusinessException e) {
                LOGGER.error("桌面池[{}]信息不存在", String.valueOf(item.getRelatedId()), e);
            }
            return false;
        }).mapToInt(StatisticDesktopConnectFaultDTO::getCount).sum();

        if (count > 0) {
            DesktopSessionLogHourRecordEntity entity = new DesktopSessionLogHourRecordEntity(hourKey, dayKey);
            entity.setCount(TypeUtils.castToDouble(count));
            entity.setRelatedType(RelateTypeEnum.DESKTOP_POOL);
            if (CbbDesktopPoolType.VDI == cbbDesktopPoolType) {
                entity.setType(desktopPoolType == DesktopPoolType.STATIC ? DesktopStatisticsTypeEnum.TOTAL_VDI_STATIC_CONNECT_FAIL :
                        DesktopStatisticsTypeEnum.TOTAL_VDI_DYNAMIC_CONNECT_FAIL);
            } else if (CbbDesktopPoolType.THIRD == cbbDesktopPoolType) {
                entity.setType(desktopPoolType == DesktopPoolType.STATIC ? DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_STATIC_CONNECT_FAIL :
                        DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_DYNAMIC_CONNECT_FAIL);
            }
            desktopSessionLogHourRecordDAO.save(entity);
        }
    }

    @Override
    public void compensateFishedSessionLog(CompensateSessionType compensateSessionType) {
        Assert.notNull(compensateSessionType, "compensateSessionType must not be null");

        // 查看退出时间为空的桌面id列表
        List<DesktopSessionLogEntity> entityList = desktopSessionLogDAO.findByState(DesktopSessionLogState.CONNECTING);
        // 最终需要修改的列表
        List<DesktopSessionLogEntity> needUpdateEntityList = new ArrayList<>();

        Map<UUID, CbbCloudDeskState> deskStateMap = new HashMap<>();
        Date currentTime = new Date();
        for (DesktopSessionLogEntity entity : entityList) {
            Long connectionId = entity.getConnectionId();
            UUID desktopId = entity.getDesktopId();
            try {
                // 获取云桌面状态，启动时从RCCP获取，定时任务直接查表
                CbbCloudDeskState deskState = getDeskState(desktopId, compensateSessionType, deskStateMap);

                // 先判断桌面是否正在运行，如果不在运行在，则证明会话已经结束
                if (deskState != CbbCloudDeskState.RUNNING) {
                    LOGGER.info("桌面[{}]状态为[{}]，结束会话[{}]", desktopId, deskState, connectionId);
                    entity.setLogoutTime(currentTime);
                    entity.setState(DesktopSessionLogState.FINISHED);
                    needUpdateEntityList.add(entity);
                    continue;
                }

                if (Objects.isNull(connectionId)) {
                    continue;
                }

                if (!desktopAPI.isAnyConnectedChannel(desktopId)) {
                    LOGGER.info("桌面[{}]会话列表中没有会话[{}]，结束会话", desktopId, connectionId);
                    entity.setLogoutTime(currentTime);
                    entity.setState(DesktopSessionLogState.FINISHED);
                    needUpdateEntityList.add(entity);
                }

            } catch (CbbDesktopNotExistsException e) {
                LOGGER.error("桌面[" + desktopId + "]不存在，结束会话[" + connectionId + "]", e);
                entity.setLogoutTime(currentTime);
                entity.setState(DesktopSessionLogState.FINISHED);
                needUpdateEntityList.add(entity);
            } catch (BusinessException e) {
                LOGGER.error("补偿云桌面[" + desktopId + "]会话[" + connectionId + "]失败，失败原因：", e);
            }

        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("需要修改的列表为：[{}]", JSON.toJSONString(entityList));
        }
        // 入库
        desktopSessionLogDAO.saveAll(needUpdateEntityList);
    }

    private CbbCloudDeskState getDeskState(UUID desktopId, CompensateSessionType compensateSessionType, Map<UUID, CbbCloudDeskState> deskStateMap)
            throws BusinessException {
        if (deskStateMap.containsKey(desktopId)) {
            return deskStateMap.get(desktopId);
        }
        CbbCloudDeskState deskState;
        if (compensateSessionType == CompensateSessionType.START_UP) {
            deskState = cbbVDIDeskMgmtAPI.queryDeskState(desktopId);
        } else {
            deskState = cbbVDIDeskMgmtAPI.getDeskVDI(desktopId).getDeskState();
        }
        deskStateMap.put(desktopId, deskState);
        return deskState;
    }
}
