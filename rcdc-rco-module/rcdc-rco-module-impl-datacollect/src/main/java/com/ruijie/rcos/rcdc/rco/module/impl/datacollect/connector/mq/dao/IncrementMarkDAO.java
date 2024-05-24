package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.entity.IncrementMarkEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * <br>
 * Description: 数据收集查询SQL <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/21 <br>
 *
 * @author xwx
 */
public interface IncrementMarkDAO extends SkyEngineJpaRepository<IncrementMarkEntity, String> {
    /**
     * 查找mark
     * @param itemKey itemKey
     * @return mark
     */
    IncrementMarkEntity findByItemKey(String itemKey);
}
