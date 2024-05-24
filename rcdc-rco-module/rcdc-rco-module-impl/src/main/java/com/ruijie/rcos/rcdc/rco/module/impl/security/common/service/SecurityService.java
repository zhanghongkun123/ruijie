package com.ruijie.rcos.rcdc.rco.module.impl.security.common.service;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyAuditLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.FtpConfigDTO;

import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月02日
 *
 * @author lihengjing
 */
public interface SecurityService {

    /**
     * 根据申请UUID生成审批流程
     *
     * @param auditApplyDetailDTO 申请UUID
     * @return 审批流程
     */
    List<AuditApplyAuditLogDTO> generateAuditLogByApply(AuditApplyDetailDTO auditApplyDetailDTO);

    /**
     * 获得文件导出审批FTP配置（加密）
     * @return 文件导出审批FTP配置对象（加密）
     */
    FtpConfigDTO obtainAuditFileEncryptFtpInfo();

    /**
     * 获得文件导出审批FTP配置
     * @return 文件导出审批FTP配置对象
     */
    FtpConfigDTO obtainAuditFileFtpInfo();

}
