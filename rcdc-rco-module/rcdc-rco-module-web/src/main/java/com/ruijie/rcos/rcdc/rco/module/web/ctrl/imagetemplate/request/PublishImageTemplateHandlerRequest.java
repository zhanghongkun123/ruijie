package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.request;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageSyncMode;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsUpgradeAPI;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import org.springframework.lang.Nullable;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/1
 *
 * @author wjp
 */
public class PublishImageTemplateHandlerRequest {

    @NotNull
    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    @NotNull
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @NotNull
    private CmsUpgradeAPI cmsUpgradeAPI;

    @Nullable
    @TextName
    @Size(max = 64)
    private String versionImageName;

    @Nullable
    @Size(max = 64)
    private String desc;

    @Nullable
    private ImageSyncMode syncMode;

    @Nullable
    private UUID[] storagePoolIdArr;

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

    public CbbImageTemplateMgmtAPI getCbbImageTemplateMgmtAPI() {
        return cbbImageTemplateMgmtAPI;
    }

    public void setCbbImageTemplateMgmtAPI(CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI) {
        this.cbbImageTemplateMgmtAPI = cbbImageTemplateMgmtAPI;
    }

    public CmsUpgradeAPI getCmsUpgradeAPI() {
        return cmsUpgradeAPI;
    }

    public void setCmsUpgradeAPI(CmsUpgradeAPI cmsUpgradeAPI) {
        this.cmsUpgradeAPI = cmsUpgradeAPI;
    }

    @Nullable
    public String getVersionImageName() {
        return versionImageName;
    }

    public void setVersionImageName(@Nullable String versionImageName) {
        this.versionImageName = versionImageName;
    }

    @Nullable
    public String getDesc() {
        return desc;
    }

    public void setDesc(@Nullable String desc) {
        this.desc = desc;
    }


    @Nullable
    public ImageSyncMode getSyncMode() {
        return syncMode;
    }

    public void setSyncMode(@Nullable ImageSyncMode syncMode) {
        this.syncMode = syncMode;
    }

    @Nullable
    public UUID[] getStoragePoolIdArr() {
        return storagePoolIdArr;
    }

    public void setStoragePoolIdArr(@Nullable UUID[] storagePoolIdArr) {
        this.storagePoolIdArr = storagePoolIdArr;
    }
}
