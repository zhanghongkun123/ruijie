package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.request.systemlog.BaseCreateSystemLogRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.RecycleBinConfigStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.global.RecycleBinGlobal;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RecycleBinService;
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
import java.util.List;

/**
 * Description: clear overdue recycleBin desktop
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月8日
 *
 * @author artom
 */
@Service
@Quartz(scheduleTypeCode = "user_desk_clean", scheduleName = BusinessKey.RCDC_USER_QUARTZ_DESK_CLEAN, cron = "0 0 3 * * ?")
public class DeskCleanQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskCleanQuartzTask.class);

    /**
     * 一天的时间，ms
     */
    private static final long MS_IN_ONE_DAY = 24 * 60 * 60 * 1000L;

    /**
     * 一个月的天数
     */
    private static final int DAY_IN_ONE_MONTH = 30;

    @Autowired
    private RecycleBinService recycleBinService;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        cleanRecycleBinService();
    }

    /**
     * * 定时清空回收站
     */
    private void cleanRecycleBinService() {
        List<ViewUserDesktopEntity> entityList = viewDesktopDetailDAO.findByIsDelete(true);
        long beginTime = System.currentTimeMillis();
        int cleanDeskCount = 0;
        for (ViewUserDesktopEntity entity : entityList) {
            try {
                if (isOverdue(entity)) {
                    recycleBinService.deleteDeskCompletely(entity.getCbbDesktopId());
                    cleanDeskCount++;
                }
            } catch (Exception e) {
                LOGGER.error("定时删除回收站云桌面失败，云桌面id[{}]，云桌面名称[{}]", entity.getCbbDesktopId(), entity.getDesktopName(), e);
            }
        }
        createSystemLogHandle(beginTime, cleanDeskCount);
    }

    private boolean isOverdue(ViewUserDesktopEntity entity) {
        Date date = entity.getDeleteTime();
        if (date != null) {
            long overdueDate = (System.currentTimeMillis() - date.getTime()) / MS_IN_ONE_DAY;
            String enable = globalParameterAPI.findParameter(RecycleBinGlobal.RECYCLE_BIN_STATE);
            String cycle = globalParameterAPI.findParameter(RecycleBinGlobal.RECYCLE_BIN_CYCLE);
            if ((RecycleBinConfigStateEnum.valueOf(enable) == RecycleBinConfigStateEnum.OPEN)
                    && overdueDate >= (Integer.valueOf(cycle) * DAY_IN_ONE_MONTH)) {
                return true;
            }
        }

        return false;
    }

    private void createSystemLogHandle(long beginTime, int cleanDeskCount) {
        if (cleanDeskCount > 0) {
            long useTime = System.currentTimeMillis() - beginTime;
            BaseCreateSystemLogRequest request = new BaseCreateSystemLogRequest(BusinessKey.RCDC_USER_QUARTZ_DESK_CLEAN,
                    new String[]{String.valueOf(cleanDeskCount), String.valueOf(useTime)});
            baseSystemLogMgmtAPI.createSystemLog(request);
        }
    }
}
