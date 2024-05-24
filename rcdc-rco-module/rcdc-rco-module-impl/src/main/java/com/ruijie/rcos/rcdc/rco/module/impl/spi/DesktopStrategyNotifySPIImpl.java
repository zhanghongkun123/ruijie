package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.callback.CbbDeskStrategyCallback;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbDeskStrategyVDINotifySPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.response.CbbDeskStrategyDeleteNotifyResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.CheckDeleteDeskStrategyAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 策略处理的spi接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月7日
 *
 * @author artom
 */
public class DesktopStrategyNotifySPIImpl implements CbbDeskStrategyVDINotifySPI {

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CheckDeleteDeskStrategyAPI checkDeleteDeskStrategyAPI;

    @Override
    public CbbDeskStrategyDeleteNotifyResponse beforeDeleteDeskStrategyVDI(UUID strategyId, CbbDeskStrategyCallback callback)
            throws BusinessException {
        Assert.notNull(strategyId, "strategyId must not be null.");

        CbbDeskStrategyVDIDTO strategyDto = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(strategyId);
        String strategyName = strategyDto.getName();

        // 检查云桌面策略是否可以删除
        checkDeleteDeskStrategyAPI.checkCanDeleteVDIDeskStrategy(strategyId, strategyName);

        return new CbbDeskStrategyDeleteNotifyResponse();
    }

}
