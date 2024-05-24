package com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.service;

import java.util.UUID;

/**
 * Description: 文件流转审计策略发生变更通知桌面
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/26
 *
 * @author lihengjing
 */

public interface AuditFileUpdateNotifyService {

    /**
     * 通知所有桌面更新文件流转审计全局策略
     */
    void notifyAllStrategyDeskAuditFile();

    /**
     * 通知云桌面策略关联的桌面更新文件审计策略
     *
     * @param id 云桌面策略ID
     */
    void notifyDeskAuditFileStrategyAndStrategyId(UUID id);
}
