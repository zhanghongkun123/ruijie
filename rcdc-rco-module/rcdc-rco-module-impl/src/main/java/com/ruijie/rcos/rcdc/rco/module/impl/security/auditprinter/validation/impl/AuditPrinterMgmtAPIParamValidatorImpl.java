package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.validation.impl;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditFilePrintInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.enums.PrintStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.AuditFileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.AuditPrinterBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.validation.AuditPrinterMgmtAPIParamValidator;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Description: 安全打印机API参数校验实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
@Service
public class AuditPrinterMgmtAPIParamValidatorImpl implements AuditPrinterMgmtAPIParamValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditPrinterMgmtAPIParamValidatorImpl.class);

    @Override
    public void validateCreateAuditPrintApply(AuditApplyDetailDTO auditApplyDetailDTO) throws BusinessException {
        Assert.notNull(auditApplyDetailDTO, "auditApplyDetailDTO cannot be null");
        try {
            validateCreateAuditPrintApplyByLogger(auditApplyDetailDTO);
        } catch (BusinessException e) {
            LOGGER.error("创建打印申请单【{}】参数校验失败，原因：{}", auditApplyDetailDTO.getApplyType(), e.getI18nMessage());
            throw e;
        }
    }

    private void validateCreateAuditPrintApplyByLogger(AuditApplyDetailDTO auditApplyDetailDTO) throws BusinessException {
        // 校验入参
        if (CollectionUtils.isEmpty(auditApplyDetailDTO.getAuditFileList())) {
            throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_PARAMETER_ILLEGAL,
                    LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_LIST_IS_BLANK));
        } else {
            for (AuditFileDTO auditFileDTO : auditApplyDetailDTO.getAuditFileList()) {
                if (!StringUtils.hasText(auditFileDTO.getFileName())) {
                    throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_PARAMETER_ILLEGAL,
                            LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_NAME_IS_BLANK));
                }
                if (!StringUtils.hasText(auditFileDTO.getFileMd5())) {
                    throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_PARAMETER_ILLEGAL,
                            LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_MD5_IS_BLANK));
                }
                if (auditFileDTO.getFileSize() == null || auditFileDTO.getFileSize() < 0L) {
                    throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_PARAMETER_ILLEGAL,
                            LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_SIZE_ILLEGAL));
                }
                if (auditFileDTO.getFilePage() == null) {
                    throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_PARAMETER_ILLEGAL,
                            LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_PAGE_ILLEGAL));
                }
            }
        }
    }


    @Override
    public void validateHandleAuditPrintApplyResult(AuditFilePrintInfoDTO auditFilePrintInfoDTO) throws BusinessException {
        Assert.notNull(auditFilePrintInfoDTO, "auditFilePrintInfoDTO cannot be null");
        try {
            validateHandleAuditPrintApplyResultByLogger(auditFilePrintInfoDTO);
        } catch (BusinessException e) {
            LOGGER.error("打印结果上报参数校验失败，请求参数：{}，原因：{}", auditFilePrintInfoDTO, e.getI18nMessage());
            throw e;
        }
    }

    private void validateHandleAuditPrintApplyResultByLogger(AuditFilePrintInfoDTO auditFilePrintInfoDTO) throws BusinessException {
        if (PrintStateEnum.SUCCESS == auditFilePrintInfoDTO.getPrintState()) {
            if (!StringUtils.hasText(auditFilePrintInfoDTO.getPrinterName())) {
                throw new BusinessException(AuditPrinterBusinessKey.RCDC_RCO_AUDIT_FILE_PRINT_INFO_PRINTER_NAME_IS_NULL);
            }
        } else if (PrintStateEnum.FAIL == auditFilePrintInfoDTO.getPrintState()) {
            if (!StringUtils.hasText(auditFilePrintInfoDTO.getPrintResultMsg())) {
                throw new BusinessException(AuditPrinterBusinessKey.RCDC_RCO_AUDIT_FILE_PRINT_INFO_PRINT_RESULT_MSG_IS_NULL);
            }
        } else {
            throw new BusinessException(AuditPrinterBusinessKey.RCDC_RCO_AUDIT_FILE_PRINT_INFO_PRINT_STATE_ERROR);
        }
    }

}
