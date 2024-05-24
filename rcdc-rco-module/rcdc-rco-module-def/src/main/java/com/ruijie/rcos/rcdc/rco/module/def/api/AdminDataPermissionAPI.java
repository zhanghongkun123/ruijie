package com.ruijie.rcos.rcdc.rco.module.def.api;


import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateAdminDataPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeleteRoleGroupPermissionByGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

import java.util.List;
import java.util.UUID;

/**
 * <p>Title: AdminMgmtAPI</p>
 * <p>Description: Function Description</p>
 * <p>Copyright: Ruijie Co., Ltd. (c) 2020</p>
 * <p>@Author: linrenjian</p>
 * <p>@Date: 2021/7/22 14:51</p>
 */
public interface AdminDataPermissionAPI {

    /**
     * 创建管理员与分组的权限关系
     *
     * @param request 请求
     * @return DtoResponse
     */
    DefaultResponse createAdminGroupPermission(CreateAdminDataPermissionRequest request);

    /**
     * 删除管理员与分组的权限关系
     *
     * @param request 请求
     * @return DefaultResponse
     */
    DefaultResponse deleteAdminGroupPermissionByGroupId(DeleteRoleGroupPermissionByGroupIdRequest request);

    /**
     * 删除根据权限ID
     *
     * @param permissionDataId 权限ID
     * @return int
     */
    int deleteByPermissionDataId(String permissionDataId);

    /**
     * 根据管理员Id获取有权限的用户组Id
     *
     * @param request 请求
     * @return ListUserGroupIdResponse
     * @throws BusinessException 业务异常
     */
    ListUserGroupIdResponse listUserGroupIdByAdminId(ListUserGroupIdRequest request) throws BusinessException;

    /**
     * 返回全部的用户组 GroupIdLabelEntry[]
     *
     * @return GroupIdLabelEntry[]
     * @throws BusinessException 业务异常
     */
    GroupIdLabelEntry[] listAllUserGroupEntry() throws BusinessException;

    /**
     * 根据管理员Id获取有权限的终端组Id
     *
     * @param request 请求
     * @return ListTerminalGroupIdResponse
     * @throws BusinessException 业务异常
     */
    ListTerminalGroupIdResponse listTerminalGroupIdByAdminId(ListTerminalGroupIdRequest request) throws BusinessException;


    /**
     * 返回全部的终端组 GroupIdLabelEntry[]
     *
     * @return GroupIdLabelEntry[]
     * @throws BusinessException 业务异常
     */
    GroupIdLabelEntry[] listAllTerminalGroupEntry() throws BusinessException;

    /**
     * 根据管理员Id获取有权限的镜像
     *
     * @param request 请求
     * @return ListTerminalGroupIdResponse
     * @throws BusinessException 业务异常
     */
    ListImageIdResponse listImageIdByAdminId(ListImageIdRequest request) throws BusinessException;


    /**
     * 返回全部的镜像 GroupIdLabelEntry[]
     *
     * @return GroupIdLabelEntry[]
     * @throws BusinessException 业务异常
     */
    GroupIdLabelEntry[] listAllImageEntry() throws BusinessException;

    /**
     * 根据管理员Id获取有权限的用户组IdLabelEntry列表
     *
     * @param request 请求
     * @return ListUserGroupIdResponse
     * @throws BusinessException 业务异常
     */
    ListUserGroupIdLabelEntryResponse listUserGroupIdLabelEntryByAdminId(ListUserGroupIdLabelEntryRequest request) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的终端组IdLabelEntry列表
     *
     * @param request 请求
     * @return ListTerminalGroupIdResponse
     * @throws BusinessException 业务异常
     */
    ListTerminalGroupIdLabelEntryResponse listTerminalGroupIdLabelEntryByAdminId(ListTerminalGroupIdLabelEntryRequest request)
            throws BusinessException;


    /**
     * 根据管理员Id获取有权限的终端组IdLabelEntry列表
     *
     * @param request 请求
     * @return ListTerminalGroupIdResponse
     * @throws BusinessException 业务异常
     */
    ListImageIdLabelEntryResponse listImageIdLabelEntryByAdminId(ListImageIdLabelEntryRequest request) throws BusinessException;

    /**
     * @param adminId 管理员ID
     * @param permissionDataId 数据权限ID
     * @return boolean
     * @throws BusinessException 业务异常
     */
    Boolean hasImageByAdminIdAndImageId(UUID adminId, UUID permissionDataId) throws BusinessException;

    /**
     * 初始化数据管理权限
     *
     * @throws BusinessException 业务异常
     */
    void initializeAdminDataPermission() throws BusinessException;

    /**
     * 初始化云桌面策略数据管理权限
     *
     * @throws BusinessException 业务异常
     */
    void initDeskStrategyAdminDataPermission() throws BusinessException;

    /**
     * 初始化云应用及外设策略数据管理权限
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
     * @param request 请求
     * @return 响应
     * @throws BusinessException 业务异常
     */
    ListIdLabelEntryResponse listDesktopPoolIdLabelEntryByAdminId(ListIdLabelEntryRequest request) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的磁盘池组IdLabelEntry列表
     *
     * @param request 请求
     * @return 响应
     * @throws BusinessException 业务异常
     */
    ListIdLabelEntryResponse listDiskPoolIdLabelEntryByAdminId(ListIdLabelEntryRequest request) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的云桌面策略组IdLabelEntry列表
     *
     * @param request 请求
     * @return 响应
     * @throws BusinessException 业务异常
     */
    ListIdLabelEntryResponse listDeskStrategyIdLabelEntryByAdminId(ListIdLabelEntryRequest request) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的应用池组IdLabelEntry列表
     *
     * @param request 请求
     * @return 响应
     * @throws BusinessException 业务异常
     */
    ListIdLabelEntryResponse listAppPoolIdLabelEntryByAdminId(ListIdLabelEntryRequest request) throws BusinessException;

    /**
     * 根据管理员Id获取有权限的应用策略组IdLabelEntry列表
     *
     * @param request 请求
     * @return 响应
     * @throws BusinessException 业务异常
     */
    ListIdLabelEntryResponse listAppMainStrategyIdLabelEntryByAdminId(ListIdLabelEntryRequest request)
            throws BusinessException;

    /**
     * 根据管理员Id获取有权限的应用外设策略组IdLabelEntry列表
     *
     * @param request 请求
     * @return 响应
     * @throws BusinessException 业务异常
     */
    ListIdLabelEntryResponse listAppPeripheralStrategyIdLabelEntryByAdminId(ListIdLabelEntryRequest request)
            throws BusinessException;

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

}
