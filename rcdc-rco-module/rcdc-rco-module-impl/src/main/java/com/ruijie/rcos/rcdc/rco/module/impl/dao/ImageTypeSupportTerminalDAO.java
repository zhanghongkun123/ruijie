package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ImageTypeSupportTerminalEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: 镜像支持的终端
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/15
 *
 * @author ypp
 */
public interface ImageTypeSupportTerminalDAO extends SkyEngineJpaRepository<ImageTypeSupportTerminalEntity, UUID> {

    /**
     * 根据镜像类型和操作系统获取支持的终端型号
     *
     * @param cbbImageType 镜像类型
     * @param cbbOsType 操作系统
     * @return List<ImageTypeSupportOsVersionEntity>
     */
    List<ImageTypeSupportTerminalEntity> findByCbbImageTypeAndOsType(CbbImageType cbbImageType, CbbOsType cbbOsType);
}
