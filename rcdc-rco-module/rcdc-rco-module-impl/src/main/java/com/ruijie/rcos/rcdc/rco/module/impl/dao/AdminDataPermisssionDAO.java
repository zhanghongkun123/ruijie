package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminDataPermissionEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: Function Description 用户数据权限表
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月21日
 *
 * @author linrenjian
 */
public interface AdminDataPermisssionDAO extends SkyEngineJpaRepository<AdminDataPermissionEntity, UUID> {
    /**
     * 根据管理员ID 删除用户数据权限
     * 
     * @param adminId 管理员ID
     */
    @Modifying
    @Transactional
    void deleteByAdminId(UUID adminId);

    /**
     * 删除根据权限ID
     * 
     * @param permissionDataId 权限ID
     * @return int
     */
    @Modifying
    @Transactional
    int deleteByPermissionDataId(String permissionDataId);

    /**
     * 根据管理员Id列表返回所有管理员数据权限
     * 
     * @param adminId 管理ID
     * @return entity列表
     */
    List<AdminDataPermissionEntity> findByAdminId(UUID adminId);


    /**
     * 根据adminDataPermisssionType和adminId列表返回数据权限
     * 
     * @param permissionDataType 数据权限类型
     * @param adminId 管理员ID
     * @return list
     */
    List<AdminDataPermissionEntity> findByPermissionDataTypeAndAdminId(AdminDataPermissionType permissionDataType, UUID adminId);

    /**
     * 根据adminDataPermisssionType和adminId和permissionDataId列表返回数据权限
     * 
     * @param permissionDataType 数据权限类型
     * @param adminId 管理员ID
     * @param permissionDataId 数据权限ID
     * @return list
     */
    List<AdminDataPermissionEntity> findByPermissionDataTypeAndAdminIdAndPermissionDataId(AdminDataPermissionType permissionDataType, UUID adminId,
            String permissionDataId);

    /**
     * 根据角色ID,groupID,groupType删除RoleGroupPermissionEntity记录
     * 
     * @param groupId 组ID
     * @param permissionDataType 类型
     * @return int
     */
    @Modifying
    @Transactional
    int deleteByPermissionDataIdAndPermissionDataType(String groupId, AdminDataPermissionType permissionDataType);


    /**
     * 判定管理员是否有操作该条数据的权限
     * 
     * @param adminId 管理员id
     * @param permissionDataId 数据权限id
     * @param permissionDataType 权限数据类型
     * @return true or false
     */
    Boolean existsByAdminIdAndPermissionDataIdAndPermissionDataType(UUID adminId, String permissionDataId,
            AdminDataPermissionType permissionDataType);

    /**
     * 根据管理员ID 删除用户数据权限
     *
     * @param adminId 管理员ID
     * @param dataPermissionTypeList 根据数据权限类型删除
     */
    @Modifying
    @Transactional
    void deleteByAdminIdAndPermissionDataTypeIn(UUID adminId, List<AdminDataPermissionType> dataPermissionTypeList);

}
