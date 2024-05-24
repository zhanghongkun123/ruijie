package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.DistributeFileEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 * Description: 分发文件表DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/14 17:42
 *
 * @author zhangyichi
 */
public interface DistributeFileDAO extends SkyEngineJpaRepository<DistributeFileEntity, UUID> {
    /**
     * 根据文件名查找
     * @param name 文件名
     * @return DeskSoftEntity 文件记录
     */
    DistributeFileEntity findByFileName(String name);
}
