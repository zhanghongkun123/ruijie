package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.role;

import java.util.Iterator;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

/**
 * Description: Function Description 删除角色批量任务请求
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年08月03日
 *
 * @author linrenjian
 */
public class DeleteRoleBatchTaskHandlerRequest {

    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    @NotNull
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    /**
     * 权限API
     */
    @NotNull
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    /**
     * 单个/多个批处理标识位 默认是进行多个的批处理
     */
    private boolean enableBatch = true;

    @NotNull
    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    public BaseAuditLogAPI getAuditLogAPI() {
        return auditLogAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }


    public IacRoleMgmtAPI getBaseRoleMgmtAPI() {
        return baseRoleMgmtAPI;
    }

    public void setBaseRoleMgmtAPI(IacRoleMgmtAPI baseRoleMgmtAPI) {
        this.baseRoleMgmtAPI = baseRoleMgmtAPI;
    }

    public Iterator<? extends BatchTaskItem> getBatchTaskItemIterator() {
        return batchTaskItemIterator;
    }

    public void setBatchTaskItemIterator(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        this.batchTaskItemIterator = batchTaskItemIterator;
    }


    public IacAdminMgmtAPI getBaseAdminMgmtAPI() {
        return baseAdminMgmtAPI;
    }

    public void setBaseAdminMgmtAPI(IacAdminMgmtAPI baseAdminMgmtAPI) {
        this.baseAdminMgmtAPI = baseAdminMgmtAPI;
    }

    public boolean getEnableBatch() {
        return enableBatch;
    }

    public void setEnableBatch(boolean enableBatch) {
        this.enableBatch = enableBatch;
    }
}

