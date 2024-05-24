package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUamDeliveryGroupObjectSpecDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryGroupObjectSpecEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUamDeliveryGroupObjectSpecService;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/03/03 20:11
 *
 * @author coderLee23
 */
@Service
public class ViewUamDeliveryGroupObjectSpecServiceImpl implements ViewUamDeliveryGroupObjectSpecService {

    @Autowired
    private ViewUamDeliveryGroupObjectSpecDAO viewUamDeliveryGroupObjectSpecDAO;

    @Override
    public List<ViewUamDeliveryGroupObjectSpecEntity> listByDeliveryGroupId(UUID deliveryGroupId) {
        Assert.notNull(deliveryGroupId, "deliveryGroupId must not be null");
        return viewUamDeliveryGroupObjectSpecDAO.findByDeliveryGroupId(deliveryGroupId);
    }
}
