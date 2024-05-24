package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminDataPermissionEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月21日
 *
 * @author linrenjian
 */
public interface AdminDataPermissionService {

    /**
     * 保存管理员数据权限
     *
     * @param adminDataPermisssionEntity adminDataPermisssionEntity
     */
    void save(AdminDataPermissionEntity adminDataPermisssionEntity);

    /**
     * 保存管理员数据权限的列表
     *
     * @param list 列表
     */
    void saveAdminDataPermisssionList(List<AdminDataPermissionEntity> list);

    /**
     * 根据用户ID 删除管理员数据权限列表
     *
     * @param adminId 角色ID
     */
    void deleteAdminDataPermisssionByAdminId(UUID adminId);


    /**
     * 删除数据权限 根据 数据ID
     *
     * @param permissionDataId 数据ID
     * @return int
     */
    int deleteAdminDataPermisssionByPermissionDataId(String permissionDataId);

    /**
     * 根据管理员ID获取对应数据权限列表
     *
     * @param adminId 角色id
     * @return 组权限列表
     * @throws BusinessException 业务异常
     */
    List<AdminDataPermissionEntity> listAdminDataPermisssionByAdminId(UUID adminId);

    /**
     * 根据管理员Id获取有权限的用户组Id
     *
     * @param adminId 管理员id
     * @return uuid列表
     * @throws BusinessException 业务异常
     */
    List<String> listUserGroupIdByAdminId(UUID adminId) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的终端组Id
     *
     * @param adminId 管理员id
     * @return uuid列表
     * @throws BusinessException 业务异常
     */
    List<String> listTerminalGroupIdByAdminId(UUID adminId) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的镜像Id
     *
     * @param adminId 管理员id
     * @return uuid列表
     * @throws BusinessException 业务异常
     */
    List<String> listImageIdByAdminId(UUID adminId) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的用户组IdLabelEntry列表
     *
     * @param adminId 管理员id
     * @return uuid列表
     * @throws BusinessException 业务异常
     */
    List<GroupIdLabelEntry> listUserGroupIdLabelEntryByAdminId(UUID adminId) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的终端组IdLabelEntry列表
     *
     * @param adminId 管理员id
     * @return uuid列表
     * @throws BusinessException 业务异常
     */
    List<GroupIdLabelEntry> listTerminalGroupIdLabelEntryByAdminId(UUID adminId) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的镜像IdLabelEntry列表
     *
     * @param adminId 管理员id
     * @return uuid列表
     * @throws BusinessException 业务异常
     */
    List<GroupIdLabelEntry> listImageIdLabelEntryByAdminId(UUID adminId) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的镜像IdLabelEntry列表
     *
     * @param adminId 管理员id
     * @param permissionDataId 数据ID
     * @return uuid列表
     * @throws BusinessException 业务异常
     */
    List<GroupIdLabelEntry> listImageIdLabelEntryByAdminIdAndImageId(UUID adminId, String permissionDataId) throws BusinessException;

    /**
     * 在升级的时候初始化数据权限
     *
     * @throws BusinessException 业务异常
     */
    void initializeAdminDataPermission() throws BusinessException;

    /**
     * 在升级的时候初始化云桌面策略数据权限
     *
     * @throws BusinessException 业务异常
     */
    void initDeskStrategyAdminDataPermission() throws BusinessException;

    /**
     * 在升级的时候初始化云应用及外设策略数据权限
     *
     * @throws BusinessException 业务异常
     */
    void initRcaStrategyAdminDataPermission() throws BusinessException;

    /**
     * 根据管理员Id和权限类型获取权限列表
     *
     * @param adminId 管理员ID
     * @param permissionType 权限类型
     * @return 权限列表
     */
    List<String> listByAdminIdAndPermissionType(UUID adminId, AdminDataPermissionType permissionType);

    /**
     * 根据管理员Id获取有权限的桌面池组IdLabelEntry列表
     *
     * @param adminId 管理员ID
     * @return 权限列表
     * @throws BusinessException 业务异常
     */
    List<GroupIdLabelEntry> listDesktopPoolIdLabelEntryByAdminId(UUID adminId) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的磁盘池组IdLabelEntry列表
     *
     * @param adminId 管理员ID
     * @return 权限列表
     * @throws BusinessException 业务异常
     */
    List<GroupIdLabelEntry> listDiskPoolIdLabelEntryByAdminId(UUID adminId) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的云桌面策略组IdLabelEntry列表
     *
     * @param adminId 管理员ID
     * @return 权限列表
     * @throws BusinessException 业务异常
     */
    List<GroupIdLabelEntry> listDeskStrategyIdLabelEntryByAdminId(UUID adminId) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的应用池组IdLabelEntry列表
     *
     * @param adminId 管理员ID
     * @return 权限列表
     * @throws BusinessException 业务异常
     */
    List<GroupIdLabelEntry> listAppPoolIdLabelEntryByAdminId(UUID adminId) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的应用池组IdLabelEntry列表
     *
     * @param adminId 管理员ID
     * @return 权限列表
     * @throws BusinessException 业务异常
     */
    List<GroupIdLabelEntry> listAppMainStrategyIdLabelEntryByAdminId(UUID adminId) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的应用池组IdLabelEntry列表
     *
     * @param adminId 管理员ID
     * @return 权限列表
     * @throws BusinessException 业务异常
     */
    List<GroupIdLabelEntry> listAppPeripheralStrategyIdLabelEntryByAdminId(UUID adminId) throws BusinessException;

    /**
     * 判定管理员是否有操作该条数据的权限
     *
     * @param adminId 管理员id
     * @param permissionDataId 数据权限id
     * @param permissionDataType 数据权限类型
     * @return true or false
     */
    Boolean hasDataPermission(UUID adminId, String permissionDataId, AdminDataPermissionType permissionDataType);

    /**
     * 根据权限id 和权限类型删除数据
     *
     * @param permissionDataId 数据权限id
     * @param permissionDataType 数据权限类型
     */
    void deleteByPermissionDataIdAndPermissionDataType(String permissionDataId, AdminDataPermissionType permissionDataType);

    /**
     * 为sysadmin添加新增的菜单权限
     * @throws BusinessException 业务异常
     */
    void addSysadminNewPermission() throws BusinessException;

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
