package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaStrategyUserEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 * Description: 云应用策略绑定用视图
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/31 下午9:15
 *
 * @author yanlin
 */
public interface ViewRcaStrategyUserDAO extends SkyEngineJpaRepository<ViewRcaStrategyUserEntity, UUID> {
}
