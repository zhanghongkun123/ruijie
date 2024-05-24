package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleGroupPermissionType;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RoleGroupPermissionEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月07日
 *
 * @author xiejian
 */
public interface RoleGroupPermissionDAO extends SkyEngineJpaRepository<RoleGroupPermissionEntity, UUID> {

    /**
     * 根据groupType和角色Id列表返回所有角色权限
     * @param type groupType
     * @param roleIds roleId列表
     * @return entity列表
     */
    List<RoleGroupPermissionEntity> findByGroupTypeAndRoleIdIn(RoleGroupPermissionType type, List<UUID> roleIds);

    /**
     * 根据角色Id列表返回所有角色权限
     * @param roleId 角色ID
     * @return entity列表
     */
    List<RoleGroupPermissionEntity> findByRoleId(UUID roleId);


    /**
     * 根据角色ID删除RoleGroupPermissionEntity记录
     * @param roleId 角色ID
     */
    @Modifying
    @Transactional
    void deleteByRoleId(UUID roleId);

    /**
     * 根据角色ID,groupID,groupType删除RoleGroupPermissionEntity记录
     * @param groupId 组ID
     * @param groupType 组类型
     */
    @Modifying
    @Transactional
    void deleteByGroupIdAndGroupType(String groupId, RoleGroupPermissionType groupType);


}