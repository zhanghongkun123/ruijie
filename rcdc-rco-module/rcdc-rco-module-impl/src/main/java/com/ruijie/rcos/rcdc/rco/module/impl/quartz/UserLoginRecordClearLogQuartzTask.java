package com.ruijie.rcos.rcdc.rco.module.impl.quartz;


import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Description: 定时清理用户报表记录计时器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/26 10:07
 *
 * @author LeiDi
 */
@Service
@Quartz(cron = "0 0 0 * * ? *", scheduleTypeCode = "clear_user_login_record_log", scheduleName =
        BusinessKey.RCDC_QUARTZ_CLEAR_USER_LOGIN_RECORD_LOG)
public class UserLoginRecordClearLogQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginRecordClearLogQuartzTask.class);

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");

        LOGGER.info("定时清理用户报表记录");

        // 获取当前时间，并前推6个月
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime specifiedLocalDateTime = now.minusDays(180);

        Date specifiedDate = Date.from(specifiedLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        userLoginRecordService.clear(specifiedDate);
    }
}
