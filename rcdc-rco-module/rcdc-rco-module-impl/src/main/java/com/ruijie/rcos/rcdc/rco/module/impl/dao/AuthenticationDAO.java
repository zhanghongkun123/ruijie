package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CertificationTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AuthenticationEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 *
 * Description: 用户拓展信息持久化接口.
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年02月24日
 *
 * @author linke
 */
public interface AuthenticationDAO extends SkyEngineJpaRepository<AuthenticationEntity, UUID> {

    /**
     * 根据资源ID和类型查询拓展信息
     *
     * @param resourceId 用户ID
     * @param type 类型
     * @return UserExtEntity
     */
    AuthenticationEntity findByResourceIdAndType(UUID resourceId, CertificationTypeEnum type);

    /**
     * 根据用户ID删除
     *
     * @param resourceId 用户ID
     * @param type 类型
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteByResourceIdAndType(UUID resourceId, CertificationTypeEnum type);

}
