package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.ViewUserProfilePathEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 * Description: 路径列表展示的DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/11
 *
 * @author zwf
 */
public interface ViewUserProfilePathDAO extends SkyEngineJpaRepository<ViewUserProfilePathEntity, UUID> {
}
