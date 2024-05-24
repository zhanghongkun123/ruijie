package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareRelationTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoSoftwareStrategyRelationEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Description: 软件管控策略与其他对象，如桌面池等的绑定关系DAO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/31
 *
 * @author linke
 */
public interface RcoSoftwareStrategyRelationDAO extends SkyEngineJpaRepository<RcoSoftwareStrategyRelationEntity, UUID> {

    /**
     * 根据使用的对象查询软件管控策略信息
     *
     * @param relationType 使用的对象类型SoftwareRelationTypeEnum
     * @param relationId   使用的对象ID
     * @return RcoSoftwareStrategyRelationEntity
     */
    RcoSoftwareStrategyRelationEntity findByRelationTypeAndRelationId(SoftwareRelationTypeEnum relationType, UUID relationId);

    /**
     * 删除策略使用对象的绑定关系
     *
     * @param relationType 对象类型
     * @param relationId   对象ID
     */
    @Transactional
    @Modifying
    void deleteByRelationTypeAndRelationId(SoftwareRelationTypeEnum relationType, UUID relationId);

    /**
     * 查询软件管控策略是否有被其他对象关联
     *
     * @param softwareStrategyId 软件管控策略ID
     * @return true有被其他对象关联
     */
    boolean existsBySoftwareStrategyId(UUID softwareStrategyId);
}
