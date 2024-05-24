package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyAuditLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyAuditLogStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.constants.AuditFileConstants;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.enums.AuditFileStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrinterStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dao.AuditApplyAuditLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dao.AuditApplyDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.entity.AuditApplyAuditLogEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.entity.AuditApplyEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.AuditApplyService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.AuditApplyUpdateNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.tx.AuditApplyServiceTx;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.AuditFileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.dao.AuditFileDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.entity.AuditFileEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.service.AuditFileService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service.AuditPrinterService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.common.SecurityBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.DateUtils;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Description: 文件流转审计管理实现
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
@Service
public class AuditApplyServiceImpl implements AuditApplyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditApplyServiceImpl.class);

    private static final String RESOURCE_LOCKED_ERROR_KEY = "sk-resource_locked";


    /**
     * 申请单号缓存器：设置1分钟后过期
     */
    private static final Cache<String, String> APPLY_ID_CACHE =
            CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).maximumSize(10000).build();

    @Autowired
    private AuditApplyDAO auditApplyDAO;

    @Autowired
    private AuditFileDAO auditFileDAO;

    @Autowired
    private AuditApplyAuditLogDAO auditApplyAuditLogDAO;

    @Autowired
    private AuditApplyServiceTx auditApplyServiceTx;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private AuditApplyUpdateNotifyService auditApplyUpdateNotifyService;

    @Autowired
    private AuditFileService auditFileService;

    @Autowired
    private AuditPrinterService auditPrinterService;

    @Override
    public synchronized String generateApplySerialNumber() {
        while (true) {
            String applySerialNumber = DateUtils.format(new Date(), AuditFileConstants.AUDIT_FILE_APPLY_SERIAL_NUMBER_FORMAT);
            if (Objects.isNull(APPLY_ID_CACHE.getIfPresent(applySerialNumber)) && 
                    Boolean.TRUE.equals(checkApplySerialNumberDuplication(applySerialNumber))) {
                APPLY_ID_CACHE.put(applySerialNumber, applySerialNumber);
                return applySerialNumber;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                LOGGER.error("获取申请单号休眠异常:", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void checkAuditApplyCanHandle(AuditApplyAuditLogDTO auditApplyAuditLogDTO) throws BusinessException {
        Assert.notNull(auditApplyAuditLogDTO, "AuditApplyAuditLogDTO not be null");
        UUID applyId = auditApplyAuditLogDTO.getApplyId();
        Assert.notNull(applyId, "applyId not be null");
        UUID auditorId = auditApplyAuditLogDTO.getAuditorId();
        Assert.notNull(auditorId, "auditorId not be null");
        Assert.notNull(auditApplyAuditLogDTO.getAuditorState(), "auditorState not be null");

        AuditApplyEntity auditFileApply = auditApplyDAO.findById(applyId)
                .orElseThrow(() -> new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_NOT_EXIST, String.valueOf(applyId)));
        if (!AuditApplyStateEnum.PENDING_APPROVAL.equals(auditFileApply.getState())) {
            throw new BusinessException(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_STATE_NOT_PENDING_APPROVAL,
                    auditFileApply.getApplySerialNumber());
        }


        if (!AuditApplyAuditLogStateEnum.APPROVED.equals(auditApplyAuditLogDTO.getAuditorState())
                && !AuditApplyAuditLogStateEnum.REJECTED.equals(auditApplyAuditLogDTO.getAuditorState())) {
            throw new BusinessException(SecurityBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_AUDITOR_NOT_HANDLED_APPLY,
                    auditFileApply.getApplySerialNumber(), auditApplyAuditLogDTO.getAuditorName(),
                    String.valueOf(auditApplyAuditLogDTO.getAuditorState()));
        }

        AuditApplyAuditLogEntity auditApplyAuditLogEntity = null;
        // 1 查询是否存在本人待审批流程
        Optional<AuditApplyAuditLogEntity> auditLogEntityOptional = auditApplyAuditLogDAO.findByApplyIdAndAuditorId(applyId, auditorId);
        if (auditLogEntityOptional.isPresent()) {
            auditApplyAuditLogEntity = auditLogEntityOptional.get();
        } else {
            // 2 查询是否存在本角色待审批流程
            IacAdminDTO admin = baseAdminMgmtAPI.getAdmin(auditorId);
            if (admin != null) {
                for (UUID roleId : admin.getRoleIdArr()) {
                    Optional<AuditApplyAuditLogEntity> roleAuditLogEntityOptional = auditApplyAuditLogDAO.findByApplyIdAndRoleId(applyId, roleId);
                    if (roleAuditLogEntityOptional.isPresent()) {
                        auditApplyAuditLogEntity = roleAuditLogEntityOptional.get();
                        break;
                    }
                }
            }
            // 3 查询是否有空的待审批流程
            if (auditApplyAuditLogEntity == null) {
                List<AuditApplyAuditLogEntity> auditApplyAuditLogEntityList =
                        auditApplyAuditLogDAO.findByApplyIdAndAuditorState(applyId, AuditApplyAuditLogStateEnum.PENDING_APPROVAL);
                if (!auditApplyAuditLogEntityList.isEmpty()) {
                    auditApplyAuditLogEntity = auditApplyAuditLogEntityList.get(0);
                }
            }
        }

        if (auditApplyAuditLogEntity == null) {
            throw new BusinessException(SecurityBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_AUDITOR_LOG_NOT_EXIST, auditFileApply.getApplySerialNumber(),
                    auditApplyAuditLogDTO.getAuditorName());
        }

        if (!AuditApplyAuditLogStateEnum.PENDING_APPROVAL.equals(auditApplyAuditLogEntity.getAuditorState())) {
            throw new BusinessException(SecurityBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_AUDITOR_STATE_NOT_PENDING_APPROVAL,
                    auditFileApply.getApplySerialNumber(), auditApplyAuditLogDTO.getAuditorName());
        }

        auditApplyAuditLogDTO.setId(auditApplyAuditLogEntity.getId());
    }

    @Override
    public void autoApproveAuditFileApply(UUID applyId) throws BusinessException {
        Assert.notNull(applyId, "applyId is null");
        List<AuditApplyAuditLogEntity> auditApplyAuditLogEntityList = auditApplyAuditLogDAO.findByApplyId(applyId);
        if (!CollectionUtils.isEmpty(auditApplyAuditLogEntityList)) {
            for (AuditApplyAuditLogEntity auditApplyAuditLogEntity : auditApplyAuditLogEntityList) {
                if (auditApplyAuditLogEntity.getAuditorState() == null
                        || AuditApplyAuditLogStateEnum.PENDING_APPROVAL.equals(auditApplyAuditLogEntity.getAuditorState())) {
                    AuditApplyAuditLogDTO auditApplyAuditLogDTO = new AuditApplyAuditLogDTO();
                    BeanUtils.copyProperties(auditApplyAuditLogEntity, auditApplyAuditLogDTO);

                    auditApplyAuditLogDTO.setAuditorState(AuditApplyAuditLogStateEnum.APPROVED);
                    auditApplyAuditLogDTO.setAuditorOpinion(LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_AUTO_APPROVED));
                    auditApplyServiceTx.handleAuditApply(auditApplyAuditLogDTO);
                }
            }
        }
    }

    @Override
    public void checkAllAuditFileUploaded(UUID deskId, UUID applyId) throws BusinessException {
        Assert.notNull(deskId, "deskId not be null");
        Assert.notNull(applyId, "checkAllAuditFileUploaded applyId not be null");
        // 所有文件都上传完成，修改申请单状态为PENDING_APPROVAL或自动批复同意APPROVED
        List<AuditFileEntity> auditFileList = auditFileDAO.findByApplyId(applyId);

        if (!CollectionUtils.isEmpty(auditFileList)) {
            boolean isAllMatch = auditFileList.stream().allMatch(file -> AuditFileStateEnum.UPLOADED == file.getFileState());
            if (isAllMatch) {
                boolean enableAutoApprove = false;
                AuditApplyDetailDTO auditApplyDetail = this.findAuditApplyDetailById(applyId);
                if (AuditApplyTypeEnum.EXPORT == auditApplyDetail.getApplyType()) {
                    AuditFileStrategyDTO auditFileGlobalStrategyDTO = auditFileService.obtainAuditFileStrategy(deskId);
                    enableAutoApprove = BooleanUtils.isTrue(auditFileGlobalStrategyDTO.getEnableAutoApprove());
                } else if (AuditApplyTypeEnum.PRINT == auditApplyDetail.getApplyType()) {
                    AuditPrinterStrategyDTO auditPrinterGlobalStrategyDTO =
                            auditPrinterService.obtainAuditPrinterStrategy(deskId);
                    enableAutoApprove = BooleanUtils.isTrue(auditPrinterGlobalStrategyDTO.getEnableAutoApprovePrintApply());
                }
                try {
                    boolean enableAutoApproveFinal = enableAutoApprove;
                    LockableExecutor.executeWithTryLock(AuditFileConstants.AUDIT_FILE_APPLY_STATE_LOCK + applyId, () -> {
                        // 开启自动审批
                        if (enableAutoApproveFinal) {
                            // 找到所有待审批流程记录进行审批更新
                            autoApproveAuditFileApply(applyId);
                        } else {
                            updateAuditApplyState(applyId, AuditApplyStateEnum.PENDING_APPROVAL);
                        }
                        auditApplyUpdateNotifyService.notifyGuestToolAuditApplyDetail(findAuditApplyDetailById(applyId));
                    }, AuditFileConstants.AUDIT_FILE_APPLY_STATE_LOCK_TIMEOUT);
                } catch (BusinessException e) {
                    LOGGER.error("文件流转审计申请单ID[{}]关联所有文件上传完成，修改申请单状态出现异常：{}", applyId, ExceptionUtils.getStackTrace(e));
                    if (!StringUtils.equalsAnyIgnoreCase(e.getKey(), RESOURCE_LOCKED_ERROR_KEY,
                            AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_IS_FINISHED)) {
                        throw e;
                    }
                }
            }
        }
    }

    @Override
    public void updateAuditApplyState(UUID id, AuditApplyStateEnum stateEnum) throws BusinessException {
        Assert.notNull(id, "date is not null");
        Assert.notNull(stateEnum, "stateEnum is not null");
        AuditApplyEntity auditApplyEntity = auditApplyDAO.getOne(id);
        if (stateEnum == auditApplyEntity.getState()) {
            return;
        }
        // 判断申请单是否结束
        if (AuditApplyStateEnum.enableApplyFinish(auditApplyEntity.getState())) {
            throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_IS_FINISHED, auditApplyEntity.getState().getMessage());
        }
        auditApplyEntity.setState(stateEnum);
        auditApplyEntity.setUpdateTime(new Date());
        auditApplyDAO.save(auditApplyEntity);
    }

    @Override
    public AuditApplyDetailDTO findAuditApplyDetailById(UUID applyId) throws BusinessException {
        Assert.notNull(applyId, "applyId not be null");
        AuditApplyEntity auditFileApply = auditApplyDAO.findById(applyId)
                .orElseThrow(() -> new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_NOT_EXIST, String.valueOf(applyId)));

        AuditApplyDetailDTO auditApplyDetailDTO = new AuditApplyDetailDTO();
        BeanUtils.copyProperties(auditFileApply, auditApplyDetailDTO);

        List<AuditApplyAuditLogEntity> auditorList = auditApplyAuditLogDAO.findByApplyId(applyId);
        if (!CollectionUtils.isEmpty(auditorList)) {
            List<AuditApplyAuditLogDTO> auditApplyAuditLogDTOList = new ArrayList<>();
            for (AuditApplyAuditLogEntity auditApplyAuditLogEntity : auditorList) {
                AuditApplyAuditLogDTO auditApplyAuditLogDTO = new AuditApplyAuditLogDTO();
                BeanUtils.copyProperties(auditApplyAuditLogEntity, auditApplyAuditLogDTO);
                auditApplyAuditLogDTOList.add(auditApplyAuditLogDTO);
            }
            auditApplyDetailDTO.setAuditorList(auditApplyAuditLogDTOList);
        }

        List<AuditFileEntity> auditFileList = auditFileDAO.findByApplyId(applyId);
        if (!CollectionUtils.isEmpty(auditFileList)) {
            List<AuditFileDTO> auditFileDTOList = new ArrayList<>();
            for (AuditFileEntity auditFileEntity : auditFileList) {
                AuditFileDTO auditFileDTO = new AuditFileDTO();
                BeanUtils.copyProperties(auditFileEntity, auditFileDTO);
                auditFileDTOList.add(auditFileDTO);
            }
            auditApplyDetailDTO.setAuditFileList(auditFileDTOList);
        }

        return auditApplyDetailDTO;
    }

    @Override
    public AuditApplyDTO findAuditApplyById(UUID applyId) throws BusinessException {
        Assert.notNull(applyId, "findAuditApplyById applyId not be null");
        AuditApplyEntity auditFileApply = auditApplyDAO.findById(applyId)
                .orElseThrow(() -> new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_NOT_EXIST, String.valueOf(applyId)));

        AuditApplyDTO auditApplyDTO = new AuditApplyDTO();
        BeanUtils.copyProperties(auditFileApply, auditApplyDTO);
        return auditApplyDTO;
    }

    @Override
    public void cancelAuditApply(UUID applyId) throws BusinessException {
        Assert.notNull(applyId, "applyId not be null");

        AuditApplyEntity auditFileApply = auditApplyDAO.findById(applyId)
                .orElseThrow(() -> new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_NOT_EXIST, String.valueOf(applyId)));

        if (AuditApplyStateEnum.UPLOADING != auditFileApply.getState() && AuditApplyStateEnum.PENDING_APPROVAL != auditFileApply.getState()) {
            throw new BusinessException(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_STATE_NOT_CANCELLED);
        }

        // 删除文件，更新文件和申请单状态
        auditFileService.deleteAuditApplyFiles(applyId, () -> auditApplyServiceTx.cancelAuditApply(applyId));
    }

    @Override
    public void discardAuditApply(UUID applyId, String reason) throws BusinessException {
        Assert.notNull(applyId, "applyId not be null");
        Assert.hasText(reason, "reason not be null");
        // 删除文件，更新文件和申请单状态
        auditFileService.deleteAuditApplyFiles(applyId, () -> auditApplyServiceTx.discardAuditApply(applyId, reason));
    }

    @Override
    public void discardAuditApplyHandler(UUID applyId) throws BusinessException {
        Assert.notNull(applyId, "discardAuditApplyHandler applyId not be null");
        auditFileService.deleteAuditApplyFiles(applyId, () -> {
            // 更新状态
            AuditApplyEntity auditApplyEntity = auditApplyDAO.getOne(applyId);
            auditApplyEntity.setState(AuditApplyStateEnum.FAIL);
            auditApplyEntity.setUpdateTime(new Date());
            auditApplyDAO.save(auditApplyEntity);
        });
        LOGGER.info("申请单{}废弃-失败处理成功", applyId.toString());
    }

    @Override
    public void validateDiscardAuditApply(UUID applyId) throws BusinessException {
        Assert.notNull(applyId, "validateDiscardAuditApply applyId not be null");
        AuditApplyEntity auditFileApply = auditApplyDAO.findById(applyId)
                .orElseThrow(() -> new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_NOT_EXIST, String.valueOf(applyId)));
        if (!AuditApplyStateEnum.getExpendableStateList().contains(auditFileApply.getState())) {
            LOGGER.error("当前申请单{}的状态：{}，不支持进行废弃", applyId, auditFileApply.getState());
            throw new BusinessException(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_STATE_NOT_DISCARD, auditFileApply.getState().getMessage());
        }
    }

    @Override
    public List<AuditApplyDetailDTO> findAuditApplyListByUserIdDesktopId(UUID userId, UUID desktopId) {
        Assert.notNull(userId, "userId not be null");
        Assert.notNull(desktopId, "desktopId not be null");

        List<AuditApplyEntity> applyEntityList = auditApplyDAO.findByUserIdAndDesktopIdOrderByCreateTimeDesc(userId, desktopId);
        List<AuditApplyDetailDTO> auditApplyDetailList = new ArrayList<>();
        applyEntityList.forEach(auditApplyEntity -> {
            AuditApplyDetailDTO auditApplyDetailDTO = new AuditApplyDetailDTO();
            BeanUtils.copyProperties(auditApplyEntity, auditApplyDetailDTO);
            auditApplyDetailList.add(auditApplyDetailDTO);
        });
        return auditApplyDetailList;
    }

    @Override
    public List<AuditApplyDetailDTO> findAuditApplyDetailListByUpdateTimeLessThan(Date updateTime) throws BusinessException {
        Assert.notNull(updateTime, "updateTime not be null");

        List<AuditApplyEntity> applyEntityList = auditApplyDAO.findByUpdateTimeLessThan(updateTime);
        return getAuditFileApplyDetail(applyEntityList);
    }

    @Override
    public List<AuditApplyDetailDTO> findAuditApplyDetailListByState(AuditApplyStateEnum auditFileApplyState) throws BusinessException {
        Assert.notNull(auditFileApplyState, "auditFileApplyState not be null");

        List<AuditApplyEntity> applyEntityList = auditApplyDAO.findByState(auditFileApplyState);
        return getAuditFileApplyDetail(applyEntityList);
    }

    @Override
    public List<AuditApplyDetailDTO> findAuditApplyListByStateIn(List<AuditApplyStateEnum> auditFileApplyStateList) throws BusinessException {
        Assert.notNull(auditFileApplyStateList, "auditFileApplyStateList not be null");
        Assert.state(!CollectionUtils.isEmpty(auditFileApplyStateList), "auditFileApplyStateList size > 0");
        List<AuditApplyEntity> applyEntityList = auditApplyDAO.findByStateIn(auditFileApplyStateList);
        return getAuditFileApplyDetail(applyEntityList);
    }

    @Override
    public PageQueryResponse<AuditApplyDTO> pageQueryForExport(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "pageQueryRequest not be null");
        PageQueryResponse<AuditApplyEntity> pageQuery = auditApplyDAO.pageQuery(pageQueryRequest);
        PageQueryResponse<AuditApplyDTO> response = new PageQueryResponse<>();
        response.setTotal(pageQuery.getTotal());
        if (ArrayUtils.isEmpty(pageQuery.getItemArr())) {
            response.setItemArr(new AuditApplyDTO[0]);
            return response;
        }
        response.setItemArr(Arrays.stream(pageQuery.getItemArr()).map(item -> {
            AuditApplyDTO auditApplyDTO = new AuditApplyDTO();
            BeanUtils.copyProperties(item, auditApplyDTO);
            return auditApplyDTO;
        }).toArray(AuditApplyDTO[]::new));
        return response;
    }

    @Override
    public void updateAuditApplyAlarmIds(UUID applyId, String alarmIds) {
        Assert.notNull(applyId, "applyId is not null");
        Assert.notNull(alarmIds, "alarmIds is not null");
        AuditApplyEntity auditApplyEntity = auditApplyDAO.getOne(applyId);
        auditApplyEntity.setAlarmIds(alarmIds);
        auditApplyEntity.setUpdateTime(new Date());
        auditApplyDAO.save(auditApplyEntity);
    }

    private List<AuditApplyDetailDTO> getAuditFileApplyDetail(List<AuditApplyEntity> applyEntityList) throws BusinessException {
        if (CollectionUtils.isEmpty(applyEntityList)) {
            return Collections.emptyList();
        }

        List<AuditApplyDetailDTO> auditFileApplyDetailList = new ArrayList<>();
        for (AuditApplyEntity auditApplyEntity : applyEntityList) {
            AuditApplyDetailDTO auditFileApplyDetail = findAuditApplyDetailById(auditApplyEntity.getId());
            auditFileApplyDetailList.add(auditFileApplyDetail);
        }

        return auditFileApplyDetailList;
    }

    private Boolean checkApplySerialNumberDuplication(String applySerialNumber) {
        return auditApplyDAO.findByApplySerialNumber(applySerialNumber) == null;
    }
}
