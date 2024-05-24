package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.maintenance.module.def.spi.CbbBusinessMaintainProductTaskSPI;


/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/4
 *
 * @author linke
 */
public class CbbBusinessMaintainProductTaskSPIImpl implements CbbBusinessMaintainProductTaskSPI {

    // 业务维护模式，每5s调用一次
    private static final int PERIOD = 5;

    private static final String TASK_NAME = "maintenance-classroom-end-lesson-task";

    @Override
    public boolean initMethod() {
        // 办公不需要实现什么逻辑
        return false;
    }

    @Override
    public boolean doWork() {
        // 办公不需要实现什么逻辑
        return false;
    }

    @Override
    public String getTaskName() {
        // 办公不需要实现什么逻辑
        return TASK_NAME;
    }

    @Override
    public int getPeriod() {
        // 办公不需要实现什么逻辑
        return PERIOD;
    }
}
