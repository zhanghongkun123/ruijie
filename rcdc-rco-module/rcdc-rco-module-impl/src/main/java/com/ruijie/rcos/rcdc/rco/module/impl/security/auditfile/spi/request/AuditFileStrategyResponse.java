package com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.spi.request;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.FtpConfigDTO;

import java.util.UUID;

/**
 * Description: 文件导出审批全局策略配置GT响应对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public class AuditFileStrategyResponse {
    /**
     * 请求ID
     */
    private UUID requestId;

    /**
     * 文件导出审批配置信息
     */
    private AuditFileStrategyDTO auditFileStrategy;

    /**
     * Ftp配置信息
     */
    private FtpConfigDTO ftpInfo;

    public AuditFileStrategyResponse(AuditFileStrategyDTO auditFileStrategy, FtpConfigDTO ftpConfigDTO) {
        this.auditFileStrategy = auditFileStrategy;
        this.ftpInfo = ftpConfigDTO;
    }

    public AuditFileStrategyResponse() {

    }

    public UUID getRequestId() {
        return this.requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public AuditFileStrategyDTO getAuditFileStrategy() {
        return auditFileStrategy;
    }

    public void setAuditFileStrategy(AuditFileStrategyDTO auditFileStrategy) {
        this.auditFileStrategy = auditFileStrategy;
    }

    public FtpConfigDTO getFtpInfo() {
        return this.ftpInfo;
    }

    public void setFtpInfo(FtpConfigDTO ftpInfo) {
        this.ftpInfo = ftpInfo;
    }
}
