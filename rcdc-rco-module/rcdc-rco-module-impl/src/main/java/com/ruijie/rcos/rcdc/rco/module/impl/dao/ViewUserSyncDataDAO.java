package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserSyncDataEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 11:19
 *
 * @author coderLee23
 */
public interface ViewUserSyncDataDAO extends SkyEngineJpaRepository<ViewUserSyncDataEntity, UUID>, JpaSpecificationExecutor<ViewUserSyncDataEntity> {



}
