package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.request;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareControlMgmtAPI;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/18
 *
 * @author lihengjing
 */
public class ImportSoftwareBatchTaskHandlerRequest {

    @NotNull
    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    private UUID targetGroupId;

    public Iterator<? extends BatchTaskItem> getBatchTaskItemIterator() {
        return batchTaskItemIterator;
    }

    public void setBatchTaskItemIterator(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        this.batchTaskItemIterator = batchTaskItemIterator;
    }

    public BaseAuditLogAPI getAuditLogAPI() {
        return auditLogAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public SoftwareControlMgmtAPI getSoftwareControlMgmtAPI() {
        return softwareControlMgmtAPI;
    }

    public void setSoftwareControlMgmtAPI(SoftwareControlMgmtAPI softwareControlMgmtAPI) {
        this.softwareControlMgmtAPI = softwareControlMgmtAPI;
    }

    public UUID getTargetGroupId() {
        return targetGroupId;
    }

    public void setTargetGroupId(UUID targetGroupId) {
        this.targetGroupId = targetGroupId;
    }
}
