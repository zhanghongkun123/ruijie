package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalOnlineTimeRecordService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: 终端在线总时长定时器
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/03/8
 * 0 0/1 * * * ?
 * @author xiao'yong'deng
 */
@Service
@Quartz(scheduleTypeCode = ScheduleTypeCodeConstants.TERMINAL_ONLINE_TIME_RECORD, scheduleName = BusinessKey.TERMINAL_ONLINE_TIME_RECORD_TASK,
        cron = "0 0/30 * * * ?")
public class TerminalOnlineTimeRecordQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalOnlineTimeRecordQuartzTask.class);

    private static final boolean LOCK = true;

    @Autowired
    private TerminalOnlineTimeRecordService onlineTimeRecordService;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        LOGGER.warn("[终端在线总时长定时器]：任务开始");
        try {
            onlineTimeRecordService.handleTerminalOnlineTimeByQuartz();
        } catch (Exception e) {
            LOGGER.error("[终端在线总时长定时器]：失败", e);
            return;
        }

        LOGGER.warn("[终端在线总时长定时器]；任务结束");
    }



}
