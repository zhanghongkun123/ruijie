package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewDesktopImageRelatedEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Description: 查询云桌面明细视图
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月17日
 *
 * @chenl
 */
public interface ViewDesktopImageRelatedDAO extends SkyEngineJpaRepository<ViewDesktopImageRelatedEntity, UUID> {


    /**
     * 根据桌面id列表跟系统类型查询符合的总数
     *
     * @param deskIdList 桌面列表
     * @return 符合的总数
     */
    List<ViewDesktopImageRelatedEntity> findByDeskIdIn(List<UUID> deskIdList);

}
