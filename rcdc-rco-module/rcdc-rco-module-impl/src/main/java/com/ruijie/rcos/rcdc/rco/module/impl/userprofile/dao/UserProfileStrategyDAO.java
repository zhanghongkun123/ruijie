package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileStrategyEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

/**
 * Description: 用户配置策略DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/14
 *
 * @author WuShengQiang
 */
public interface UserProfileStrategyDAO extends SkyEngineJpaRepository<UserProfileStrategyEntity, UUID> {

    /**
     * 通过策略名称查找策略实体
     *
     * @param name 策略名称
     * @return 响应对象
     */
    UserProfileStrategyEntity findByName(String name);

    /**
     * 查询策略名称
     *
     * @param id 策略ID
     * @return 策略名称
     */
    @Query("select name from UserProfileStrategyEntity where id = ?1")
    String findNameById(UUID id);
}
