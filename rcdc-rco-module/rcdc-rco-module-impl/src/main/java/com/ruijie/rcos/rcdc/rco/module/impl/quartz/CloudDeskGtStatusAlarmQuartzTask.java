package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskGtMgmtAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopGuestToolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月17日
 *
 * @author linrenjian
 */
@Service
@Quartz(scheduleTypeCode = ScheduleTypeCodeConstants.DESKTOP_GUEST_TOOL_CHECK, cron = "0 0 /1 * * ? *",
        scheduleName = BusinessKey.RCDC_RCO_QUARTZ_DESKTOP_GUEST_TOOL_CHECK)
public class CloudDeskGtStatusAlarmQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDeskGtStatusAlarmQuartzTask.class);

    @Autowired
    private DesktopGuestToolMgmtAPI desktopGuestToolMgmtAPI;

    @Autowired
    private CbbVDIDeskGtMgmtAPI cbbVDIDeskGtMgmtAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");

        // 开启GT告警 则进行告警
        if (cbbVDIDeskGtMgmtAPI.getGtHeartBeatConfig().getEnableGtHeartBeat()) {
            // 清理告警 并且产生告警
            desktopGuestToolMgmtAPI.releaseAlarmAndCreateAlarm();
        }

    }

}
