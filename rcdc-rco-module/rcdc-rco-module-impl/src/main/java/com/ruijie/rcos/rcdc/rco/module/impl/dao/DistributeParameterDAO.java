package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.DistributeParameterEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Description: 文件分发任务参数DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/16 18:10
 *
 * @author zhangyichi
 */
public interface DistributeParameterDAO extends SkyEngineJpaRepository<DistributeParameterEntity, UUID> {

    /**
     * 查找包含指定内容的参数
     * @param content 指定内容
     * @return 参数列表
     */
    List<DistributeParameterEntity> findByParameterIsLike(String content);

}
