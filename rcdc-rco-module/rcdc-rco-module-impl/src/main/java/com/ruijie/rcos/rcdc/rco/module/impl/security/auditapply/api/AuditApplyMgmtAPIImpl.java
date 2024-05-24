package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.response.QueryAlarmResponse;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditApplyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyAuditLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ViewAuditApplyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.constants.AuditFileConstants;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.constants.AuditPrinterConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.AuditApplyService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.AuditApplyUpdateNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.sm.AuditApplyContextResolver;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.sm.AuditApplyDiscardHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.tx.AuditApplyServiceTx;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.AuditFileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.service.AuditFileService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.validation.AuditFileMgmtAPIParamValidator;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service.AuditPrinterService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.validation.AuditPrinterMgmtAPIParamValidator;
import com.ruijie.rcos.rcdc.rco.module.impl.security.common.service.SecurityService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 文件流转审计功能管理实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public class AuditApplyMgmtAPIImpl implements AuditApplyMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditApplyMgmtAPIImpl.class);

    @Autowired
    private AuditFileMgmtAPIParamValidator auditFileMgmtAPIParamValidator;

    @Autowired
    private AuditPrinterMgmtAPIParamValidator auditPrinterMgmtAPIParamValidator;

    @Autowired
    private AuditApplyServiceTx auditApplyServiceTx;

    @Autowired
    private AuditApplyService auditApplyService;

    @Autowired
    private AuditFileService auditFileService;

    @Autowired
    private AuditApplyUpdateNotifyService auditApplyUpdateNotifyService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private AuditPrinterService auditPrinterService;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private BaseAlarmAPI baseAlarmAPI;

    @Override
    public AuditApplyDetailDTO createAuditApply(UUID deskId, AuditApplyDetailDTO auditApplyDetailDTO) throws BusinessException {
        Assert.notNull(deskId, "deskId is null");
        Assert.notNull(auditApplyDetailDTO, "auditApplyDetailDTO must not be null");
        LOGGER.info("(安全审计申请请求对象) auditApplyDetailDTO:", JSON.toJSONString(auditApplyDetailDTO));
        
        // 补充申请人信息
        fillDeskInfo(deskId, auditApplyDetailDTO);
        // 补充完善申请单参数
        fillAuditFileApplyDTO(auditApplyDetailDTO);

        switch (auditApplyDetailDTO.getApplyType()) {
            case EXPORT:
                // 检查是否开启文件流转审计功能
                auditFileService.checkEnableAuditFile(deskId);
                // 校验参数
                auditFileMgmtAPIParamValidator.validateCreateAuditFileApply(auditApplyDetailDTO);
                break;
            case PRINT:
                // 是否开启安全打印机
                auditPrinterService.checkEnableAuditPrinter(deskId);
                // 校验参数
                auditPrinterMgmtAPIParamValidator.validateCreateAuditPrintApply(auditApplyDetailDTO);
                break;
            default:
                throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_TYPE_ILLEGAL);
        }

        // 补充审批流程信息
        List<AuditApplyAuditLogDTO> auditLogList = securityService.generateAuditLogByApply(auditApplyDetailDTO);
        auditApplyDetailDTO.setAuditorList(auditLogList);

        // 检查是否产生告警（非可疑行为告警直接拒绝工单）
        auditFileService.checkAuditFileApplyAlarm(auditApplyDetailDTO);
        auditFileService.fillAuditFileApplyDetailDTOAllState(auditApplyDetailDTO);

        // 事务创建申请单、申请关联文件、申请关联审批流程
        auditApplyServiceTx.createAuditApply(auditApplyDetailDTO);

        LOGGER.info("(安全审计申请请求对象) auditFileApplyDetailDTO：{}", JSON.toJSONString(auditApplyDetailDTO));
        return auditApplyDetailDTO;
    }


    private void fillDeskInfo(UUID deskId, AuditApplyDetailDTO auditApplyDetailDTO) throws BusinessException {
        CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        if (cloudDesktopDetailDTO != null) {
            auditApplyDetailDTO.setDesktopId(cloudDesktopDetailDTO.getId());
            auditApplyDetailDTO.setDesktopName(cloudDesktopDetailDTO.getDesktopName());
            auditApplyDetailDTO.setDesktopIp(cloudDesktopDetailDTO.getDesktopIp());
            auditApplyDetailDTO.setDesktopMac(cloudDesktopDetailDTO.getDesktopMac());
            auditApplyDetailDTO.setDesktopPoolType(DesktopPoolType.valueOf(cloudDesktopDetailDTO.getDesktopPoolType()));
            auditApplyDetailDTO.setDesktopPoolId(cloudDesktopDetailDTO.getDesktopPoolId());

            auditApplyDetailDTO.setUserId(cloudDesktopDetailDTO.getUserId());
            auditApplyDetailDTO.setUserName(cloudDesktopDetailDTO.getUserName());

            auditApplyDetailDTO.setTerminalId(cloudDesktopDetailDTO.getTerminalId());
            auditApplyDetailDTO.setTerminalName(cloudDesktopDetailDTO.getTerminalName());
            auditApplyDetailDTO.setTerminalIp(cloudDesktopDetailDTO.getTerminalIp());
            auditApplyDetailDTO.setTerminalMac(cloudDesktopDetailDTO.getTerminalMac());
            if (cloudDesktopDetailDTO.getTerminalId() != null) {
                // 设置终端类型
                ViewTerminalEntity terminal = terminalService.getViewByTerminalId(cloudDesktopDetailDTO.getTerminalId());
                if (terminal != null && terminal.getPlatform() != null && terminal.getTerminalOsType() != null) {
                    auditApplyDetailDTO.setTerminalType(CbbTerminalTypeEnums.convert(terminal.getPlatform().name(), terminal.getTerminalOsType()));
                }
            }
        }
    }


    private void fillAuditFileApplyDTO(AuditApplyDetailDTO auditApplyDetailDTO) {
        auditApplyDetailDTO.setId(UUID.randomUUID());
        String applySerialNumber = auditApplyService.generateApplySerialNumber();
        auditApplyDetailDTO.setApplySerialNumber(applySerialNumber);
        auditApplyDetailDTO.setCreateTime(new Date());
        auditApplyDetailDTO.setUpdateTime(new Date());
        if (auditApplyDetailDTO.getApplyType() == null) {
            auditApplyDetailDTO.setApplyType(AuditApplyTypeEnum.EXPORT);
        }
    }

    @Override
    public String handleAuditApplyByAuditor(AuditApplyAuditLogDTO auditApplyAuditLogDTO) throws BusinessException {
        Assert.notNull(auditApplyAuditLogDTO, "AuditApplyAuditLogDTO must not be null");
        UUID applyId = auditApplyAuditLogDTO.getApplyId();
        AuditApplyDetailDTO auditFileApplyDetail = auditApplyService.findAuditApplyDetailById(applyId);

        LOGGER.info("安全审计[{}]申请审批请求对象 AuditApplyAuditLogDTO：{}", auditFileApplyDetail.getApplyType(),
                JSON.toJSONString(auditApplyAuditLogDTO));
        // 检查申请单是否允许处理
        auditApplyService.checkAuditApplyCanHandle(auditApplyAuditLogDTO);

        // 处理申请单
        LockableExecutor.executeWithTryLock(AuditFileConstants.AUDIT_FILE_APPLY_STATE_LOCK + applyId, 
            () -> auditApplyServiceTx.handleAuditApply(auditApplyAuditLogDTO), 
            AuditFileConstants.AUDIT_FILE_APPLY_STATE_LOCK_TIMEOUT);

        // 通知申请单审批结果
        AuditApplyDetailDTO auditApplyDetailDTO = auditApplyService.findAuditApplyDetailById(applyId);
        if (AuditApplyTypeEnum.EXPORT == auditApplyDetailDTO.getApplyType()) {
            auditApplyUpdateNotifyService.notifyGuestToolAuditApplyDetail(auditApplyDetailDTO);
        }
        return auditApplyDetailDTO.getApplySerialNumber();
    }

    @Override
    public AuditApplyDetailDTO findAuditApplyDetailById(UUID applyId) throws BusinessException {
        Assert.notNull(applyId, "applyId must not be null");
        return auditApplyService.findAuditApplyDetailById(applyId);
    }

    @Override
    public void cancelAuditFileApply(UUID deskId, UUID applyId) throws BusinessException {
        Assert.notNull(applyId, "cancelAuditFileApply applyId must not be null");
        Assert.notNull(deskId, "cancelAuditFileApply deskId must not be null");

        LOGGER.debug("(撤回文件流转审计申请) 桌面[{}]撤回申请单UUID：{}", deskId, applyId);
        auditFileService.checkEnableAuditFile(deskId);

        auditApplyService.cancelAuditApply(applyId);
    }

    @Override
    public void discardAuditApply(UUID applyId, String reason) throws BusinessException {
        Assert.notNull(applyId, "applyId not be null");
        Assert.hasText(reason, "reason not be null");
        stateMachineFactory.newBuilder(UUID.randomUUID(), AuditApplyDiscardHandler.class)
                .initArg(AuditApplyContextResolver.APPLY_ID, applyId)
                .initArg(AuditApplyContextResolver.FAIL_REASON, reason)
                .lockResources(applyId.toString())
                .start().waitForAllProcessFinish();
    }

    @Override
    public List<AuditApplyDetailDTO> findAuditFileApplyByUserIdDesktopId(UUID userId, UUID desktopId) {
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(desktopId, "desktopId must not be null");
        return auditApplyService.findAuditApplyListByUserIdDesktopId(userId, desktopId);
    }

    @Override
    public PageQueryResponse<ViewAuditApplyDTO> auditApplyPageQuery(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        return auditApplyService.pageQuery(request);
    }

    @Override
    public void handleAuditFileApplyUploaded(UUID deskId, UUID applyId, UUID fileId) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");
        Assert.notNull(applyId, "applyId is not null");
        Assert.notNull(fileId, "fileId is not null");
        LOGGER.info("(处理上传文件完成状态) 申请单[{}]，文件ID[{}]", applyId, fileId);

        AuditApplyDetailDTO auditFileApplyDetail = auditApplyService.findAuditApplyDetailById(applyId);
        // 申请单处于处理中状态，忽略对文件上传的处理
        if (AuditApplyStateEnum.HANDLING == auditFileApplyDetail.getState()) {
            LOGGER.info("申请单[{}]状态为：处理中，忽略对文件[{}]上传完成的处理", applyId, fileId);
            return;
        }
        boolean isPrint = AuditApplyTypeEnum.PRINT == auditFileApplyDetail.getApplyType();
        if (isPrint) {
            // 检查是否开启安全打印机功能
            auditPrinterService.checkEnableAuditPrinter(deskId);
        } else {
            // 检查是否开启文件流转审计功能
            auditFileService.checkEnableAuditFile(deskId);
        }

        try {
            auditFileService.handleUploadedByFileId(fileId, auditFileApplyDetail.getCreateTime());
        } catch (BusinessException e) {
            if (AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_STORAGE_SPACE_NOT_ENOUGH.equals(e.getKey())) {
                auditFileService.saveFileStorageSpaceNotEnoughAlarm(auditFileApplyDetail.getApplySerialNumber());
            }
            AuditApplyDetailDTO applyDetailDTO = auditApplyService.findAuditApplyDetailById(applyId);
            if (applyDetailDTO.getState() == AuditApplyStateEnum.FAIL) {
                // 通知GT失败
                auditApplyUpdateNotifyService.notifyGuestToolAuditApplyDetail(applyDetailDTO);
            }
            throw e;
        } catch (InterruptedException ex) {
            LOGGER.error("发生强制中断线程！", ex);
            //业务由中断任务处理
            Thread.currentThread().interrupt();
            return;
        }
        // 检查申请单所有文件是否都上传完毕
        auditApplyService.checkAllAuditFileUploaded(deskId, applyId);
    }

    @Override
    public void applyRelease(UUID alarmId, @Nullable String businessId) throws BusinessException {
        Assert.notNull(alarmId, "applyBatchRelease alarmId can be null");
        QueryAlarmResponse alarmResponse = baseAlarmAPI.queryAlarm(alarmId);
        String alarmTypeName = alarmResponse.getAlarmType();
        if (Objects.isNull(businessId) || (!Objects.equals(alarmTypeName, AuditFileConstants.AUDIT_FILE_ALARM_TYPE) 
                && !Objects.equals(alarmTypeName, AuditPrinterConstants.AUDIT_PRINT_ALARM_TYPE))) {
            return;
        }
        AuditApplyDTO auditApplyDTO = auditApplyService.findAuditApplyById(UUID.fromString(businessId));
        String alarmIds = auditApplyDTO.getAlarmIds();
        if (StringUtils.isBlank(alarmIds) || !alarmIds.contains(alarmId.toString())) {
            return;
        }
        Set<String> alarmIdSet = Arrays.stream(alarmIds.split(AuditFileConstants.APPLY_SEPARATOR))
                .filter(tempAlarmId -> !StringUtils.equals(tempAlarmId, alarmId.toString())).collect(Collectors.toSet());
        alarmIds = StringUtils.join(alarmIdSet, AuditFileConstants.APPLY_SEPARATOR);
        auditApplyService.updateAuditApplyAlarmIds(auditApplyDTO.getId(), alarmIds);
    }

}
