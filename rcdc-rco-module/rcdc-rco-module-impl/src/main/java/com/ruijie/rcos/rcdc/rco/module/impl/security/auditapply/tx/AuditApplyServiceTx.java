package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.tx;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyAuditLogDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: 文件流转审计管理事务接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditApplyServiceTx {

    /**
     * 创建审计申请单
     * 
     * @param auditApplyDetailDTO 创建审计申请单详情
     */
    void createAuditApply(AuditApplyDetailDTO auditApplyDetailDTO);

    /**
     * 审批审计申请单
     *
     * @param auditApplyAuditLogDTO 审计申请单 审批记录日志详情
     * @return 申请单号
     * @throws BusinessException 业务异常
     */
    String handleAuditApply(AuditApplyAuditLogDTO auditApplyAuditLogDTO) throws BusinessException;

    /**
     * 废弃申请单
     * @param applyId 申请单UUID
     * @param reason 终止原因
     * @throws BusinessException 业务异常
     */
    void discardAuditApply(UUID applyId, String reason) throws BusinessException;

    /**
     * 撤销申请单
     * @param applyId 申请单UUID
     * @throws BusinessException 业务异常
     */
    void cancelAuditApply(UUID applyId) throws BusinessException;
}
