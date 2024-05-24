package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryGroupObjectSpecEntity;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/03/03 20:09
 *
 * @author coderLee23
 */
public interface ViewUamDeliveryGroupObjectSpecService {


    /**
     * 根据交付组id 获取交付对象的规格数据
     * 
     * @param deliveryGroupId 交付组id
     * @return List<ViewUamDeliveryGroupObjectSpecEntity>
     */
    List<ViewUamDeliveryGroupObjectSpecEntity> listByDeliveryGroupId(UUID deliveryGroupId);

}
