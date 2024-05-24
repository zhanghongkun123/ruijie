package com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.common.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyAuditLogStateEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
public class HandleApplyRequest extends IdArrWebRequest {

    /**
     * 审批结果
     */
    @ApiModelProperty(value = "申请单审批结果", required = true)
    @NotNull
    private AuditApplyAuditLogStateEnum auditorState;

    /**
     * 审批意见
     */
    @ApiModelProperty(value = "审批意见", required = true)
    @Nullable
    private String auditorOpinion;

    public AuditApplyAuditLogStateEnum getAuditorState() {
        return this.auditorState;
    }

    public void setAuditorState(AuditApplyAuditLogStateEnum auditorState) {
        this.auditorState = auditorState;
    }

    @Nullable
    public String getAuditorOpinion() {
        return this.auditorOpinion;
    }

    public void setAuditorOpinion(@Nullable String auditorOpinion) {
        this.auditorOpinion = auditorOpinion;
    }
}
