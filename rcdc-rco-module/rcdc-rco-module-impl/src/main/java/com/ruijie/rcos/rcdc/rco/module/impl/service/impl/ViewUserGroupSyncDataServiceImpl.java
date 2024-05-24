package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUserGroupSyncDataDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserGroupSyncDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUserGroupSyncDataService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 11:23
 *
 * @author coderLee23
 */
@Service
public class ViewUserGroupSyncDataServiceImpl implements ViewUserGroupSyncDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewUserGroupSyncDataServiceImpl.class);

    @Autowired
    private ViewUserGroupSyncDataDAO viewUserGroupSyncDataDAO;

    @Override
    public List<ViewUserGroupSyncDataEntity> findAll() {
        return viewUserGroupSyncDataDAO.findAll();
    }

    @Override
    public ViewUserGroupSyncDataEntity findViewUserGroupSyncData(String fullGroupName) {
        Assert.notNull(fullGroupName, "fullGroupName must not be null");
        return viewUserGroupSyncDataDAO.findByFullGroupName(fullGroupName);
    }

    @Override
    public Optional<ViewUserGroupSyncDataEntity> findById(UUID id) {
        Assert.notNull(id, "id must not be null");
        return viewUserGroupSyncDataDAO.findById(id);
    }
}
