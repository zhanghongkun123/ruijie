package com.ruijie.rcos.rcdc.rco.module.impl.init;

import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLogAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * Description: 启动默认日志清理类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/20
 *
 * @author zhiweiHong
 */
@Service
public class StartCleanCollectLogTaskInitializer implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartCleanCollectLogTaskInitializer.class);

    @Autowired
    private CbbTerminalLogAPI cbbTerminalLogAPI;

    @Override
    public void safeInit() {
        LOGGER.info("开始启动默认日志清理任务");

        try {
            cbbTerminalLogAPI.startDefaultCleanCollectLogTask();
        } catch (BusinessException e) {
            LOGGER.error("启用默认日志清理任务失败！{}", e);
        }
    }
}
