package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfilePathGroupEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 * Description: 用户配置路径组DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public interface UserProfilePathGroupDAO extends SkyEngineJpaRepository<UserProfilePathGroupEntity, UUID> {

    /**
     * 根据名称查找组对象
     *
     * @param name 名称
     * @return 组对象
     */
    UserProfilePathGroupEntity findByName(String name);
}