package com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 统一管理DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/10
 *
 * @author TD
 */
public class UnifiedManageDataDTO {

    private UUID id;

    /**
     * 关联ID
     */
    private UUID relatedId;

    /**
     * 关联对象
     */
    private UnifiedManageFunctionKeyEnum relatedType;

    /**
     * 统一管理ID
     */
    private UUID unifiedManageDataId;

    /**
     * 创建时间
     */
    private Date createTime;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
