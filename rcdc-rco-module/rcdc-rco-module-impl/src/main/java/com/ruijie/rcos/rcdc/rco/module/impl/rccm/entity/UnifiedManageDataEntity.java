package com.ruijie.rcos.rcdc.rco.module.impl.rccm.entity;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 统一管理数据
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/11
 *
 * @author TD
 */
@Entity
@Table(name = "t_rco_unified_manage_data")
public class UnifiedManageDataEntity {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 关联ID
     */
    private UUID relatedId;

    /**
     * 关联对象
     */
    @Enumerated(EnumType.STRING)
    private UnifiedManageFunctionKeyEnum relatedType;

    /**
     * 统一管理ID
     */
    private UUID unifiedManageDataId;

    /**
     * 创建时间
     */
    private Date createTime;

    @Version
    private Integer version;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
