package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.Date;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ClusterServerTrendEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月9日
 * 
 * @author zhuangchenwu
 */
public interface ClusterServerTrendDAO extends SkyEngineJpaRepository<ClusterServerTrendEntity, UUID> {

    /**
     * 删除指定创建时间之前的记录
     * 
     * @param createTime 指定创建时间
     */
    @Transactional
    void deleteByCreateTimeLessThan(Date createTime);
}
