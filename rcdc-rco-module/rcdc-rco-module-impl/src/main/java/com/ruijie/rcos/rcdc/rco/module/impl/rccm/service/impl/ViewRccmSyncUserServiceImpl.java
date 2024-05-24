package com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.impl;

import com.ruijie.rcos.rcdc.rco.module.impl.rccm.dao.ViewRccmSyncUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.entity.ViewRccmSyncUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.ViewRccmSyncUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/24 18:52
 *
 * @author coderLee23
 */
@Service
public class ViewRccmSyncUserServiceImpl implements ViewRccmSyncUserService {

    @Autowired
    private ViewRccmSyncUserDAO viewRccmSyncUserDAO;

    @Override
    public Page<ViewRccmSyncUserEntity> pageViewRccmSyncUser(Pageable pageable) {
        Assert.notNull(pageable, "pageable must not be null");
        return viewRccmSyncUserDAO.findAll(pageable);
    }
}
