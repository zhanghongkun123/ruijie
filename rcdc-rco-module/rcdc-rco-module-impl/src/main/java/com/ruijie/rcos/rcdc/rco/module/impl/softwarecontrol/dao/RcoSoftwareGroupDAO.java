package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareGroupTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoSoftwareGroupEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
public interface RcoSoftwareGroupDAO extends SkyEngineJpaRepository<RcoSoftwareGroupEntity, UUID> {

    /**
     * 根据名称和分组类型获取到分组实体
     *
     * @param name      分组名称
     * @param groupType 分组类型
     * @return 分组实体
     */
    RcoSoftwareGroupEntity findByNameAndGroupType(String name, SoftwareGroupTypeEnum groupType);

    /**
     * 根据软件分组ids返回分组列表
     * @param groupIds 分组id
     * @return 分组列表
     */
    List<RcoSoftwareGroupEntity> findByIdIn(Iterable<UUID> groupIds);
}
