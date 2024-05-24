package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.spi.request;

import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.enums.AuditApplyNotifyActionEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 文件流转审计申请单上报变更状态请求体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/28
 *
 * @author WuShengQiang
 */
public class AuditApplyClientNotifyRequest {

    /**
     * GT请求ID
     */
    @NotNull
    private UUID requestId;

    /**
     * 申请单ID
     **/
    @NotNull
    private UUID applyId;

    /**
     * 文件记录ID
     **/
    @Nullable
    private UUID fileId;

    /**
     * 失败原因
     **/
    @Nullable
    private String failReason;

    /**
     * 申请单状态
     */
    private AuditApplyNotifyActionEnum action;

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public UUID getApplyId() {
        return applyId;
    }

    public void setApplyId(UUID applyId) {
        this.applyId = applyId;
    }

    @Nullable
    public UUID getFileId() {
        return this.fileId;
    }

    public void setFileId(@Nullable UUID fileId) {
        this.fileId = fileId;
    }

    @Nullable
    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(@Nullable String failReason) {
        this.failReason = failReason;
    }

    public AuditApplyNotifyActionEnum getAction() {
        return action;
    }

    public void setAction(AuditApplyNotifyActionEnum action) {
        this.action = action;
    }
}
