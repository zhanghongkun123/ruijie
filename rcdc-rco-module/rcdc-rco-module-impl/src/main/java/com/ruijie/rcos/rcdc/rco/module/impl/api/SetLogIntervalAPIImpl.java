package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SetLogIntervalAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SetLogIntervalRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.handler.SetLogIntervalHandler;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * 
 * Description: 设置日志周期API实现类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/18
 *
 * @author zhiweiHong
 */
public class SetLogIntervalAPIImpl implements SetLogIntervalAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetLogIntervalAPIImpl.class);

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private CbbDesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    @Override
    public DefaultResponse setLogInterval(SetLogIntervalRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(request.getInterval(), "intervalDay is not null");
        LOGGER.info("获取当前设置日志周期天数");
        Integer oldValue = desktopOpLogMgmtAPI.getDesktopOpLogRetainDays();

        Map<String, Serializable> argMap = new HashMap<>();
        argMap.put(Constants.OLD_DAY_KEY, oldValue);
        argMap.put(Constants.INTERVAL_DAY_KEY, request.getInterval());
        LOGGER.info("开启设置日志周期状态机");
        stateMachineFactory.newBuilder(SetLogIntervalHandler.class).initArgs(argMap).start().waitForAllProcessFinish();
        return DefaultResponse.Builder.success();
    }
}
