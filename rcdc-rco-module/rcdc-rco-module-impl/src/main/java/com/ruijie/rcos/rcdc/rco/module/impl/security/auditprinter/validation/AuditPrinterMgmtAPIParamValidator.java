package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.validation;


import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditFilePrintInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 安全打印机API参数校验接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditPrinterMgmtAPIParamValidator {

    /**
     * 创建打印申请单参数校验
     *
     * @param auditApplyDetailDTO 打印申请单参数
     * @throws BusinessException 业务异常
     */
    void validateCreateAuditPrintApply(AuditApplyDetailDTO auditApplyDetailDTO) throws BusinessException;

    /**
     * 打印结果处理参数校验
     * @param auditFilePrintInfoDTO 打印结果处理
     * @throws BusinessException 业务异常
     */
    void validateHandleAuditPrintApplyResult(AuditFilePrintInfoDTO auditFilePrintInfoDTO) throws BusinessException;
}
