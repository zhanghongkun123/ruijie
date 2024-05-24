package com.ruijie.rcos.rcdc.rco.module.impl.special.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.special.entity.UserProfileSpecialConfigEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 * Description: 用户特殊配置DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/15
 *
 * @author zwf
 */
public interface UserProfileSpecialConfigDAO extends SkyEngineJpaRepository<UserProfileSpecialConfigEntity, UUID> {

}
