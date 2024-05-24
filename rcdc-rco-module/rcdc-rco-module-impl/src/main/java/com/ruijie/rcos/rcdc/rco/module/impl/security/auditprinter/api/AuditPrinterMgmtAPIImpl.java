package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.AuditApplyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditPrinterMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.FtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditFilePrintInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrintApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrintLogStaticDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrinterStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.ViewAuditPrintLogDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service.AuditPrintLogService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service.AuditPrinterService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.validation.AuditPrinterMgmtAPIParamValidator;
import com.ruijie.rcos.rcdc.rco.module.impl.security.common.service.SecurityService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 安全打印机功能管理实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public class AuditPrinterMgmtAPIImpl implements AuditPrinterMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditPrinterMgmtAPIImpl.class);

    @Autowired
    private AuditPrinterMgmtAPIParamValidator auditPrinterMgmtAPIParamValidator;

    @Autowired
    private AuditPrinterService auditPrinterService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private AuditApplyMgmtAPI auditApplyMgmtAPI;

    @Autowired
    private AuditPrintLogService auditPrintLogService;

    @Override
    public AuditPrinterStrategyDTO obtainAuditPrinterStrategy(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId is null");
        return auditPrinterService.obtainAuditPrinterStrategy(deskId);
    }

    @Override
    public FtpConfigDTO obtainAuditPrinterEncryptFtpInfo() {
        return securityService.obtainAuditFileEncryptFtpInfo();

    }

    @Override
    public void handleAuditPrintApplyResult(UUID deskId, AuditFilePrintInfoDTO auditFilePrintInfoDTO) throws BusinessException {
        Assert.notNull(deskId, "deskId is null");
        Assert.notNull(auditFilePrintInfoDTO, "auditFilePrintInfoDTO is null");

        auditPrinterService.checkEnableAuditPrinter(deskId);
        auditPrinterMgmtAPIParamValidator.validateHandleAuditPrintApplyResult(auditFilePrintInfoDTO);

        auditPrinterService.handleAuditPrintApplyResult(auditFilePrintInfoDTO);
    }

    @Override
    public PageQueryResponse<ViewAuditPrintLogDTO> auditPrintLogPageQuery(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        return auditPrintLogService.pageQuery(request);
    }


    @Override
    public PageQueryResponse<AuditPrintLogStaticDTO> auditPrintLogStaticPageQuery(PageQueryRequest request) {
        Assert.notNull(request, "request is null");

        return auditPrinterService.auditPrintLogStaticPageQuery(request);
    }

    @Override
    public AuditPrintApplyDetailDTO findAuditPrintApplyDetailById(UUID applyId) throws BusinessException {
        Assert.notNull(applyId, "applyId is null");
        AuditApplyDetailDTO auditApplyDetail = auditApplyMgmtAPI.findAuditApplyDetailById(applyId);

        AuditPrintApplyDetailDTO auditPrintApplyDetailDTO = new AuditPrintApplyDetailDTO();
        BeanUtils.copyProperties(auditApplyDetail, auditPrintApplyDetailDTO);

        List<UUID> fileIdList = auditPrintApplyDetailDTO.getAuditFileList().stream().map(AuditFileDTO::getId).collect(Collectors.toList());
        List<AuditFilePrintInfoDTO> auditFilePrintInfoDTOList = auditPrinterService.findAuditFilePrintInfoListByFileIdList(fileIdList);

        auditPrintApplyDetailDTO.setAuditFilePrintInfoList(auditFilePrintInfoDTOList);
        return auditPrintApplyDetailDTO;

    }

}
