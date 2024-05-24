package com.ruijie.rcos.rcdc.rco.module.impl.sms.quartz;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.sms.SmsBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.sms.service.ExternalMessageLogService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * Description: 外部消息记录定时自动清理
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author TD
 */
@Service
@Quartz(cron = "0 30 2 * * ?", scheduleTypeCode = ScheduleTypeCodeConstants.EXTERNAL_MESSAGE_LOG_CLEAN, 
        scheduleName = SmsBusinessKey.RCDC_RCO_QUARTZ_MESSAGE_NOTICE_LOG_CLEAN)
public class ExternalMessageLogCleanQuartzTask implements QuartzTask {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalMessageLogCleanQuartzTask.class);

    /**
     * t_sk_global_parameter表存放云桌面日志保留天数键值
     * */
    private static final String DESK_OP_LOG_RETAIN_DAY = "desk_op_log_retain_day";

    public static final long ONE_DAY = 24 * 60 * 60 * 1000L;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private ExternalMessageLogService externalMessageLogService;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        LOGGER.info("[外部消息记录自动清理]：任务开始");

        String overdueDay = globalParameterAPI.findParameter(DESK_OP_LOG_RETAIN_DAY);
        Assert.notNull(overdueDay, "overdueDay can not be empty");
        Date overdueTime = new Date(System.currentTimeMillis() - Integer.parseInt(overdueDay) * ONE_DAY);

        Integer overdueLogNumber = externalMessageLogService.countBySendTimeLessThan(overdueTime);
        if (overdueLogNumber > 0) {
            LOGGER.info("move overdue external message logs, number is [{}]", overdueLogNumber);
            long beginTime = System.currentTimeMillis();
            externalMessageLogService.deleteBySendTimeLessThan(overdueTime);
            createSystemLogHandle(beginTime, overdueLogNumber);
        }

        LOGGER.info("[外部消息记录自动清理]；任务结束");
    }

    private void createSystemLogHandle(long beginTime, int cleanDeskCount) {
        if (cleanDeskCount <= 0) {
            return;
        }
        long useTime = System.currentTimeMillis() - beginTime;
        auditLogAPI.recordLog(SmsBusinessKey.RCDC_RCO_QUARTZ_EXTERNAL_MESSAGE_LOG_CLEAN_FINISH, 
                String.valueOf(cleanDeskCount), String.valueOf(useTime));
    }
}
