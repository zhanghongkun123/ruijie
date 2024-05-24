package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.UamDeliveryGroupObjectSpecUpk;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryGroupObjectSpecEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/03/03 20:07
 *
 * @author coderLee23
 */
public interface ViewUamDeliveryGroupObjectSpecDAO extends SkyEngineJpaRepository<ViewUamDeliveryGroupObjectSpecEntity,
        UamDeliveryGroupObjectSpecUpk> {


    /**
     * 根据交付组id 获取交付对象的规格数据
     *
     * @param deliveryGroupId 交付组id
     * @return List<ViewUamDeliveryGroupObjectSpecEntity>
     */
    List<ViewUamDeliveryGroupObjectSpecEntity> findByDeliveryGroupId(UUID deliveryGroupId);
}
