package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopLicenseStatAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


/**
 * Description: 授权使用情况采集定时任务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/06/14
 *
 * @author zjy
 */
@Service
@Quartz(scheduleTypeCode = ScheduleTypeCodeConstants.DESKTOP_LICENSE_USED_INFO_STAT, cron = "0 * * * * ? ",
        scheduleName = BusinessKey.RCDC_RCO_QUARTZ_DESKTOP_LICENSE_USED_INFO_STAT)
public class DesktopLicenseUsedInfoStatQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopLicenseUsedInfoStatQuartzTask.class);

    @Autowired
    private DesktopLicenseStatAPI desktopLicenseStatAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        LOGGER.debug("[定时记录云桌面授权使用情况]：任务开始");

        desktopLicenseStatAPI.saveCurrentDesktopLicense();

        LOGGER.debug("[定时记录云桌面授权使用情况]：任务结束");
    }
}
