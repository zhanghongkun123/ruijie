package com.ruijie.rcos.rcdc.rco.module.impl.tx;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbPowerPlanEnum;

/**
 * Description: 迁移自动休眠全局配置到VDI桌面策略TX类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/24 10:16
 *
 * @author yxq
 */
public interface MigrateAutoSleepStrategyServiceTx {

    /**
     * 迁移自动休眠策略
     * @param powerPlan 休眠策略
     * @param powerPlanTime 自动休眠到时间
     */
    void migrateAutoSleepStrategy(CbbPowerPlanEnum powerPlan, Integer powerPlanTime);
}
