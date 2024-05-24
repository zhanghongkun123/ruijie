package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.quartz;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.sysmanage.module.def.api.MaintenanceModeMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.enums.SystemMaintenanceState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.AuditApplyService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.AuditApplyUpdateNotifyService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 定时2分钟扫描状态为UPLOADING的申请单，将申请单详情发送给客户端（GT）,要求上传文件
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/27
 *
 * @author WuShengQiang
 */
@Service
@Quartz(scheduleTypeCode = "audit_apply_uploading", scheduleName = AuditApplyBusinessKey.RCDC_RCO_QUARTZ_AUDIT_APPLY_UPLOADING,
        cron = "0 0/2 * * * ?")
public class AuditApplyUploadingQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditApplyUploadingQuartzTask.class);

    @Autowired
    private MaintenanceModeMgmtAPI maintenanceModeMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private AuditApplyService auditApplyService;

    @Autowired
    private AuditApplyUpdateNotifyService auditApplyUpdateNotifyService;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;
    
    @Autowired
    private DesktopPoolUserService desktopPoolUserService;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        SystemMaintenanceState maintenanceMode = maintenanceModeMgmtAPI.getMaintenanceMode();
        if (maintenanceMode != SystemMaintenanceState.NORMAL) {
            LOGGER.info("定时任务扫描状态为UPLOADING的申请单不执行,当前处于维护模式:{}", maintenanceMode);
            return;
        }

        // 1.查询uploading状态的申请单
        List<AuditApplyDetailDTO> applyDetailDTOList = auditApplyService.findAuditApplyDetailListByState(AuditApplyStateEnum.UPLOADING);
        if (CollectionUtils.isEmpty(applyDetailDTOList)) {
            return;
        }

        // 2.补偿更新异常未流转状态申请单
        for (AuditApplyDetailDTO auditApplyDetailDTO : applyDetailDTOList) {
            auditApplyService.checkAllAuditFileUploaded(auditApplyDetailDTO.getDesktopId(), auditApplyDetailDTO.getId());
        }

        // 3.重新查询uploading状态的申请单
        applyDetailDTOList = auditApplyService.findAuditApplyDetailListByState(AuditApplyStateEnum.UPLOADING);
        if (CollectionUtils.isEmpty(applyDetailDTOList)) {
            return;
        }
        // 4.适配动态池桌面的申请单：动态池桌面申请单支持用户在桌面A进行申请，在池中其它桌面进行导出，
        //  如果池桌面存在申请单，应该通知申请单所绑定用户在动态池中正在使用的桌面，不存在使用中的桌面则不进行通知
        List<AuditApplyDetailDTO> dynamicPoolDesktopApplyList = applyDetailDTOList.stream()
                .filter(auditApplyDetailDTO -> auditApplyDetailDTO.getDesktopPoolType() == DesktopPoolType.DYNAMIC)
                .filter(auditApplyDetailDTO -> {
                    UUID useDesktopId = desktopPoolUserService.findDeskRunningByDesktopPoolIdAndUserId
                            (auditApplyDetailDTO.getDesktopPoolId(), auditApplyDetailDTO.getUserId());
                    auditApplyDetailDTO.setDesktopId(useDesktopId);
                    return Objects.nonNull(useDesktopId);
                }).collect(Collectors.toList());
        // 5.提取非动态池桌面的申请单，并合并动态池桌面的申请单
        applyDetailDTOList = applyDetailDTOList.stream()
                .filter(auditApplyDetailDTO -> auditApplyDetailDTO.getDesktopPoolType() != DesktopPoolType.DYNAMIC).collect(Collectors.toList());
        applyDetailDTOList.addAll(dynamicPoolDesktopApplyList);
        if (CollectionUtils.isEmpty(applyDetailDTOList)) {
            return;
        }
        // 6.查询运行中的桌面
        Set<UUID> deskIdSet = applyDetailDTOList.stream().map(AuditApplyDetailDTO::getDesktopId).collect(Collectors.toSet());
        List<UUID> deskIdList = cbbDeskMgmtAPI.listDesktopByDeskState(CbbCloudDeskState.RUNNING, new ArrayList<>(deskIdSet));
        if (CollectionUtils.isEmpty(deskIdList)) {
            LOGGER.info("定时任务扫描状态为UPLOADING的申请单提前结束,没有运行中的桌面,待上传的桌面ID列表:{}", JSON.toJSONString(deskIdSet));
            return;
        }

        applyDetailDTOList = applyDetailDTOList.stream()
                .filter(auditFileApplyDetailDTO -> deskIdList.contains(auditFileApplyDetailDTO.getDesktopId())).collect(Collectors.toList());

        // 7.查询所有云桌面策略,判断是否开启审计
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


        // 8.通知发送给客户端（GT）
        for (AuditApplyDetailDTO auditApplyDetailDTO : applyDetailDTOList) {
            CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(auditApplyDetailDTO.getDesktopId());
            CbbDeskStrategyDTO strategyDTO = strategyMap.get(deskDTO.getStrategyId());

            // 只有开启安全打印、文件流转审计功能 才进行申请单重传通知
            if (Objects.isNull(strategyDTO) ||
                    (AuditApplyTypeEnum.EXPORT == auditApplyDetailDTO.getApplyType() && BooleanUtils.isFalse(strategyDTO.getEnableAuditFile())) ||
                    (AuditApplyTypeEnum.PRINT == auditApplyDetailDTO.getApplyType() && BooleanUtils.isFalse(strategyDTO.getEnableAuditPrinter()))) {
                continue;
            }

            auditApplyUpdateNotifyService.notifyGuestToolAuditApplyDetail(auditApplyDetailDTO);
        }
    }
}
