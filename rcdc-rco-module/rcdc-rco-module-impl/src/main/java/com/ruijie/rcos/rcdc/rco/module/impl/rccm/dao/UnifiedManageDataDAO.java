package com.ruijie.rcos.rcdc.rco.module.impl.rccm.dao;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.entity.UnifiedManageDataEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * Description: 统一管理DAO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/11
 *
 * @author TD
 */
public interface UnifiedManageDataDAO extends SkyEngineJpaRepository<UnifiedManageDataEntity, UUID> {

    /**
     * 根据关联ID和关联类型查询数据
     *
     * @param relatedId   关联ID
     * @param relatedType 关联类型
     * @return UnifiedManageDataEntity
     */
    UnifiedManageDataEntity findByRelatedIdAndRelatedType(UUID relatedId, UnifiedManageFunctionKeyEnum relatedType);

    /**
     * 根据管理ID查询数据
     *
     * @param unifiedManageDataId  管理ID
     * @return UnifiedManageDataEntity
     */
    UnifiedManageDataEntity findByUnifiedManageDataId(UUID unifiedManageDataId);

    /**
     * 根据关联类型查询数据
     *
     * @param relatedType 关联类型
     * @return UnifiedManageDataEntity
     */
    List<UnifiedManageDataEntity> findByRelatedType(UnifiedManageFunctionKeyEnum relatedType);

    /**
     * 根据关联id和资源类型查找统一管理数据唯一ID
     *
     * @param relatedId 关联id
     * @param relatedType 类型
     * @return 统一管理数据唯一ID
     */
    @Query("SELECT unifiedManageDataId FROM UnifiedManageDataEntity WHERE relatedId = ?1 AND relatedType = ?2")
    UUID getUnifiedManageDataId(UUID relatedId, UnifiedManageFunctionKeyEnum relatedType);

    /**
     * 根据管理ID列表查询数据
     *
     * @param unifiedManageDataIdList 管理ID列表
     * @return 结果集
     */
    List<UnifiedManageDataEntity> findByUnifiedManageDataIdIn(List<UUID> unifiedManageDataIdList);

    /**
     * 根据关联ID列表和类型查询
     *
     * @param relatedType 类型
     * @param relatedIdList 关联id列表
     * @return 结果集
     */
    List<UnifiedManageDataEntity> findByRelatedTypeAndRelatedIdIn(UnifiedManageFunctionKeyEnum relatedType, List<UUID> relatedIdList);
}
