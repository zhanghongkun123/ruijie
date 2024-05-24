package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ImageTypeSupportOsVersionEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Description: 镜像类型支持操作系统版本配置表
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/15
 *
 * @author ypp
 */
public interface ImageTypeSupportOsVersionDAO extends SkyEngineJpaRepository<ImageTypeSupportOsVersionEntity, UUID> {

    /**
     * 根据镜像类型和操作系统本获取支持的操作系统版本
     * 
     * @param cbbImageType 镜像类型
     * @param cbbOsType 操作系统
     * @return List<ImageTypeSupportOsVersionEntity>
     */
    List<ImageTypeSupportOsVersionEntity> findByCbbImageTypeAndOsType(CbbImageType cbbImageType, CbbOsType cbbOsType);
}
