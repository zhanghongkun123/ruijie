package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbGetDeskVDIPageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.PlatformAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopUsageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageDayEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.util.QueryUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 桌面资源利用率（日）定时任务，每隔10分钟执行一次
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 14:48
 * 
 * @Service
 * @Quartz(scheduleTypeCode = "rco_desk_resource_usage_day_statistic",
 *                          scheduleName = BusinessKey.RCDC_RCO_BIGSCREEN_QUARTZ_DESKTOP_RESOURCE_USAGE_DAY_STATISTIC,
 *                          cron = "0 10 0 * * ? *")
 *
 * @author zhangyichi
 */
public class DesktopResourceUsageDayStatisticQuartzTask implements QuartzTask {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DesktopResourceUsageDayStatisticQuartzTask.class);

    @Autowired
    CbbVDIDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    DesktopResourceUsageDayDAO desktopResourceUsageDayDAO;

    @Autowired
    DesktopService desktopService;

    @Autowired
    PlatformAPI platformAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws BusinessException {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        LOGGER.info("云桌面资源使用率日统计定时任务开始====");
        List<LocalDate> dateList = check();
        if (dateList.isEmpty()) {
            return;
        }
        // 一周内有缺失数据需要重新获取
        LOGGER.info("需要统计云桌面资源利用率的日期：dateList={}", dateList);
        for (LocalDate localDate : dateList) {
            // 获取某天的云桌面资源使用率并进行统计
            save(localDate);
        }
        LOGGER.info("云桌面资源使用率日统计定时任务结束====");
    }

    private List<LocalDate> check() throws BusinessException {
        // 获取最大补偿天数
        int retryDays = getRetryDays();
        if (retryDays == 0) {
            return Lists.newArrayList();
        }
        LocalDate now = LocalDate.now();
        LocalDate startTime = now.minusDays(retryDays);
        LocalDate endTime = now.minusDays(1);
        List<Date> entityList =
                desktopResourceUsageDayDAO.queryStatisticTimeList(DateUtil.localDateToDate(startTime), DateUtil.localDateToDate(endTime));

        List<LocalDate> timeList = entityList.stream().map(DateUtil::dateToLocalDate).collect(Collectors.toList());

        List<LocalDate> retryDateList = Lists.newArrayList();
        for (int i = 1; i <= retryDays; i++) {
            retryDateList.add(now.minusDays(i));
        }

        return retryDateList.stream().filter(o -> !timeList.contains(o)).collect(Collectors.toList());
    }

    private int getRetryDays() throws BusinessException {
        long systemWorkTime = platformAPI.getSystemTime().getSystemWorkTime();
        return Math.min(Constants.DEFAULT_RETRY_DAYS, DateUtil.computeDayInterval(systemWorkTime));
    }

    private void save(LocalDate date) throws BusinessException {
        int pageSize = Constants.API_PAGE_QUERY_LIMIT;
        CbbGetDeskVDIPageRequest apiPageRequest = new CbbGetDeskVDIPageRequest();
        apiPageRequest.setLimit(pageSize);

        QueryUtil<CbbDeskDTO, CbbGetDeskVDIPageRequest> desktopQuery = new QueryUtil<>();
        List<CbbDeskDTO> deskDTOList =
                desktopQuery.findAllByPageQuery(apiPageRequest, cbbDeskMgmtAPI::pageQueryDeskVDI, dto -> true, dtoList -> dtoList.size() == pageSize);
        if (CollectionUtils.isEmpty(deskDTOList)) {
            // 无云桌面，无需统计
            return;
        }
        List<DesktopResourceUsageDayEntity> entityList = Lists.newArrayList();
        entityList.addAll(getUsageEntityList(date, deskDTOList));

        desktopResourceUsageDayDAO.saveAll(entityList);
    }

    private List<DesktopResourceUsageDayEntity> getUsageEntityList(LocalDate date, List<CbbDeskDTO> cbbDeskDtoList) throws BusinessException {
        List<DesktopResourceUsageDayEntity> resultList = Lists.newArrayList();
        LocalDateTime startTime = date.atStartOfDay();
        LocalDateTime endTime = startTime.with(LocalTime.MAX);

        UUID[] vmIdArr = new UUID[cbbDeskDtoList.size()];
        for (int i = 0; i < cbbDeskDtoList.size(); i++) {
            vmIdArr[i] = cbbDeskDtoList.get(i).getDeskId();
        }

        List<DesktopUsageDTO> dtoList =
                desktopService.listDesktopUsageById(vmIdArr, DateUtil.localDateTimeToDate(startTime), DateUtil.localDateTimeToDate(endTime));

        for (DesktopUsageDTO dto : dtoList) {
            if (dto.getCpuUsage() > 0 || dto.getMemoryUsage() > 0 || dto.getDiskUsage() > 0) {
                DesktopResourceUsageDayEntity entity = new DesktopResourceUsageDayEntity();
                BeanUtils.copyProperties(dto, entity);
                entity.setStatisticTime(DateUtil.localDateToDate(date));
                entity.setCreateTime(new Date());
                resultList.add(entity);
            }
        }

        return resultList;
    }
}
