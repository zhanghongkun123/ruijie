package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserGroupSyncDataEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 11:19
 *
 * @author coderLee23
 */
public interface ViewUserGroupSyncDataDAO
        extends SkyEngineJpaRepository<ViewUserGroupSyncDataEntity, UUID>, JpaSpecificationExecutor<ViewUserGroupSyncDataEntity> {


    /**
     * 根据 全路径用户组匹配
     * 
     * @param fullGroupName 全路径组名
     * @return ViewUserGroupSyncDataEntity
     */
    ViewUserGroupSyncDataEntity findByFullGroupName(String fullGroupName);

}
