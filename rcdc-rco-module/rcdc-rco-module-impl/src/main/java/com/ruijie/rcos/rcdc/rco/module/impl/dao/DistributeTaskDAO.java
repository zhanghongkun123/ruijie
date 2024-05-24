package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.DistributeTaskEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Description: 分发任务（父任务）DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 14:51
 *
 * @author zhangyichi
 */
public interface DistributeTaskDAO extends SkyEngineJpaRepository<DistributeTaskEntity, UUID> {

    /**
     * 根据参数ID查找文件分发任务
     * @param parameterId 参数ID
     * @return 任务列表
     */
    List<DistributeTaskEntity> findByParameterId(UUID parameterId);

    /**
     * 根据任务名称查找文件分发任务
     * @param name 任务名称
     * @return 任务列表
     */
    List<DistributeTaskEntity> findByTaskName(String name);
}
