package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewDistributeTaskEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/05/04 11:37
 *
 * @author coderLee23
 */
public interface ViewDistributeTaskDAO
        extends SkyEngineJpaRepository<ViewDistributeTaskEntity, UUID>, JpaSpecificationExecutor<ViewDistributeTaskEntity> {


}
