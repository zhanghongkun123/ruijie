package com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.quartz;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.StatisticDesktopPoolDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.DesktopStatisticsTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.RelateTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopSessionLogHourRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopSessionLogHourRecordEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.service.DesktopPoolDashboardService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/20 15:41
 *
 * @author yxq
 */
@Service
@Quartz(cron = "0 0/2 * * * ? *", scheduleTypeCode = "statistic_desk_use_rate_dashboard_data", scheduleName =
        BusinessKey.RCDC_RCO_QUARTZ_STATISTIC_DESK_USE_RATE_DASHBOARD_DATA)
public class StatisticDesktopUseRateQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticDesktopUseRateQuartzTask.class);

    /**
     * 将使用率转换为百分比的形式
     */
    private static final double CONVERT_USED_RATE_TO_PERCENTAGE = 100D;

    @Autowired
    private DesktopPoolDashboardService desktopPoolDashboardService;

    @Autowired
    private DesktopSessionLogHourRecordDAO desktopSessionLogHourRecordDAO;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext must not be null");
        LOGGER.debug("执行定时统计桌面池桌面使用率报表信息");

        // 获取相关key
        Date currentDate = new Date();
        String hourKey = DateUtil.getStatisticHourKey(currentDate);

        // 当前所有桌面池里面桌面使用情况
        List<StatisticDesktopPoolDesktopCountDTO> currentDeskPoolUseDTOList = desktopPoolDashboardService.getAllDesktopPoolUseInfo();
        // 当前每个桌面池使用率
        Map<UUID, StatisticDesktopPoolDesktopCountDTO> currentIdMap = currentDeskPoolUseDTOList.stream().collect(Collectors.toMap(
                StatisticDesktopPoolDesktopCountDTO::getDesktopPoolId, Function.identity()));

        // 查询dayKey对应小时的使用信息
        List<DesktopSessionLogHourRecordEntity> entityList = desktopSessionLogHourRecordDAO.findByHourKeyAndTypeAndRelatedIdIn(
                hourKey, DesktopStatisticsTypeEnum.SINGLE_USED_RATE, new ArrayList<>(currentIdMap.keySet()));

        // 数据库里面哪些桌面池已经有了信息
        Map<UUID, DesktopSessionLogHourRecordEntity> dbDesktopPoolIdEntityMap = entityList.stream().collect(Collectors.toMap(
                DesktopSessionLogHourRecordEntity::getRelatedId, Function.identity()));

        // 最后需要入库的列表
        List<DesktopSessionLogHourRecordEntity> resultEntityList = new ArrayList<>();

        currentIdMap.forEach((deskPoolId, value) -> {
            // 转换成使用率
            long canUseCount = (long)value.getTotalCount() - value.getErrorCount();
            // 如果分母为0，则认为使用率为0
            double useRate = canUseCount == 0 ? 0 : CONVERT_USED_RATE_TO_PERCENTAGE * value.getUsedCount() / (canUseCount);
            // 如果数据库里面已经有了值，那么更新使用率为较大的值
            if (dbDesktopPoolIdEntityMap.containsKey(deskPoolId)) {
                DesktopSessionLogHourRecordEntity entity = dbDesktopPoolIdEntityMap.get(deskPoolId);
                entity.setCount(Math.max(entity.getCount(), useRate));
                resultEntityList.add(entity);
            } else {
                // 如果数据库里面没有值，则插入新的值
                DesktopSessionLogHourRecordEntity entity = new DesktopSessionLogHourRecordEntity();
                entity.setCount(useRate);
                entity.setRelatedId(deskPoolId);
                entity.setRelatedType(RelateTypeEnum.DESKTOP_POOL);
                entity.setType(DesktopStatisticsTypeEnum.SINGLE_USED_RATE);
                resultEntityList.add(entity);
            }
        });
        // 入库
        desktopSessionLogHourRecordDAO.saveAll(resultEntityList);

        // 统计全部VDI静态池
        statisticByDesktopPoolType(currentDeskPoolUseDTOList, hourKey, DesktopPoolType.STATIC, CbbDesktopPoolType.VDI,
                DesktopStatisticsTypeEnum.TOTAL_VDI_STATIC_USED_RATE);
        // 统计全部VDI动态池
        statisticByDesktopPoolType(currentDeskPoolUseDTOList, hourKey, DesktopPoolType.DYNAMIC, CbbDesktopPoolType.VDI,
                DesktopStatisticsTypeEnum.TOTAL_VDI_DYNAMIC_USED_RATE);

        // 统计全部第三方静态池
        statisticByDesktopPoolType(currentDeskPoolUseDTOList, hourKey, DesktopPoolType.STATIC, CbbDesktopPoolType.THIRD,
                DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_STATIC_USED_RATE);
        // 统计全部第三方动态池
        statisticByDesktopPoolType(currentDeskPoolUseDTOList, hourKey, DesktopPoolType.STATIC, CbbDesktopPoolType.THIRD,
                DesktopStatisticsTypeEnum.TOTAL_THIRD_PARTY_DYNAMIC_USED_RATE);

        // 统计全部桌面池加起来的总和信息
        statisticTotalDesktopPool(hourKey);
    }

    private void statisticByDesktopPoolType(List<StatisticDesktopPoolDesktopCountDTO> currentDeskPoolUseDTOList, String hourKey,
                                            DesktopPoolType poolType, CbbDesktopPoolType cbbDesktopPoolType,
                                            DesktopStatisticsTypeEnum statisticType) {
        List<StatisticDesktopPoolDesktopCountDTO> statisticDTOList = currentDeskPoolUseDTOList.stream()
                .filter(item -> poolType == item.getDesktopPoolType() && cbbDesktopPoolType == item.getDesktopSourceType())
                .collect(Collectors.toList());

        List<DesktopSessionLogHourRecordEntity> entityList = desktopSessionLogHourRecordDAO.findByHourKeyAndType(hourKey, statisticType);
        DesktopSessionLogHourRecordEntity entity;
        if (CollectionUtils.isEmpty(entityList)) {
            entity = new DesktopSessionLogHourRecordEntity();
            entity.setCount(0D);
            entity.setRelatedType(RelateTypeEnum.DESKTOP_POOL);
            entity.setType(statisticType);
        } else {
            entity = entityList.get(0);
        }

        int canUseTotal = 0;
        int used = 0;
        for (StatisticDesktopPoolDesktopCountDTO countDTO : statisticDTOList) {
            canUseTotal = canUseTotal + countDTO.getTotalCount() - countDTO.getErrorCount();
            used = used + countDTO.getUsedCount();
        }
        double useRate = canUseTotal == 0 ? 0 : CONVERT_USED_RATE_TO_PERCENTAGE * used / (canUseTotal);
        entity.setCount(Math.max(entity.getCount(), useRate));
        desktopSessionLogHourRecordDAO.save(entity);
    }

    private void statisticTotalDesktopPool(String hourKey) {
        // 统计全部桌面池加起来的总和信息
        StatisticDesktopPoolDesktopCountDTO totalDesktopCountInfo = desktopPoolDashboardService.getCurrentAllDesktopPoolInfo();
        // 如果数据库中没有数据，则不需要统计
        if (totalDesktopCountInfo == null) {
            return;
        }
        long totalCanUseCount = (long)totalDesktopCountInfo.getTotalCount() - totalDesktopCountInfo.getErrorCount();
        // 如果分母为0，则认为使用率为0
        double totalUsedRate = totalCanUseCount == 0 ? 0 : CONVERT_USED_RATE_TO_PERCENTAGE * totalDesktopCountInfo.getUsedCount() / totalCanUseCount;
        DesktopSessionLogHourRecordEntity totalDeskEntity = desktopSessionLogHourRecordDAO.findFirstByHourKeyAndType(hourKey,
                DesktopStatisticsTypeEnum.TOTAL_USED_RATE);
        if (totalDeskEntity == null) {
            totalDeskEntity = new DesktopSessionLogHourRecordEntity();
            totalDeskEntity.setCount(totalUsedRate);
            totalDeskEntity.setRelatedType(RelateTypeEnum.DESKTOP_POOL);
            totalDeskEntity.setType(DesktopStatisticsTypeEnum.TOTAL_USED_RATE);
        } else {
            totalDeskEntity.setCount(Math.max(totalUsedRate, totalDeskEntity.getCount()));
        }
        desktopSessionLogHourRecordDAO.save(totalDeskEntity);
    }

}
