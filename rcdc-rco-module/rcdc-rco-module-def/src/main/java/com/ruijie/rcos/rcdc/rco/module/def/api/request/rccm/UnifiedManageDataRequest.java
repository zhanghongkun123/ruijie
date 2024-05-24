package com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 统一管理请求类
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/11
 *
 * @author TD
 */
public class UnifiedManageDataRequest {

    /**
     * 关联ID
     */
    @NotNull
    private UUID relatedId;

    /**
     * 关联对象
     */
    @NotNull
    private UnifiedManageFunctionKeyEnum relatedType;

    /**
     * 统一管理ID
     */
    @Nullable
    private UUID unifiedManageDataId = UUID.randomUUID();

    public UnifiedManageDataRequest() {
    }

    public UnifiedManageDataRequest(UUID relatedId, UnifiedManageFunctionKeyEnum relatedType) {
        Assert.notNull(relatedId, "relatedId can not null");
        Assert.notNull(relatedType, "relatedType can not null");
        this.relatedId = relatedId;
        this.relatedType = relatedType;
    }

    public UnifiedManageDataRequest(UUID relatedId, UnifiedManageFunctionKeyEnum relatedType, UUID unifiedManageDataId) {
        Assert.notNull(relatedId, "relatedId can not null");
        Assert.notNull(relatedType, "relatedType can not null");
        Assert.notNull(unifiedManageDataId, "unifiedManageDataId can not null");
        this.relatedId = relatedId;
        this.relatedType = relatedType;
        this.unifiedManageDataId = unifiedManageDataId;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public UnifiedManageFunctionKeyEnum getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(UnifiedManageFunctionKeyEnum relatedType) {
        this.relatedType = relatedType;
    }

    public UUID getUnifiedManageDataId() {
        return unifiedManageDataId;
    }

    public void setUnifiedManageDataId(UUID unifiedManageDataId) {
        this.unifiedManageDataId = unifiedManageDataId;
    }
}
