package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.ruijie.rcos.rcdc.rco.module.def.api.ExportAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ExportUserExcelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: 对用户导出数据定时每天晚上2点进行清理
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 20:03 2023/3/16
 *
 * @author liusd
 */
@Service
@Quartz(cron = "0 20 3 * * ?", scheduleTypeCode = ScheduleTypeCodeConstants.RCDC_RCO_EXPORT_DATA_CLEAN,
        scheduleName = BusinessKey.RCDC_RCO_EXPORT_DATA_CLEAN)
public class ExportDataCleanQuartzTask implements QuartzTask {

    @Autowired
    private ExportUserExcelAPI exportUserExcelAPI;

    @Autowired
    private ExportAPI exportAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        this.exportUserExcelAPI.deleteOldFile();
        exportAPI.clearOldFile();
    }
}
