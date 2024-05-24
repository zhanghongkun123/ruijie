package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.callback.CbbDeskStrategyCallback;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbDeskStrategyIDVNotifySPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.response.CbbDeskStrategyDeleteNotifyResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.CheckDeleteDeskStrategyAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/28 16:12
 *
 * @author conghaifeng
 */
public class DesktopStrategyIDVNotifySPIImpl implements CbbDeskStrategyIDVNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopStrategyIDVNotifySPIImpl.class);

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CheckDeleteDeskStrategyAPI checkDeleteDeskStrategyAPI;


    @Override
    public CbbDeskStrategyDeleteNotifyResponse beforeDeleteDeskStrategyIDV(UUID strategyId, CbbDeskStrategyCallback callback)
            throws BusinessException {
        Assert.notNull(strategyId, "strategyId must not be null.");
        LOGGER.info("spi中接收到的即将删除的策略id：" + strategyId);
        CbbDeskStrategyIDVDTO idvStrategyDto = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(strategyId);
        String strategyName = idvStrategyDto.getName();

        checkDeleteDeskStrategyAPI.checkCanDeleteIDVOrVOIDeskStrategy(strategyId, strategyName);

        return new CbbDeskStrategyDeleteNotifyResponse();
    }

}
