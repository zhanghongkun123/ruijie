package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TokenContextRegistry;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;

/**
 * Description: 清除失效的rcdc 免登陆token
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-10-09
 * 0/5 * * * * ? *
 *
 * @author zqj
 */
@Service
@Quartz(scheduleTypeCode = "rcdc_no_auth_invalid_token_clean", scheduleName = BusinessKey.RCDC_NO_AUTH_QUARTZ_INVALID_TOKEN_CLEAN,
        cron = "0 0 5 * * ?")
public class RcdcNoAuthTokenCleanQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcdcNoAuthTokenCleanQuartzTask.class);


    @Autowired
    private TokenContextRegistry tokenContextRegistry;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        tokenContextRegistry.cleanInvalidToken();
    }
}
