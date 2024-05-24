package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.DeskopOpLogPageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDesktopOpLogDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.PlatformAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopStartCountDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopStartCountDayEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.util.QueryUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 每日开机数统计定时任务，每隔10分钟执行一次
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/25
 * 
 * //@GlobalUniqueBean("desktopStartCountStatisticQuartz")
 * //@Quartz(scheduleTypeCode = "rco_desk_start_count_statistic", scheduleName =
 * BusinessKey.RCDC_RCO_BIGSCREEN_QUARTZ_DESKTOP_START_COUNT_STATISTIC,
 * // cron = "0 10 0 * * ? *")
 *
 * @author zhangyichi
 */

public class DesktopStartCountStatisticQuartzTask implements QuartzTask {

    private static final String START_EVERT_NAME = "START";

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopStartCountStatisticQuartzTask.class);

    @Autowired
    private CbbDesktopOpLogMgmtAPI cbbUserDesktopOpLogMgmtAPI;

    @Autowired
    private DesktopStartCountDayDAO desktopStartCountDayDAO;

    @Autowired
    private PlatformAPI platformAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws BusinessException {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        LOGGER.info("每天开机数统计定时任务开始======");
        List<LocalDate> dateList = check();
        if (dateList.isEmpty()) {
            return;
        }

        // 如果List集合不足7条数据，则说明存在数据遗漏，需要重新获取
        for (LocalDate localDate : dateList) {
            LocalDateTime startTime = localDate.atStartOfDay();
            LocalDateTime endTime = startTime.with(LocalTime.MAX);
            save(startTime, endTime);
        }
        LOGGER.info("每天开机数统计定时任务结束======");
    }

    private List<LocalDate> check() throws BusinessException {
        // 获取需要补偿的最大天数
        int retryDays = getRetryDays();
        if (retryDays == 0) {
            return Lists.newArrayList();
        }
        LocalDate now = LocalDate.now();
        LocalDate startTime = now.minusDays(retryDays);
        LocalDate endTime = now.minusDays(1);
        List<DesktopStartCountDayEntity> entityList =
                desktopStartCountDayDAO.listStartCount(DateUtil.localDateToDate(startTime), DateUtil.localDateToDate(endTime));
        List<LocalDate> deskTopTimeList = entityList.stream().map(o -> DateUtil.dateToLocalDate(o.getStatisticTime())).collect(Collectors.toList());

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

    /**
     * 获取指定日期内云桌面开机数，并保存
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @throws BusinessException 业务异常
     */
    private void save(LocalDateTime startTime, LocalDateTime endTime) throws BusinessException {
        // 分页获取全部云桌面操作日志
        int limit = Constants.API_PAGE_QUERY_LIMIT;
        DeskopOpLogPageRequest apiPageRequest = new DeskopOpLogPageRequest(new PageWebRequest());
        apiPageRequest.setLimit(limit);
        apiPageRequest.setStartTime(DateUtil.localDateTimeToDate(startTime));
        apiPageRequest.setEndTime(DateUtil.localDateTimeToDate(endTime));
        QueryUtil<CbbDesktopOpLogDetailDTO, DeskopOpLogPageRequest> desktopOpLogQuery = new QueryUtil<>();
        LOGGER.debug("从CbbUserDesktopOpLogMgmtAPI获取云桌面操作日志");
        List<CbbDesktopOpLogDetailDTO> logDtoList =
                desktopOpLogQuery.findAllByPageQuery(apiPageRequest, cbbUserDesktopOpLogMgmtAPI::pageQuery, dto -> true, dtoList ->
                    dtoList.size() == limit
                );

        Set<UUID> statisticSet =
                logDtoList.stream().filter(o -> START_EVERT_NAME.equals(o.getEventName())).map(o -> o.getDesktopId()).collect(Collectors.toSet());
        DesktopStartCountDayEntity entity = new DesktopStartCountDayEntity();
        entity.setStartCount(statisticSet.size());
        entity.setStatisticTime(DateUtil.localDateTimeToDate(endTime));
        entity.setCreateTime(new Date());
        desktopStartCountDayDAO.save(entity);
    }
}
