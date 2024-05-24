package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamAppTestEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/04/05 23:01
 *
 * @author coderLee23
 */
public interface ViewUamAppTestDAO extends SkyEngineJpaRepository<ViewUamAppTestEntity, UUID>, JpaSpecificationExecutor<ViewUamAppTestEntity> {


}
