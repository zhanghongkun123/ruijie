package com.ruijie.rcos.rcdc.rco.module.impl.init;

import com.ruijie.rcos.rcdc.rco.module.common.condition.ConditionProductOnConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.cmscomponent.CmsComponentBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalUpgradeInitAPI;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;

/**
 *
 * Description: 终端系统初始化
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月22日
 *
 * @author nting
 */
@Service
@Conditional(ConditionProductOnConfig.class)
public class TerminalUpgradeInitializer implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalUpgradeInitializer.class);

    @Autowired
    private CbbTerminalUpgradeInitAPI terminalUpgradeInitAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    // 初始化安卓OTA线程名称
    private static final String TERMINAL_UPGRADE_INITIALIZER = "terminal_upgrade_initializer";

    // 初始化安卓OTA异常重试间隔时间
    private static final Integer SLEEP_TIME = 10 * 1000;

    @Override
    public void safeInit() {
        ThreadExecutors.execute(TERMINAL_UPGRADE_INITIALIZER, this::doInitProc);
    }

    private void doInitProc() {
        LOGGER.info("开始初始化安卓OTA");
        // 启动初始化安卓OTA，强关联，直到成功为止
        while (true) {
            try {
                if (serverModelAPI.isInitModel()) {
                    LOGGER.error("获取服务器部署信息失败：未找到对应的模式");
                    throw new BusinessException(CmsComponentBusinessKey.RCDC_RCO_GET_CMS_COMPONENT_STATUS_ERROR);
                }
                if (serverModelAPI.isVdiModel()) {
                    LOGGER.info("开始初始化安卓OTA");
                    terminalUpgradeInitAPI.initAndroidOTA();
                    LOGGER.info("完成初始化安卓OTA");
                }
                return;
            } catch (Exception e) {
                LOGGER.error("初始化安卓OTA失败：{}，反复重试", e.getMessage());
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException exception) {
                    LOGGER.error("初始化安卓OTA失败,间隔{}秒出现异常。", SLEEP_TIME, exception.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
