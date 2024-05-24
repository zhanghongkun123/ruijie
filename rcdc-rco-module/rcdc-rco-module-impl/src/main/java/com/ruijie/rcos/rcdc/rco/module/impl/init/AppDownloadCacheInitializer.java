package com.ruijie.rcos.rcdc.rco.module.impl.init;

import com.ruijie.rcos.base.upgrade.module.def.enums.PacketProductType;
import com.ruijie.rcos.rcdc.rco.module.common.condition.ConditionProductOnConfig;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClientCompressionAPI;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;

/**
 * Description: 终端系统升级任务初始化
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/3 10:42
 *
 * @author conghaifeng
 */
@Service
@Conditional(ConditionProductOnConfig.class)
public class AppDownloadCacheInitializer implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppDownloadCacheInitializer.class);


    @Autowired
    private ClientCompressionAPI clientCompressionAPI;


    @Override
    public void safeInit() {
        ThreadExecutors.execute("init-oa-app", () -> {
            try {
                clientCompressionAPI.createConfiguredInstaller(PacketProductType.ONE_AGENT);
            } catch (Exception e) {
                LOGGER.error("init oa app fail", e);
            }
        });
        ThreadExecutors.execute("init-oc-app", () -> {
            try {
                clientCompressionAPI.createConfiguredInstaller(PacketProductType.ONE_CLIENT);
            } catch (Exception e) {
                LOGGER.error("init oc app fail", e);
            }
        });
    }
}
