package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.sm;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.AuditApplyService;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * Description: 废弃申请单初始操作
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/25
 *
 * @author TD
 */
@Service
public class AuditApplyDiscardInitProcessor implements StateTaskHandle.StateProcessor {

    @Autowired
    private AuditApplyService auditApplyService;
    
    @Override
    public StateTaskHandle.ProcessResult doProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context can be not null");
        UUID applyId = AuditApplyContextResolver.resolveApplyId(context);
        AuditApplyDTO auditApplyDTO = auditApplyService.findAuditApplyById(applyId);
        // 校验申请单是否支持废弃
        auditApplyService.validateDiscardAuditApply(applyId);
        context.putIfAbscent(AuditApplyContextResolver.OLD_APPLY_STATE, auditApplyDTO.getState());
        auditApplyService.updateAuditApplyState(applyId, AuditApplyStateEnum.HANDLING);
        return StateTaskHandle.ProcessResult.next();
    }

    @Override
    public StateTaskHandle.ProcessResult undoProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context can not be null");
        AuditApplyStateEnum applyState = context.get(AuditApplyContextResolver.OLD_APPLY_STATE, AuditApplyStateEnum.class);
        if (Objects.nonNull(applyState)) {
            UUID applyId = AuditApplyContextResolver.resolveApplyId(context);
            auditApplyService.updateAuditApplyState(applyId, applyState);
        }
        return StateTaskHandle.ProcessResult.next();
    }
}
