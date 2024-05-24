package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopNormalDistributionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.NormalDistributionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageMonthDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageDayEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageMonthEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesktopNormalDistributionService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.util.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 云桌面资源使用率正态分布建模工具
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 12:17
 *
 * @author brq
 */
@Service
public class DesktopNormalDistributionServiceImpl implements DesktopNormalDistributionService {

    /**
     * 无效的方差值
     */
    private static final double INVALIDATE_STANDARD_DEVIATION = 0.0;

    /**
     * 无效的使用率
     */
    private static final double INVALIDATE_USAGE_VALUE = 0.0;

    /**
     * 云桌面正态分布建模数据统计区间日期
     */
    private static final Integer DESKTOP_NORMAL_DISTRIBUTION_DAYS = 7;

    @Autowired
    private DesktopResourceUsageDayDAO desktopResourceUsageDayDAO;

    @Autowired
    private DesktopResourceUsageMonthDAO desktopResourceUsageMonthDAO;

    /**
     * 计算云桌面正太分布建模数据
     * 1、获取全部云桌面最近7天资源使用率数据
     * 2、获取全部云桌面最近一个月的平均资源使用率数据
     * 3、计算各个云桌面最近7天和最近1个月，共8组数据的平均值
     * 4、根据每个云桌面的平均值进行正态分布建模数据计算
     * 5、获取全部云桌面最近1天的资源使用率数据
     * 6、并判断每个云桌面数据在建模数据中的分布情况，做数量统计
     *
     * @return Map<ResourceTypeEnum, DesktopNormalDistributionDTO> 返回数据
     */
    @Override
    public Map<ResourceTypeEnum, DesktopNormalDistributionDTO> normalDistribution() {
        Map<ResourceTypeEnum, DesktopNormalDistributionDTO> distributionMap = Maps.newHashMap();
        LocalDate nowDate = LocalDate.now();

        // 1、获取全部云桌面最近7天资源使用率数据
        List<DesktopResourceUsageDayEntity> dayEntityList = getDesktopResourceUsageDayEntities(nowDate);

        // 2、获取全部云桌面最近一个月的平均资源使用率数据
        List<DesktopResourceUsageMonthEntity> monthEntityList = getDesktopResourceUsageMonthEntities(nowDate);

        if (CollectionUtils.isEmpty(dayEntityList) && CollectionUtils.isEmpty(monthEntityList)) {
            return distributionMap;
        }

        distributionMap = buildNormalDistributionMap(dayEntityList, monthEntityList,
            ResourceTypeEnum.CPU);
        distributionMap.putAll(buildNormalDistributionMap(dayEntityList, monthEntityList,
            ResourceTypeEnum.MEMORY));

        return distributionMap;
    }

    private List<DesktopResourceUsageDayEntity> getDesktopResourceUsageDayEntities(LocalDate nowDate) {
        LocalDate oneDayAgo = nowDate.minusDays(1);
        LocalDate sevenDayAgo = nowDate.minusDays(DESKTOP_NORMAL_DISTRIBUTION_DAYS);

        return desktopResourceUsageDayDAO.getDayResourceUsageByStatisticTime(DateUtil.localDateToDate(sevenDayAgo),
            DateUtil.localDateToDate(oneDayAgo));
    }

    private List<DesktopResourceUsageMonthEntity> getDesktopResourceUsageMonthEntities(LocalDate nowDate) {
        LocalDate lastMonthOfFirstDay = nowDate.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastMonthOfLastDay = nowDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

        return desktopResourceUsageMonthDAO.getMonthResourceUsage(DateUtil.localDateToDate(lastMonthOfFirstDay),
            DateUtil.localDateToDate(lastMonthOfLastDay));
    }

    /**
     * 构建正态分布数据模型
     * @param dayEntityList 日资源使用率列表
     * @param monthEntityList 月资源使用率列表
     * @param resourceType 资源类型
     * @return Map<ResourceTypeEnum, DesktopNormalDistributionDTO> 建模数据
     */
    private Map<ResourceTypeEnum, DesktopNormalDistributionDTO> buildNormalDistributionMap(List<DesktopResourceUsageDayEntity> dayEntityList,
        List<DesktopResourceUsageMonthEntity> monthEntityList, ResourceTypeEnum resourceType) {
        // 3、计算各个云桌面最近7天和最近1个月，共8组数据的平均值，返回数据放入Map
        Map<ResourceTypeEnum, List<Double>> averageUsageMap = computeAverageUsage(dayEntityList, monthEntityList, resourceType);

        // 4、根据每个云桌面的平均值进行正态分布建模数据计算
        // 4.1 计算中值
        Double averageValue =
            averageUsageMap.get(resourceType).stream().mapToDouble(o -> o).summaryStatistics().getAverage();

        // 4.2 计算标差的准备数据
        DoubleSummaryStatistics cpuStandardDeviationSummaryStatistics =
            averageUsageMap.get(resourceType).stream().mapToDouble(o -> Math.pow(o - averageValue, 2))
                .summaryStatistics();

        // 4.3 计算标差
        Double standardDeviation = Math.sqrt(cpuStandardDeviationSummaryStatistics.getAverage());
        if (standardDeviation == INVALIDATE_STANDARD_DEVIATION) {
            return Maps.newHashMap();
        }

        // 5、获取全部云桌面最近1天的资源使用率数据
        LocalDate now = LocalDate.now();
        LocalDate oneDayAgo = now.minusDays(1);
        List<DesktopResourceUsageDayEntity> oneDayEntityList =
            desktopResourceUsageDayDAO.getDayResourceUsageByStatisticTime(DateUtil.localDateToDate(oneDayAgo),
                DateUtil.localDateToDate(oneDayAgo));

        // 计算云桌面资源使用率正太分布区间数据
        NormalDistributionDTO normalDistributionDTO = MathUtil.normalDistribution(averageValue, standardDeviation);

        // 6、判断每个云桌面数据在建模数据中的分布情况，做数量统计
        DesktopNormalDistributionDTO dto = computeNormalDistributionNum(oneDayEntityList, normalDistributionDTO,
            resourceType);
        dto.setAverageValue(averageValue);
        dto.setStandardDeviation(standardDeviation);

        Map<ResourceTypeEnum, DesktopNormalDistributionDTO> distributionMap = Maps.newHashMap();
        distributionMap.put(resourceType, dto);

        return distributionMap;
    }

