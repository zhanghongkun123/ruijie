package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;

/**
 * Description: 文件流转审计策略发生变更通知桌面
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/26
 *
 * @author lihengjing
 */

public interface AuditApplyUpdateNotifyService {

    /**
     * 通知文件流转审计申请单详情（通知上传、通知审批结果）
     * @param auditApplyDetailDTO 文件流转审计申请单详情
     */
    void notifyGuestToolAuditApplyDetail(AuditApplyDetailDTO auditApplyDetailDTO);
}
