package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.CheckDeleteDeskStrategyAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVOIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVOIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbDeskStrategyVOINotifySPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.response.CbbDeskStrategyDeleteNotifyResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 删除 VOI 云桌面策略之前校验业务是否允许删除
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.04.29
 *
 * @author linhj
 */
public class DesktopStrategyVOINotifySPIImpl implements CbbDeskStrategyVOINotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopStrategyVOINotifySPIImpl.class);

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private CheckDeleteDeskStrategyAPI checkDeleteDeskStrategyAPI;


    @Override
    public CbbDeskStrategyDeleteNotifyResponse beforeDeleteDeskStrategyVOI(UUID strategyId) throws BusinessException {

        Assert.notNull(strategyId, "strategyId must not be null");
        LOGGER.info("spi 中接收到的即将删除的策略 id={}", strategyId);

        // 获取当前准备删除的云桌面策略关联的一个终端组
        CbbDeskStrategyVOIDTO idvStrategyDto = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOI(strategyId);
        String strategyName = idvStrategyDto.getName();

        // 校验是否可以删除
        checkDeleteDeskStrategyAPI.checkCanDeleteIDVOrVOIDeskStrategy(strategyId, strategyName);

        return new CbbDeskStrategyDeleteNotifyResponse();
    }
}
