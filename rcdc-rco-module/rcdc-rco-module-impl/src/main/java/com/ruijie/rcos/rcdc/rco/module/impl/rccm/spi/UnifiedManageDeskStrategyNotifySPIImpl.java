package com.ruijie.rcos.rcdc.rco.module.impl.rccm.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbUpdateDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbDeskStrategyChangeNotifySPI;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.UnifiedManageDataService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 统一管理接收策略变更SPI实现
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/11
 *
 * @author TD
 */
public class UnifiedManageDeskStrategyNotifySPIImpl implements CbbDeskStrategyChangeNotifySPI {

    @Autowired
    private UnifiedManageDataService unifiedManageDataService;

    @Override
    public void notify(CbbUpdateDeskStrategyVDIDTO request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        // 空实现
    }

    @Override
    public void createNotify(UUID deskStrategyId) throws BusinessException {
        Assert.notNull(deskStrategyId, "createNotify deskStrategyId can not be null");
        unifiedManageDataService.createNotify(deskStrategyId);
    }

    @Override
    public void updateNotify(UUID deskStrategyId) throws BusinessException {
        Assert.notNull(deskStrategyId, "updateNotify deskStrategyId can not be null");
        unifiedManageDataService.updateNotify(deskStrategyId);
    }

    @Override
    public void deleteNotify(UUID deskStrategyId, String deskStrategyName) throws BusinessException {
        Assert.notNull(deskStrategyId, "deleteNotify deskStrategyId can not be null");
        Assert.hasText(deskStrategyName, "deleteNotify deskStrategyName can not be null");
        unifiedManageDataService.deleteNotify(deskStrategyId,deskStrategyName);
    }

}
