package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.tx;

import java.util.UUID;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 安全打印审计管理事务接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditPrinterServiceTx {

    /**
     * 删除打印申请单记录及关联文件和审批记录
     *
     * @param applyId 申请单ID
     * @throws BusinessException 业务异常
     */
    void deleteAuditPrintApply(UUID applyId) throws BusinessException;
}
