package com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.tx.impl;

import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dao.AuditApplyAuditLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dao.AuditApplyDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.dao.AuditFileDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.tx.AuditFileServiceTx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 文件导出审批管理事务实现
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
@Service
public class AuditFileServiceTxImpl implements AuditFileServiceTx {
    
    @Autowired
    private AuditApplyDAO auditApplyDAO;

    @Autowired
    private AuditFileDAO auditFileDAO;

    @Autowired
    private AuditApplyAuditLogDAO auditApplyAuditLogDAO;

    @Override
    public void deleteAuditFileApply(UUID applyId) {
        Assert.notNull(applyId, "applyId must be not null");
        auditApplyAuditLogDAO.deleteAllByApplyId(applyId);
        auditFileDAO.deleteAllByApplyId(applyId);
        auditApplyDAO.deleteById(applyId);
    }
}
