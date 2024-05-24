package com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.validation;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 文件流转审计API参数校验接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditFileMgmtAPIParamValidator {

    /**
     * 创建申请单参数校验
     * @param auditApplyDetailDTO 文件流转审计申请请求对象
     * @throws BusinessException 业务异常
     */
    void validateCreateAuditFileApply(AuditApplyDetailDTO auditApplyDetailDTO) throws BusinessException;
}
