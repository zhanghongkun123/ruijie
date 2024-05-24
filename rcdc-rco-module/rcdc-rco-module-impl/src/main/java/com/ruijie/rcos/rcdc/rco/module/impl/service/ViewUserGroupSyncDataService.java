package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserGroupSyncDataEntity;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 11:22
 *
 * @author coderLee23
 */
public interface ViewUserGroupSyncDataService {


    /**
     * 获取所有需要同步的用户组数据
     * 
     * @return List<ViewUserGroupSyncDataEntity>
     */
    List<ViewUserGroupSyncDataEntity> findAll();


    /**
     * 根据全路径的用户组查找
     * 
     * @param fullGroupName 全路径用户组名
     * @return ViewUserGroupSyncDataEntity
     */
    ViewUserGroupSyncDataEntity findViewUserGroupSyncData(String fullGroupName);

    /**
     * 根据id 获取用户组数据
     *
     * @param id 唯一表示
     * @return ViewUserGroupSyncDataEntity
     */
    Optional<ViewUserGroupSyncDataEntity> findById(UUID id);

}
