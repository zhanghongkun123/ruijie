package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaDynamicUserInfoEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 * Description: 应用组绑定用户视图
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 24/10/2022 下午 3:35
 *
 * @author gaoxueyuan
 */
public interface ViewRcaDynamicUserInfoDAO extends SkyEngineJpaRepository<ViewRcaDynamicUserInfoEntity, UUID> {
}
