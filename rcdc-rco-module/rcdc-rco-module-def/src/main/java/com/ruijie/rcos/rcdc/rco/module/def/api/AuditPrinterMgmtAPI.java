package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.FtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditFilePrintInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrintApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrintLogStaticDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrinterStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.ViewAuditPrintLogDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

import java.util.UUID;

/**
 * Description: 安全打印机功能管理API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditPrinterMgmtAPI {

    /**
     * 根据云桌面ID获得安全打印机策略
     *
     * @param deskId 云桌面ID
     * @return 安全打印机全局策略对象
     * @throws BusinessException 业务异常
     */
    AuditPrinterStrategyDTO obtainAuditPrinterStrategy(UUID deskId) throws BusinessException;

    /**
     * 获得安全打印机FTP配置（加密）
     * 
     * @return 安全打印机FTP配置对象（加密）
     */
    FtpConfigDTO obtainAuditPrinterEncryptFtpInfo();

    /**
     * 处理打印结果
     *
     * @param deskId 云桌面ID
     * @param auditFilePrintInfoDTO 安全打印机打印结果
     * @throws BusinessException 业务异常
     */
    void handleAuditPrintApplyResult(UUID deskId, AuditFilePrintInfoDTO auditFilePrintInfoDTO) throws BusinessException;

    /**
     * 分页查询打印记录
     * @param request 分页查询对象
     * @return 打印记录分页记录
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<ViewAuditPrintLogDTO> auditPrintLogPageQuery(PageQueryRequest request) throws BusinessException;


    /**
     * 分页查询打印记录统计报表
     * @param request 分页查询对象
     * @return 分页数据
     */
    PageQueryResponse<AuditPrintLogStaticDTO> auditPrintLogStaticPageQuery(PageQueryRequest request);

    /**
     * 根据申请UUID获取打印申请单详情
     * @param applyId 申请单UUID
     * @return 打印申请单详情
     * @throws BusinessException 业务异常
     */
    AuditPrintApplyDetailDTO findAuditPrintApplyDetailById(UUID applyId) throws BusinessException;
}
