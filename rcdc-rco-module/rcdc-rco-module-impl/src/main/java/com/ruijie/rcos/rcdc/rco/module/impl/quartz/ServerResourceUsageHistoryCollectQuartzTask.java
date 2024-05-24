package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;

/**
 * 
 * Description: 服务器资源使用情况收集，每5分钟执行
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月26日
 * 
 * @Service
 *          // @Quartz(scheduleTypeCode = "rco_server_resource_usage_history_collect",
 *          // scheduleName = BusinessKey.RCDC_RCO_BIGSCREEN_QUARTZ_SERVER_RESOURCE_USAGE_HISTORY_COLLECT, cron = "0 0/5
 *          * * * ? ")
 * 
 * @author wanmulin
 */
public class ServerResourceUsageHistoryCollectQuartzTask extends AbstractServerResourceUsageHistoryCollect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerResourceUsageHistoryCollectQuartzTask.class);

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        List<PhysicalServerDTO> serverList = listAllPhysicalServer();
        if (CollectionUtils.isEmpty(serverList)) {
            LOGGER.info("服务器列表为空，无需统计。");
            return;
        }

        save(serverList, LocalDateTime.now().plusMinutes(Constants.STATIC_MINUTE_BEFORE), LocalDateTime.now());
    }

}
