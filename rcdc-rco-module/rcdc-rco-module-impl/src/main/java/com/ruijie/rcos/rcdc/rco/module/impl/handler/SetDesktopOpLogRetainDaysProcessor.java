package com.ruijie.rcos.rcdc.rco.module.impl.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;

/**
 *
 * Description: 设置桌面日志周期处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/18
 *
 * @author zhiweiHong
 */
@Service
public class SetDesktopOpLogRetainDaysProcessor implements StateTaskHandle.StateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetDesktopOpLogRetainDaysProcessor.class);

    @Autowired
    private CbbDesktopOpLogMgmtAPI userDesktopOpLogMgmtAPI;

    @Override
    public StateTaskHandle.ProcessResult doProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context is not null");
        Integer intervalDay = context.get(Constants.INTERVAL_DAY_KEY, Integer.class);
        LOGGER.info("调用CbbUserDesktopOpLogMgmtAPI组件更新日志周期天数，更新时间为{}", intervalDay);
        userDesktopOpLogMgmtAPI.updateDesktopOpLogRetainDays(intervalDay);
        return StateTaskHandle.ProcessResult.next();
    }


    @Override
    public StateTaskHandle.ProcessResult undoProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context is not null");
        Integer oldValue = context.get(Constants.OLD_DAY_KEY, Integer.class);
        LOGGER.info("执行CbbUserDesktopOpLogMgmtAPI失败，开始回滚，回滚数值为{}", oldValue);
        userDesktopOpLogMgmtAPI.updateDesktopOpLogRetainDays(oldValue);
        return StateTaskHandle.ProcessResult.next();
    }
}
