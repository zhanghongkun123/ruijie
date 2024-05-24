package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.common.query.RcdcJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewAppDiskEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/04/05 15:56
 *
 * @author coderLee23
 */
public interface ViewAppDiskDAO extends RcdcJpaRepository<ViewAppDiskEntity, UUID>, JpaSpecificationExecutor<ViewAppDiskEntity> {


}
