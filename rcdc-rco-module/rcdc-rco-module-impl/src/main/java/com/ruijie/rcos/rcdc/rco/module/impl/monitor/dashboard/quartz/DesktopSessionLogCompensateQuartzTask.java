package com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.quartz;

import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.CompensateSessionType;
import com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.service.DesktopPoolDashboardService;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: 每10分钟查询会话记录表中在线的记录，查询这个会话是否真的结束
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/10 19:47
 *
 * @author yxq
 */
@Service
@Quartz(cron = "0 0/10 * * * ? *", scheduleTypeCode = "compensate_desktop_session_log_code", scheduleName =
        BusinessKey.RCDC_RCO_QUARTZ_COMPENSATE_DESKTOP_SESSION_LOG)
public class DesktopSessionLogCompensateQuartzTask implements QuartzTask {

    @Autowired
    private DesktopPoolDashboardService desktopPoolDashboardService;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext must not be null");

        // 补偿会话结束时间
        desktopPoolDashboardService.compensateFishedSessionLog(CompensateSessionType.CRON_TASK);

        // 补偿桌面断开连接时间
        userDesktopMgmtAPI.compensateConnectClosedTime(CompensateSessionType.CRON_TASK);
    }
}
