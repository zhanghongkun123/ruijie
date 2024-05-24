package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.tx.impl;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.entity.AuditFileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dao.AuditApplyAuditLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dao.AuditApplyDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.dao.AuditFileDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.dao.AuditFilePrintInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.tx.AuditPrinterServiceTx;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 安全打印审计管理事务实现
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
@Service
public class AuditPrinterServiceTxImpl implements AuditPrinterServiceTx {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditPrinterServiceTxImpl.class);

    @Autowired
    private AuditApplyDAO auditApplyDAO;

    @Autowired
    private AuditFileDAO auditFileDAO;

    @Autowired
    private AuditApplyAuditLogDAO auditApplyAuditLogDAO;

    @Autowired
    private AuditFilePrintInfoDAO auditFilePrintInfoDAO;

    @Override
    public void deleteAuditPrintApply(UUID applyId) {
        Assert.notNull(applyId, "applyId must be not null");
        List<AuditFileEntity> auditFileEntityList = auditFileDAO.findByApplyId(applyId);
        for (AuditFileEntity file : auditFileEntityList) {
            auditFilePrintInfoDAO.deleteByFileId(file.getId());
        }
        auditFileDAO.deleteAllByApplyId(applyId);
        auditApplyAuditLogDAO.deleteAllByApplyId(applyId);
        auditApplyDAO.deleteById(applyId);
    }

}
