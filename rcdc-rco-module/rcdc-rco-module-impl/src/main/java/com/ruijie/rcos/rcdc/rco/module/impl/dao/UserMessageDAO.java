package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserMessageEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * Description: 用户消息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/25
 *
 * @author Jarman
 */
public interface UserMessageDAO extends SkyEngineJpaRepository<UserMessageEntity, UUID> {

    /**
     * 根据信息id获取信息列表
     * @param messageIdList 信息id
     * @return 信息列表
     */
    @Query(value = "select t from UserMessageEntity t where t.id in (?1)")
    List<UserMessageEntity> findByMessageIds(List<UUID> messageIdList);
}
