package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.ViewRcoUserProfileStrategyCountEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 * Description: 用户配置策略路径数量视图DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/14
 *
 * @author WuShengQiang
 */
public interface ViewRcoUserProfileStrategyCountDAO extends SkyEngineJpaRepository<ViewRcoUserProfileStrategyCountEntity, UUID> {
}