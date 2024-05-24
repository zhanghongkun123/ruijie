package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopLicenseStatAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Description: 授权使用情况采集定时任务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/06/14
 *
 * @author zjy
 */
@Service
@Quartz(scheduleTypeCode = ScheduleTypeCodeConstants.DESKTOP_LICENSE_USED_INFO_STAT_CLEAN, cron = "0 20 5 * * ? ",
        scheduleName = BusinessKey.RCDC_RCO_QUARTZ_DESKTOP_LICENSE_USED_INFO_STAT_CLEAN)
public class DesktopLicenseUsedInfoStatCleanQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopLicenseUsedInfoStatCleanQuartzTask.class);

    private static final String DESKTOP_LICENSE_STAT_RETAIN_DAY = "desktop_license_stat_retain_day";

    private static final String DEFAULT_DESKTOP_LICENSE_STAT_RETAIN_DAY = "365";

    private static final long ONE_DAY = 24 * 60 * 60 * 1000L;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private DesktopLicenseStatAPI desktopLicenseStatAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        LOGGER.info("[云桌面过期授权记录自动清理]：任务开始");

        String overdueDay = globalParameterAPI.findParameter(DESKTOP_LICENSE_STAT_RETAIN_DAY);
        if (StringUtils.isEmpty(overdueDay)) {
            overdueDay = DEFAULT_DESKTOP_LICENSE_STAT_RETAIN_DAY;
        }
        Date overdueTime = new Date(System.currentTimeMillis() - Integer.parseInt(overdueDay) * ONE_DAY);
        LOGGER.info("过期数据的日期为：{}", overdueTime);
        Integer count = desktopLicenseStatAPI.deleteOverdueStat(overdueTime);
        LOGGER.info("[云桌面过期授权记录自动清理]；任务结束，共清理数据数量：{}", count);
    }
}
