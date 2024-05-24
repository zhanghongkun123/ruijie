package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskLicenseMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.LicenseGlobal;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: 每10s同步windows系统已激活的云桌面数量
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 20:03 2020/5/15
 *
 * @author yxd
 */
@Service
@Quartz(cron = "0/10 * * * * ?", scheduleTypeCode = "cloud_desk_obtain_active_desk_count",
        scheduleName = BusinessKey.RCDC_CLOUDDESKTOP_ACTIVE_DESKTOP_COUNT_SYNC)
public class ObtainActiveDesktopCountQuartzTask implements QuartzTask {

    @Autowired
    private CbbDeskLicenseMgmtAPI cbbDeskLicenseMgmtAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");

        int activeDeskNum = cbbDeskLicenseMgmtAPI.acquireWindowsActiveDeskNum().getActiveDeskNum();
        LicenseGlobal.setWindowsActiveNum(activeDeskNum);
    }
}
