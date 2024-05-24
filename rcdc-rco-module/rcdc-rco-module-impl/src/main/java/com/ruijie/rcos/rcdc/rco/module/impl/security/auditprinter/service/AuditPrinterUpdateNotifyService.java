package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: 安全打印机策略发生变更通知桌面
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/26
 *
 * @author lihengjing
 */

public interface AuditPrinterUpdateNotifyService {

    /**
     * 通知所有策略关联的桌面更新安全打印机策略
     */
    void notifyAllStrategyDeskAuditPrinter();

    /**
     * 通知云桌面策略关联的桌面更新打印审计策略
     *
     * @param id 云桌面策略ID
     */
    void notifyDeskAuditPrinterStrategyAndStrategyId(UUID id);

    /**
     * 通知指定云桌面安全打印策略变更
     * @param deskId 桌面ID
     * @param strategyVDIDTO 策略信息
     * @throws BusinessException 异常信息
     */
    void notifyDeskAuditPrinterStrategyAndDeskId(UUID deskId, CbbDeskStrategyVDIDTO strategyVDIDTO) throws BusinessException;
}
