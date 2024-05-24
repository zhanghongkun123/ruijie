package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbThirdPartyDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.callback.CbbDeskStrategyCallback;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyThirdPartyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbDeskStrategyThirdPartyNotifySPI;
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
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月25日
 *
 * @author wangjie9
 */
public class DesktopStrategyThirdPartyNotifySPIImpl implements CbbDeskStrategyThirdPartyNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopStrategyThirdPartyNotifySPIImpl.class);

    @Autowired
    private CbbThirdPartyDeskStrategyMgmtAPI cbbThirdPartyDeskStrategyMgmtAPI;

    @Autowired
    private CheckDeleteDeskStrategyAPI checkDeleteDeskStrategyAPI;


    @Override
    public CbbDeskStrategyDeleteNotifyResponse beforeDeleteDeskStrategyThirdParty(UUID strategyId, String strategyName,
                                                                                  CbbDeskStrategyCallback callback) throws BusinessException {
        Assert.notNull(strategyId, "strategyId must not be null.");
        Assert.hasText(strategyName, "strategyName must not be null.");
        LOGGER.info("spi中接收到的即将删除的策略id：" + strategyId);
        checkDeleteDeskStrategyAPI.checkCanDeleteThirdPartyDeskStrategy(strategyId, strategyName);
        return new CbbDeskStrategyDeleteNotifyResponse();
    }
}
