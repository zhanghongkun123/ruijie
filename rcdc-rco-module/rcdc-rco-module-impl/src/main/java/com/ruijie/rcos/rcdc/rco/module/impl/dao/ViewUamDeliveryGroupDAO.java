package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryGroupEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: 交付对象视图
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/05 12:03
 *
 * @author coderLee23
 */
public interface ViewUamDeliveryGroupDAO
        extends SkyEngineJpaRepository<ViewUamDeliveryGroupEntity, UUID>, JpaSpecificationExecutor<ViewUamDeliveryGroupEntity> {


}
