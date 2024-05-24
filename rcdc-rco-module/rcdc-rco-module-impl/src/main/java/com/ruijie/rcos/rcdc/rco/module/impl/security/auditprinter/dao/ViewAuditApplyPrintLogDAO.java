package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.dao;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.entity.ViewAuditApplyPrintLogEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年2月22日
 *
 * @author jarmna
 */
public interface ViewAuditApplyPrintLogDAO
        extends SkyEngineJpaRepository<ViewAuditApplyPrintLogEntity, UUID>, PageQueryDAO<ViewAuditApplyPrintLogEntity> {


}
