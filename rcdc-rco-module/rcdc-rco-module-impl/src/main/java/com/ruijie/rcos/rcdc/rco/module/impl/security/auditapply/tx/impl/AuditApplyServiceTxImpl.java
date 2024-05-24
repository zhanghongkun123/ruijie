package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.tx.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.constants.AuditFileConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.AuditPrinterBusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyAuditLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyAuditLogStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.enums.AuditFileStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.enums.PrintStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dao.AuditApplyAuditLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dao.AuditApplyDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.entity.AuditApplyAuditLogEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.entity.AuditApplyEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.tx.AuditApplyServiceTx;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.dao.AuditFileDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.entity.AuditFileEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.dao.AuditFilePrintInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.entity.AuditFilePrintInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.security.common.SecurityBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 文件流转审计管理事务实现
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
@Service
public class AuditApplyServiceTxImpl implements AuditApplyServiceTx {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditApplyServiceTxImpl.class);

    private static final String FILE_NAME_SEPARATOR = ",";

    @Autowired
    private AuditApplyDAO auditApplyDAO;

    @Autowired
    private AuditFileDAO auditFileDAO;

    @Autowired
    private AuditFilePrintInfoDAO auditFilePrintInfoDAO;

    @Autowired
    private AuditApplyAuditLogDAO auditApplyAuditLogDAO;

    @Override
    public void createAuditApply(AuditApplyDetailDTO auditApplyDetailDTO) {
        Assert.notNull(auditApplyDetailDTO, "auditFileApplyDetailDTO not be null");
        List<AuditApplyAuditLogDTO> auditorList = auditApplyDetailDTO.getAuditorList();
        Assert.notNull(auditorList, "auditorList cannot be null");

        List<AuditFileDTO> auditFileList = auditApplyDetailDTO.getAuditFileList();

        // 1.保存申请单
        AuditApplyEntity auditApplyEntity = new AuditApplyEntity();
        BeanUtils.copyProperties(auditApplyDetailDTO, auditApplyEntity);
        if (!CollectionUtils.isEmpty(auditFileList)) {
            auditApplyEntity.setFileName(StringUtils.join(auditFileList.stream().map(AuditFileDTO::getFileName)
                    .collect(Collectors.toList()), FILE_NAME_SEPARATOR));
        }
        auditApplyDAO.save(auditApplyEntity);

        if (!CollectionUtils.isEmpty(auditFileList)) {
            // 2.保存文件列表
            List<AuditFileEntity> auditFileEntityList = new ArrayList<>();
            List<AuditFilePrintInfoEntity> auditPrinterEntityList = new ArrayList<>();
            for (AuditFileDTO auditFileDTO : auditFileList) {
                auditFileDTO.setApplyId(auditApplyDetailDTO.getId());
                AuditFileEntity auditFileEntity = new AuditFileEntity();
                BeanUtils.copyProperties(auditFileDTO, auditFileEntity);
                auditFileEntityList.add(auditFileEntity);
                // 打印类型申请单 保存打印记录
                if (AuditApplyTypeEnum.PRINT == auditApplyDetailDTO.getApplyType()) {
                    AuditFilePrintInfoEntity auditFilePrintInfoEntity = new AuditFilePrintInfoEntity();
                    auditFilePrintInfoEntity.setId(UUID.randomUUID());
                    auditFilePrintInfoEntity.setFileId(auditFileEntity.getId());
                    if (AuditApplyStateEnum.REJECTED == auditApplyDetailDTO.getState()) {
                        auditFilePrintInfoEntity.setPrintState(PrintStateEnum.FAIL);
                        auditFilePrintInfoEntity
                                .setPrintResultMsg(LocaleI18nResolver.resolve(AuditPrinterBusinessKey.RCDC_RCO_AUDIT_PRINT_APPLY_LIMIT));
                    } else {
                        auditFilePrintInfoEntity.setPrintState(PrintStateEnum.PENDING_PRINT);
                    }
                    auditFilePrintInfoEntity.setCreateTime(new Date());
                    auditPrinterEntityList.add(auditFilePrintInfoEntity);
                }
            }
            auditFileDAO.saveAll(auditFileEntityList);
            // 2.1 保存打印记录
            if (!CollectionUtils.isEmpty(auditPrinterEntityList)) {
                auditFilePrintInfoDAO.saveAll(auditPrinterEntityList);
            }
        }
        
        // 3.保存审批流程
        List<AuditApplyAuditLogEntity> auditApplyAuditLogEntityList = new ArrayList<>();
        for (AuditApplyAuditLogDTO auditApplyAuditLogDTO : auditorList) {
            AuditApplyAuditLogEntity auditApplyAuditLogEntity = new AuditApplyAuditLogEntity();
            BeanUtils.copyProperties(auditApplyAuditLogDTO, auditApplyAuditLogEntity);
            auditApplyAuditLogEntityList.add(auditApplyAuditLogEntity);
        }
        auditApplyAuditLogDAO.saveAll(auditApplyAuditLogEntityList);
    }

    @Override
    public String handleAuditApply(AuditApplyAuditLogDTO auditApplyAuditLogDTO) throws BusinessException {
        Assert.notNull(auditApplyAuditLogDTO, "AuditApplyAuditLogDTO not be null");
        UUID applyId = auditApplyAuditLogDTO.getApplyId();
        Assert.notNull(applyId, "applyId not be null");
        Assert.notNull(auditApplyAuditLogDTO.getAuditorState(), "auditorState not be null");

        AuditApplyEntity auditFileApply = auditApplyDAO.findById(applyId).orElseThrow(
            () -> new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_NOT_EXIST, String.valueOf(auditApplyAuditLogDTO.getId())));
        // 判断申请单是否结束
        if (AuditApplyStateEnum.enableApplyFinish(auditFileApply.getState())) {
            throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_IS_FINISHED, auditFileApply.getState().getMessage());
        }
        
        // 1.更新审批流程
        AuditApplyAuditLogEntity auditApplyAuditLogEntity = auditApplyAuditLogDAO.findById(auditApplyAuditLogDTO.getId())
                .orElseThrow(() -> new BusinessException(SecurityBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_AUDITOR_LOG_RECORD_NOT_EXIST,
                        String.valueOf(auditApplyAuditLogDTO.getId())));
        auditApplyAuditLogEntity.setAuditorId(auditApplyAuditLogDTO.getAuditorId());
        auditApplyAuditLogEntity.setAuditorName(auditApplyAuditLogDTO.getAuditorName());
        auditApplyAuditLogEntity.setAuditorState(auditApplyAuditLogDTO.getAuditorState());
        auditApplyAuditLogEntity.setAuditorOpinion(auditApplyAuditLogDTO.getAuditorOpinion());
        auditApplyAuditLogEntity.setUpdateTime(new Date());

        auditApplyAuditLogDAO.save(auditApplyAuditLogEntity);

        // 2. 如果审批流程为最后一个流程 更新申请单、文件状态
        if (auditApplyAuditLogEntity.getIsLastAuditor()) {

            // 2.1 更新文件状态
            List<AuditFileEntity> fileEntityList = auditFileDAO.findByApplyId(applyId);
            if (!CollectionUtils.isEmpty(fileEntityList)) {
                for (AuditFileEntity auditFileEntity : fileEntityList) {
                    if (AuditApplyAuditLogStateEnum.APPROVED.equals(auditApplyAuditLogEntity.getAuditorState())) {
                        auditFileEntity.setFileState(AuditFileStateEnum.APPROVED);
                    } else if (AuditApplyAuditLogStateEnum.REJECTED.equals(auditApplyAuditLogEntity.getAuditorState())) {
                        auditFileEntity.setFileState(AuditFileStateEnum.REJECTED);
                    }
                    auditFileEntity.setUpdateTime(new Date());
                }
                auditFileDAO.saveAll(fileEntityList);
            }

            // 2.2 更新申请单状态
            if (AuditApplyAuditLogStateEnum.APPROVED.equals(auditApplyAuditLogEntity.getAuditorState())) {
                auditFileApply.setState(AuditApplyStateEnum.APPROVED);
            } else if (AuditApplyAuditLogStateEnum.REJECTED.equals(auditApplyAuditLogEntity.getAuditorState())) {
                auditFileApply.setState(AuditApplyStateEnum.REJECTED);
            }
            auditFileApply.setUpdateTime(new Date());

            auditApplyDAO.save(auditFileApply);

        }
        return auditFileApply.getApplySerialNumber();
    }

    @Override
    public void discardAuditApply(UUID applyId, String reason) throws BusinessException {
        Assert.notNull(applyId, "applyId not be null");
        Assert.hasText(reason, "reason not be null");
        LockableExecutor.executeWithTryLock(AuditFileConstants.AUDIT_FILE_APPLY_STATE_LOCK + applyId, 
            () -> {
                AuditApplyEntity auditApplyEntity = auditApplyDAO.findById(applyId)
                        .orElseThrow(() -> new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_NOT_EXIST, String.valueOf(applyId)));
                // 判断申请单是否支持更新为失败
                if (AuditApplyStateEnum.enableApplyFail(auditApplyEntity.getState())) {
                    throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_IS_FINISHED, auditApplyEntity.getState().getMessage());
                }
                updateAuditApply(auditApplyEntity, AuditApplyStateEnum.FAIL, reason);
            }, AuditFileConstants.AUDIT_FILE_APPLY_STATE_LOCK_TIMEOUT);
    }

    @Override
    public void cancelAuditApply(UUID applyId) throws BusinessException {
        Assert.notNull(applyId, "cancelAuditApply applyId not be null");
        LockableExecutor.executeWithTryLock(AuditFileConstants.AUDIT_FILE_APPLY_STATE_LOCK + applyId, 
            () -> {
                AuditApplyEntity auditApplyEntity = auditApplyDAO.findById(applyId)
                        .orElseThrow(() -> new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_NOT_EXIST, String.valueOf(applyId)));
                // 判断申请单是否结束
                if (AuditApplyStateEnum.enableApplyFinish(auditApplyEntity.getState())) {
                    throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_IS_FINISHED, auditApplyEntity.getState().getMessage());
                }
                updateAuditApply(auditApplyEntity, AuditApplyStateEnum.CANCELED, null);
            }, AuditFileConstants.AUDIT_FILE_APPLY_STATE_LOCK_TIMEOUT);
    }
    
    private void updateAuditApply(AuditApplyEntity auditApplyEntity, AuditApplyStateEnum applyState, String reason) {
        UUID applyId = auditApplyEntity.getId();

        // 更新申请单状态，以及失败原因
        auditApplyEntity.setState(applyState);
        if (StringUtils.isNotBlank(reason)) {
            auditApplyEntity.setFailReason(reason);
        }
        auditApplyEntity.setUpdateTime(new Date());
        auditApplyDAO.save(auditApplyEntity);
        
        // 处于中间状态的文件单置为失败
        Set<AuditFileEntity> fileEntitySet = auditFileDAO.findByApplyId(applyId).stream()
                .filter(auditFileEntity -> auditFileEntity.getFileState() == AuditFileStateEnum.COMPUTING
                        || auditFileEntity.getFileState() == AuditFileStateEnum.UPLOADING
                        || auditFileEntity.getFileState() == AuditFileStateEnum.UPLOADED)
                .peek(auditFileEntity -> {
                    auditFileEntity.setFileState(AuditFileStateEnum.FAIL);
                    auditFileEntity.setUpdateTime(new Date());
                }).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(fileEntitySet)) {
            auditFileDAO.saveAll(fileEntitySet);
        }
    }

}
