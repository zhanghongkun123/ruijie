package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUamUserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUamUserDesktopService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/05/01 22:05
 *
 * @author coderLee23
 */
@Service
public class ViewUamUserDesktopServiceImpl implements ViewUamUserDesktopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewUamUserDesktopServiceImpl.class);

    @Autowired
    private ViewUamUserDesktopDAO viewUamUserDesktopDAO;

    @Override
    public ViewUamUserDesktopEntity findByCloudDesktopId(UUID cloudDesktopId) {
        Assert.notNull(cloudDesktopId, "cloudDesktopId must not be null");
        return viewUamUserDesktopDAO.findByCloudDesktopId(cloudDesktopId);
    }
}
