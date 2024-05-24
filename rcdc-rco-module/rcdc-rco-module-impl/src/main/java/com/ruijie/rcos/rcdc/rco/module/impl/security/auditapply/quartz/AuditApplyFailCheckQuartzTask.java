package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.quartz;

import com.ruijie.rcos.base.sysmanage.module.def.api.MaintenanceModeMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.enums.SystemMaintenanceState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.AuditApplyService;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 定时10分钟扫描状态为UPLOADING、COMPUTING、PENDING_APPROVAL的申请单，
 * 将申请单对应云桌面删除、策略删除、未开启文件流转审计功能、未开启安全打印的申请单置为失败。
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/05/22
 *
 * @author TD
 */
@Service
@Quartz(scheduleTypeCode = "audit_apply_fail_check", scheduleName = AuditApplyBusinessKey.RCDC_RCO_QUARTZ_AUDIT_APPLY_FAIL_CHECK,
        cron = "0 0/10 * * * ?")
public class AuditApplyFailCheckQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditApplyFailCheckQuartzTask.class);

    @Autowired
    private MaintenanceModeMgmtAPI maintenanceModeMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private AuditApplyService auditApplyService;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;
    
    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        SystemMaintenanceState maintenanceMode = maintenanceModeMgmtAPI.getMaintenanceMode();
        if (maintenanceMode != SystemMaintenanceState.NORMAL) {
            LOGGER.info("定时任务扫描状态为UPLOADING的申请单不执行,当前处于维护模式:{}", maintenanceMode);
            return;
        }

        // 1.查询UPLOADING、COMPUTING、PENDING_APPROVAL状态的申请单
        List<AuditApplyDetailDTO> applyDetailDTOList = auditApplyService.findAuditApplyListByStateIn(AuditApplyStateEnum.getNotCompleteStateList());
        if (CollectionUtils.isEmpty(applyDetailDTOList)) {
            return;
        }

        // 2.查询对应申请单的桌面信息
        List<UUID> deskIdList = applyDetailDTOList.stream().map(AuditApplyDetailDTO::getDesktopId).collect(Collectors.toList());
        Map<UUID, CbbDeskInfoDTO> existDeskMap = cbbVDIDeskMgmtAPI.queryByDeskIdList(deskIdList).stream()
                .collect(Collectors.toMap(CbbDeskInfoDTO::getDeskId, cbbDeskInfoDTO -> cbbDeskInfoDTO));

        // 查询所有云桌面策略,判断是否开启审计
        List<CbbDeskStrategyDTO> deskStrategyList;
        try {
            deskStrategyList = cbbVDIDeskStrategyMgmtAPI.listDeskStrategyVDI();
        } catch (Exception e) {
            LOGGER.error("获取策略列表失败", e);
            return;
        }

        if (CollectionUtils.isEmpty(deskStrategyList)) {
            return;
        }

        Map<UUID, CbbDeskStrategyDTO> strategyMap = deskStrategyList.stream().collect(Collectors.toMap(CbbDeskStrategyDTO::getId, dto -> dto));

        // 3.判断是否终止申请单
        for (AuditApplyDetailDTO auditApplyDetailDTO : applyDetailDTOList) {
            CbbDeskInfoDTO deskDTO = existDeskMap.get(auditApplyDetailDTO.getDesktopId());
            // 3.1桌面信息为空代表申请单所绑定的桌面已经被删除
            if (Objects.isNull(deskDTO)) {
                UUID desktopPoolId = auditApplyDetailDTO.getDesktopPoolId();
                // 非动态池申请单查询的桌面信息为空，直接废弃申请单
                if (auditApplyDetailDTO.getDesktopPoolType() != DesktopPoolType.DYNAMIC || Objects.isNull(desktopPoolId)) {
                    // 直接终止申请单
                    auditApplyService.discardAuditApply(auditApplyDetailDTO.getId(),
                            LocaleI18nResolver.resolve(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_BIND_DESKTOP_DELETED));
                    continue;
                }
                // 动态池申请单查询的池信息为空，直接废弃申请单
                if (StringUtils.isEmpty(cbbDesktopPoolMgmtAPI.getDesktopPoolName(desktopPoolId))) {
                    // 直接终止申请单
                    auditApplyService.discardAuditApply(auditApplyDetailDTO.getId(),
                            LocaleI18nResolver.resolve(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_BIND_DESKTOP_POOL_DELETED));
                }
                continue;
            }
            CbbDeskStrategyDTO strategyDTO = strategyMap.get(deskDTO.getStrategyId());
            // 3.2策略为空，需要系统自动终止申请单
            if (Objects.isNull(strategyDTO)) {
                auditApplyService.discardAuditApply(auditApplyDetailDTO.getId(),
                        LocaleI18nResolver.resolve(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_BIND_DESKTOP_STRATEGY_DELETED));
                continue;
            }
            // 3.3未开启文件流转审计功能，需要系统自动终止申请单
            if (AuditApplyTypeEnum.EXPORT == auditApplyDetailDTO.getApplyType() && BooleanUtils.isFalse(strategyDTO.getEnableAuditFile())) {
                auditApplyService.discardAuditApply(auditApplyDetailDTO.getId(),
                        LocaleI18nResolver.resolve(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_DESKTOP_STRATEGY_UPDATE_DISABLE));
                continue;
            }
            // 3.4未开启安全打印，需要系统自动终止申请单
            if (AuditApplyTypeEnum.PRINT == auditApplyDetailDTO.getApplyType() && BooleanUtils.isFalse(strategyDTO.getEnableAuditPrinter())) {
                auditApplyService.discardAuditApply(auditApplyDetailDTO.getId(),
                        LocaleI18nResolver.resolve(AuditApplyBusinessKey.RCDC_RCO_AUDIT_PRINTER_APPLY_DESKTOP_STRATEGY_UPDATE_DISABLE));
            }
        }
    }
}