    private Map<ResourceTypeEnum, List<Double>> computeAverageUsage(List<DesktopResourceUsageDayEntity> dayEntityList
        , List<DesktopResourceUsageMonthEntity> monthEntityList, ResourceTypeEnum resourceType) {

        Map<UUID, List<Double>> usageMap = Maps.newHashMap();
        for (DesktopResourceUsageDayEntity dayEntity : dayEntityList) {
            addToUsageMap(usageMap, dayEntity.getDesktopId(), dayEntity.getUsage(resourceType));
        }
        for (DesktopResourceUsageMonthEntity monthEntity : monthEntityList) {
            addToUsageMap(usageMap, monthEntity.getDesktopId(), monthEntity.getUsage(resourceType));
        }

        List<Double> usageAvgList = Lists.newArrayList();
        for (Entry<UUID, List<Double>> entry : usageMap.entrySet()) {
            usageAvgList.add(entry.getValue().stream().collect(Collectors.averagingDouble(Double::valueOf)));
        }
        Map<ResourceTypeEnum, List<Double>> averageUsageMap = Maps.newHashMap();
        averageUsageMap.put(resourceType, usageAvgList);

        return averageUsageMap;
    }

    private void addToUsageMap(Map<UUID, List<Double>> usageMap, UUID deskId, Double usage) {
        if (usage == INVALIDATE_USAGE_VALUE) {
            return;
        }
        if (usageMap.containsKey(deskId)) {
            usageMap.get(deskId).add(usage);
        } else {
            List<Double> tempList = Lists.newArrayList();
            tempList.add(usage);
            usageMap.put(deskId, tempList);
        }
    }

    /**
     * 计算CPU/memory正态分布的区间云桌面数目
     */
    private DesktopNormalDistributionDTO computeNormalDistributionNum(List<DesktopResourceUsageDayEntity> oneDayEntityList,
        NormalDistributionDTO normalDistributionDTO, ResourceTypeEnum resourceType) {

        Long normalNumLeft = oneDayEntityList.stream().filter(o -> normalDistributionDTO.getAverageValue() > o.getUsage(resourceType) &&
            o.getUsage(resourceType) >= normalDistributionDTO.getNormalNumLeftLine()).count();

        Long normalNumRight = oneDayEntityList.stream().filter(o -> normalDistributionDTO.getNormalNumRightLine() > o.getUsage(resourceType)
            && o.getUsage(resourceType) >= normalDistributionDTO.getAverageValue()).count();

        Long lowLoad = oneDayEntityList.stream().filter(o -> normalDistributionDTO.getNormalNumLeftLine() > o.getUsage(resourceType)
            && o.getUsage(resourceType) >= normalDistributionDTO.getLowLoadLine()).count();

        Long highLoad = oneDayEntityList.stream().filter(o -> normalDistributionDTO.getHighLoadLine() > o.getUsage(resourceType)
            && o.getUsage(resourceType) >= normalDistributionDTO.getNormalNumRightLine()).count();

        Long ultraLowLoad = oneDayEntityList.stream().filter(o -> normalDistributionDTO.getLowLoadLine() > o.getUsage(resourceType) &&
            o.getUsage(resourceType) >= normalDistributionDTO.getUltraLowLoadLine()).count();

        Long ultraHighLoad = oneDayEntityList.stream().filter(o -> normalDistributionDTO.getUltraHighLoadLine() > o.getUsage(resourceType)
            && o.getUsage(resourceType) >= normalDistributionDTO.getHighLoadLine()).count();

        Long lowLoadAbnormal = oneDayEntityList.stream().filter(o ->
            normalDistributionDTO.getUltraLowLoadLine() > o.getUsage(resourceType)).count();

        Long highLoadAbnormal = oneDayEntityList.stream().filter(o ->
            o.getUsage(resourceType) >= normalDistributionDTO.getUltraHighLoadLine()).count();

        DesktopNormalDistributionDTO desktopNormalDistributionDTO = new DesktopNormalDistributionDTO();
        desktopNormalDistributionDTO.setNormalNumLeft(normalNumLeft.intValue());
        desktopNormalDistributionDTO.setNormalNumRight(normalNumRight.intValue());
        desktopNormalDistributionDTO.setLowLoad(lowLoad.intValue());
        desktopNormalDistributionDTO.setHighLoad(highLoad.intValue());
        desktopNormalDistributionDTO.setUltraLowLoad(ultraLowLoad.intValue());
        desktopNormalDistributionDTO.setUltraHighLoad(ultraHighLoad.intValue());
        desktopNormalDistributionDTO.setLowLoadAbnormal(lowLoadAbnormal.intValue());
        desktopNormalDistributionDTO.setHighLoadAbnormal(highLoadAbnormal.intValue());

        return desktopNormalDistributionDTO;
    }

}
