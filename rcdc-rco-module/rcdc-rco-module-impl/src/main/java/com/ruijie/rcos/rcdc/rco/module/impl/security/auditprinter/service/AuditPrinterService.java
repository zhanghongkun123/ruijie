package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditFilePrintInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrintLogStaticDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrinterStrategyDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

import java.util.List;
import java.util.UUID;

/**
 * Description: 安全打印机管理接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditPrinterService {

    /**
     * 根据云桌面ID获取安全打印机策略
     *
     * @param deskId 云桌面ID
     * @return 安全打印机全局策略
     * @throws BusinessException 业务异常
     */
    AuditPrinterStrategyDTO obtainAuditPrinterStrategy(UUID deskId) throws BusinessException;

    /**
     * 检查是否开启安全打印机功能
     *
     * @param deskId 云桌面ID
     * @throws BusinessException 业务异常
     */
    void checkEnableAuditPrinter(UUID deskId) throws BusinessException;

    /**
     * 处理打印结果通知
     * @param auditFilePrintInfoDTO 打印结果信息
     * @throws BusinessException 业务异常
     */
    void handleAuditPrintApplyResult(AuditFilePrintInfoDTO auditFilePrintInfoDTO) throws BusinessException;

    /**
     * 分页查询打印记录统计报表
     * @param request 分页查询对象
     * @return 分页数据
     */
    PageQueryResponse<AuditPrintLogStaticDTO> auditPrintLogStaticPageQuery(PageQueryRequest request);
    
    /**
     * 根据文件列表ID获取打印记录
     * @param fileIdList 文件ID列表
     * @return 打印记录列表
     */
    List<AuditFilePrintInfoDTO> findAuditFilePrintInfoListByFileIdList(List<UUID> fileIdList);
}
