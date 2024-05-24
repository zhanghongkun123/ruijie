package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import static com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_CLIENT_QUARTZ_CLEAR_CLIENT_OPT_LOG;
import static com.ruijie.rcos.rcdc.rco.module.impl.Constants.CLEAR_CLIENT_OP_LOG_INTERVAL;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.rcdc.rco.module.def.api.ClientOpLogAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;

/**
 * Description: rca-client操作日志定时清理
 * Copyright: Copyright (c) 2022
 * Company: RuiJie Co., Ltd.
 * Create Time: 2022/4/11 11:09 上午
 *
 * @author zhouhuan
 */
@Service
@Quartz(cron = "0 0 0 * * ? *", scheduleTypeCode = "clear_rco_client_opt_log", scheduleName =
        RCDC_RCO_CLIENT_QUARTZ_CLEAR_CLIENT_OPT_LOG)
public class ClearClientOptLogQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClearClientOptLogQuartzTask.class);

    private final int defaultInterval = 6;

    @Autowired
    private ClientOpLogAPI clientOpLogAPI;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");

        LOGGER.info("定时清理客户端操作日志");

        // 获取当前时间，并前推6个月
        LocalDateTime now = LocalDateTime.now();
        String interval = globalParameterService.findParameter(CLEAR_CLIENT_OP_LOG_INTERVAL);
        LocalDateTime specifiedLocalDateTime = now.minusMonths(StringUtils.isEmpty(interval) ? defaultInterval : Integer.parseInt(interval));

        Date specifiedDate = Date.from(specifiedLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        clientOpLogAPI.clear(specifiedDate);
    }

}
