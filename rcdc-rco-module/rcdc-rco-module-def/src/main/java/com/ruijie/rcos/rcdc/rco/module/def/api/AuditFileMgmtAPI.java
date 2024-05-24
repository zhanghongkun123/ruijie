package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileGlobalConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.FtpConfigDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: 文件导出审批功能管理API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditFileMgmtAPI {

    /**
     * 获得文件导出审批全局策略
     *
     * @param deskId 云桌面ID
     * @return 文件导出审批全局策略对象
     * @throws BusinessException 业务异常
     */
    AuditFileStrategyDTO obtainAuditFileStrategy(UUID deskId) throws BusinessException;

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

    /**
     * 编辑文件导出审批全局策略
     *
     * @param auditFileGlobalStrategyDTO 文件导出审批全局策略对象
     * @throws BusinessException 业务异常
     */
    void editAuditFileGlobalStrategy(AuditFileGlobalConfigDTO auditFileGlobalStrategyDTO) throws BusinessException;


    /**
     * 根据文件ID查询
     *
     * @param fileId 文件ID
     * @return 文件信息
     * @throws BusinessException 业务异常
     */
    AuditFileDTO findAuditFileById(UUID fileId) throws BusinessException;

    /**
     * 获得文件导出审批全局配置
     *
     * @return 文件导出审批全局策略对象
     */
    AuditFileGlobalConfigDTO obtainAuditFileGlobalConfig();
}
