package com.ruijie.rcos.rcdc.rco.module.impl.datasync.quartz;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.impl.datasync.DataSyncBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.constant.DataSyncConstant;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.entity.DataSyncLogEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.service.DataSyncLogService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/25 16:34
 *
 * @author coderLee23
 */
@Service
@Quartz(cron = "0 0 0 * * ? *", scheduleTypeCode = "clear_data_sync_log", scheduleName = DataSyncBusinessKey.RCDC_QUARTZ_CLEAR_DATA_SYNC_LOG)
public class DataSyncLogQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSyncLogQuartzTask.class);

    /**
     * 限制日志不能超过150w条记录，超过部分清楚
     */
    private static final Long LOG_LIMIT_TOTAL = 1500000L;

    @Autowired
    private DataSyncLogService dataSyncLogService;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        clearOverdueLog();
    }

    private void clearOverdueLog() {
        LOGGER.info("定时清理用户和用户组同步记录");
        // 获取当前时间，并前推1个月
        LocalDateTime now = LocalDateTime.now();
        String overdueDay = globalParameterAPI.findParameter(DataSyncConstant.DESK_OP_LOG_RETAIN_DAY);
        Assert.notNull(overdueDay, "overdueDay must not be null");

        LocalDateTime specifiedLocalDateTime = now.minusDays(Integer.parseInt(overdueDay));

        Date specifiedDate = Date.from(specifiedLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        dataSyncLogService.deleteByExpireTime(specifiedDate);

        Long count = dataSyncLogService.count();
        if (count > LOG_LIMIT_TOTAL) {
            LOGGER.info("日志总数超过{}条，删除超过的记录！", count);
            DataSyncLogEntity dataSyncLogEntity = dataSyncLogService.findOneByOffset(LOG_LIMIT_TOTAL);
            dataSyncLogService.deleteByExpireTime(dataSyncLogEntity.getCreateTime());
        }

    }

}
