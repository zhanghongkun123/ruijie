package com.ruijie.rcos.rcdc.rco.module.impl.init;

import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.CompensateSessionType;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: 系统异常恢复后，动态池桌面运行中的是否需要添加断连标识
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/08/26
 *
 * @author linke
 */
@Service
public class PoolDesktopConnectCloseTimeInitImpl implements SafetySingletonInitializer {
    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Override
    public void safeInit() {
        userDesktopMgmtAPI.compensateConnectClosedTime(CompensateSessionType.START_UP);
    }
}
