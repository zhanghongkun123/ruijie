package com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.StatisticDesktopConnectFaultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.StatisticDayDesktopSessionLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.StatisticDesktopPoolDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.DesktopStatisticsTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.request.StatisticDesktopPoolHistoryInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.response.StatisticDesktopPoolHistoryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.CompensateSessionType;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 桌面池报表Service接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/18 16:55
 *
 * @author yxq
 */
public interface DesktopPoolDashboardService {

    /**
     * 以小时为单位，统计桌面池相关信息
     *
     * @param request 统计请求
     * @return 统计结果
     */
    StatisticDesktopPoolHistoryResponse statisticByHourStep(StatisticDesktopPoolHistoryInfoRequest request);

    /**
     * 以天为单位，统计桌面池相关信息
     *
     * @param request       统计请求
     * @param statisticDays 统计多少天
     * @return 统计结果
     */
    StatisticDesktopPoolHistoryResponse statisticByDayStep(StatisticDesktopPoolHistoryInfoRequest request, int statisticDays);

    /**
     * 获取当前时间，各个桌面池的使用情况
     *
     * @return 使用情况
     */
    List<StatisticDesktopPoolDesktopCountDTO> getAllDesktopPoolUseInfo();

    /**
     * 获取指定时间内，每个桌面池、桌面组的连接失败数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return List<StatisticConnectFaultDTO>
     */
    List<StatisticDesktopConnectFaultDTO> getConnectFaultCountList(Date startTime, Date endTime);

    /**
     * 获取一天内的桌面池使用率统计信息
     *
     * @param dayKey   天key
     * @param typeList 类型
     * @return 统计结果
     */
    List<StatisticDayDesktopSessionLogDTO> getMaxUsedRateList(String dayKey, List<DesktopStatisticsTypeEnum> typeList);

    /**
     * 获取一天内的桌面池连接失败统计信息
     *
     * @param dayKey 天key
     * @param type   类型
     * @return 统计结果
     */
    List<StatisticDayDesktopSessionLogDTO> getDayConnectFaultCountList(String dayKey, DesktopStatisticsTypeEnum type);

    /**
     * 获取当前时间所有桌面池的数量信息
     *
     * @return 所有桌面池的数量信息
     */
    StatisticDesktopPoolDesktopCountDTO getCurrentAllDesktopPoolInfo();

    /**
     * 获取当前时间指定桌面池的数量信息
     *
     * @param deskPoolId 桌面池id
     * @return 指定桌面池的数量信息
     */
    StatisticDesktopPoolDesktopCountDTO getCurrentDesktopPoolInfo(UUID deskPoolId);

    /**
     * 获取当前时间指定桌面池的数量信息
     *
     * @param desktopPoolType 桌面池类型(动态、静态)
     * @param cbbDesktopPoolType 桌面池类型(VDI、第三方)
     * @return 指定桌面池的数量信息
     */
    StatisticDesktopPoolDesktopCountDTO getCurrentAllDesktopPoolInfo(@Nullable DesktopPoolType desktopPoolType,
                                                                     CbbDesktopPoolType cbbDesktopPoolType);

    /**
     * 清除过期数据
     *
     * @param currentDate 当前日期
     */
    void clearExpiredData(Date currentDate);

    /**
     * 以天为单位统计数据
     *
     * @param monthKey 月key
     * @param dayKey   天key
     */
    void statisticDayData(String monthKey, String dayKey);

    /**
     * 统计指定时间这一小时的连接失败数量
     *
     * @param date 指定的时间
     */
    void statisticHourConnectFaultData(Date date);

    /**
     * 将会话记录中未结束，但是实际上已经不存在的会话记录结束掉
     *
     * @param compensateSessionType 会话补偿类型
     */
    void compensateFishedSessionLog(CompensateSessionType compensateSessionType);
}
