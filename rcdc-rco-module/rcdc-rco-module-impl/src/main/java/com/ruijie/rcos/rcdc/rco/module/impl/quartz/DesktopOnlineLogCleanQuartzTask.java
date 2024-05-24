package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.request.systemlog.BaseCreateSystemLogRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoploginlog.service.DesktopOnlineLogService;
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
 * Description: 用户登入登出日志自动清理
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/07/27
 * @author linke
 */
@Service
@Quartz(scheduleTypeCode = ScheduleTypeCodeConstants.DESKTOP_ONLINE_LOG_CLEAN, scheduleName = BusinessKey.RCDC_RCO_QUARTZ_DESKTOP_ONLINE_LOG_CLEAN,
        cron = "0 30 3 * * ?")
public class DesktopOnlineLogCleanQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopOnlineLogCleanQuartzTask.class);

    /**
     * t_sk_global_parameter表存放云桌面日志保留天数键值
     * */
    private static final String DESK_OP_LOG_RETAIN_DAY = "desk_op_log_retain_day";

    public static final long ONE_DAY = 24 * 60 * 60 * 1000L;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private DesktopOnlineLogService desktopOnlineLogService;

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        LOGGER.debug("[用户登入登出日志自动清理]：任务开始");

        String overdueDay = globalParameterAPI.findParameter(DESK_OP_LOG_RETAIN_DAY);
        Date overdueTime = new Date(System.currentTimeMillis() - Integer.parseInt(overdueDay) * ONE_DAY);

        Integer overdueLogNumber = desktopOnlineLogService.countByOperationTimeLessThan(overdueTime);
        if (overdueLogNumber > 0) {
            LOGGER.info("move overdue desktop online logs, number is [{}]", overdueLogNumber);
            long beginTime = System.currentTimeMillis();
            desktopOnlineLogService.deleteByOperationTimeLessThan(overdueTime);
            createSystemLogHandle(beginTime, overdueLogNumber);
        }

        LOGGER.debug("[用户登入登出日志自动清理]；任务结束");
    }

    private void createSystemLogHandle(long beginTime, int cleanDeskCount) {
        if (cleanDeskCount <= 0) {
            return;
        }
        long useTime = System.currentTimeMillis() - beginTime;
        BaseCreateSystemLogRequest request = new BaseCreateSystemLogRequest(BusinessKey.RCDC_RCO_QUARTZ_DESKTOP_ONLINE_LOG_CLEAN_FINISH,
                new String[] {String.valueOf(cleanDeskCount), String.valueOf(useTime)});
        baseSystemLogMgmtAPI.createSystemLog(request);
    }
}
