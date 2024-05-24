package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.sm;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.cache.AuditFileUploadedCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.AuditApplyService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.AuditApplyUpdateNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * Description: 废弃操作
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/25
 *
 * @author TD
 */
@Service
public class AuditApplyDiscardProcessor implements StateTaskHandle.StateProcessor {

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private AuditApplyUpdateNotifyService auditApplyUpdateNotifyService;

    @Autowired
    private AuditApplyService auditApplyService;
    
    @Autowired
    private AuditFileUploadedCacheManager auditFileUploadedCacheManager;

    @Autowired
    private DesktopPoolUserService desktopPoolUserService;
    
    @Override
    public StateTaskHandle.ProcessResult doProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context can not be null");
        UUID applyId = AuditApplyContextResolver.resolveApplyId(context);
        AuditApplyDetailDTO applyDetailDTO = auditApplyService.findAuditApplyDetailById(applyId);
        String failReason = AuditApplyContextResolver.resolveFailReason(context);
        // 先取消指定申请单文件上传操作
        auditFileUploadedCacheManager.cancelApplyIdFileUploaded(applyId);
        // 桌面不在线或者申请单为废弃、待审批、拒绝、已审批、已撤回状态直接清理数据
        if (hasDiscardSuccess(applyDetailDTO)) {
            auditApplyService.discardAuditApply(applyId, failReason);
            return StateTaskHandle.ProcessResult.next();
        }
        // 累计时长超过1小时直接失败
        if (context.getRetryTotalMills() > AuditApplyContextResolver.DEFAULT_MAX_RETRY) {
            throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_DISCARD_MAX_OVERTIME);
        }
        // 发送消息通知
        applyDetailDTO.setFailReason(failReason);
        applyDetailDTO.setState(AuditApplyStateEnum.FAIL);
        auditApplyUpdateNotifyService.notifyGuestToolAuditApplyDetail(applyDetailDTO);
        return StateTaskHandle.ProcessResult.retry(AuditApplyContextResolver.DO_WAITING_RETRY);
    }
    
    private boolean hasDiscardSuccess(AuditApplyDetailDTO applyDetailDTO) {
        AuditApplyStateEnum applyState = applyDetailDTO.getState();
        if (applyDetailDTO.getDesktopPoolType() == DesktopPoolType.DYNAMIC) {
            UUID useDesktopId = desktopPoolUserService.findDeskRunningByDesktopPoolIdAndUserId(
                    applyDetailDTO.getDesktopPoolId(), applyDetailDTO.getUserId());
            if (Objects.isNull(useDesktopId)) {
                return true;
            }
        } else {
            try {
                ViewUserDesktopEntity userDesktopEntity = queryCloudDesktopService.checkDesktopExistInDeskViewById(applyDetailDTO.getDesktopId());
                // 申请单所属用户与桌面所属用户不一致或桌面不在线则直接废弃申请单
                if (!Objects.equals(userDesktopEntity.getUserId(), applyDetailDTO.getUserId())
                        || !Objects.equals(CbbCloudDeskState.RUNNING.name(), userDesktopEntity.getDeskState())) {
                    return true;
                }
            } catch (BusinessException e) {
                // 云桌面不存在也直接废弃申请单
                return true;
            }
        }
        return AuditApplyStateEnum.REJECTED == applyState 
                || AuditApplyStateEnum.FAIL == applyState 
                || AuditApplyStateEnum.APPROVED == applyState 
                || AuditApplyStateEnum.CANCELED == applyState 
                || AuditApplyStateEnum.PENDING_APPROVAL == applyState;
    }
}
