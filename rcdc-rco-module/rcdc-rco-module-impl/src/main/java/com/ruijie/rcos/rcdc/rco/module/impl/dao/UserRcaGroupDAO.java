package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserRcaGroupEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 * Description: 应用池绑定关系
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/02/21
 *
 * @author zhengjingyong
 */
public interface UserRcaGroupDAO extends SkyEngineJpaRepository<UserRcaGroupEntity, UUID> {

}
