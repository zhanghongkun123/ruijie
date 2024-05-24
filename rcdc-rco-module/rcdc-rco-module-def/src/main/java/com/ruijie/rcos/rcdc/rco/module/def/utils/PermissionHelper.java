package com.ruijie.rcos.rcdc.rco.module.def.utils;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.base.iac.module.dto.IacLoginUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacLoginAdminRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AdminDataPermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DefaultAdmin;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleGroupPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateAdminDataPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeleteRoleGroupPermissionByGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.GetAllByGroupIdAndIsDeleteRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListImageIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListTerminalGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListImageIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListTerminalGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListUserGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.PermissionConstants;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年02月20日
 *
 * @author xiejian
 */
@Service
public class PermissionHelper {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionHelper.class);

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private AdminMgmtAPI adminMgmtAPI;

    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private IacAdminMgmtAPI iacAdminMgmtAPI;

    @Autowired
    private IacRoleMgmtAPI iacRoleMgmtAPI;

    public static final String DATA_PERMISSION_ID_KEY = "id";


    /**
     * 判断是否是所有组权限
     *
     * @param sessionContext session上下文
     * @return boolean
     */
    public boolean isAllGroupPermission(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is not null");
        IacLoginUserDTO userInfo = iacAdminMgmtAPI.getLoginUserInfo();
        return isAllGroupPermission(userInfo.getId());
    }

    /**
     * 判断是否是所有组权限
     *
     * @param adminId 管理员ID
     * @return boolean
     */
    public boolean isAllGroupPermission(UUID adminId) {
        Assert.notNull(adminId, "adminId is not null");

        try {
            IacAdminDTO admin = iacAdminMgmtAPI.getAdmin(adminId);
            IacRoleDTO role = iacRoleMgmtAPI.getRole(admin.getRoleIdArr()[0]);
            // 角色是超级管理员(中文) 或者 内置的系统管理员（英文）
            return RoleType.ADMIN.getName().equals(role.getRoleName()) || DefaultAdmin.SYSADMIN.getName().equals(admin.getUserName());

        } catch (BusinessException e) {
            LOGGER.error("baseAdminMgmtAPI getAdmin error!", e);
            return false;
        }
    }

    /**
     * 判断是否是所有日志权限
     *
     * @param sessionContext session上下文
     * @return boolean类型
     */
    public boolean isAllLogPermission(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is not null");

        try {
            IacLoginUserDTO loginUserInfo = baseAdminMgmtAPI.getLoginUserInfo();
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(loginUserInfo.getId());
            UUID[] roleIdArr = baseAdminDTO.getRoleIdArr();

            List<IacRoleDTO> baseRoleDTOList = baseRoleMgmtAPI.getRoleAllByRoleIds(roleIdArr);
            // 角色是 系统管理员（英文） 超级管理员(中文) 审计管理员（英文） 则有全部权限
            return baseRoleDTOList.stream().anyMatch(dto -> dto.getRoleName().equals(RoleType.SYSADMIN.getName())
                    || dto.getRoleName().equals(RoleType.ADMIN.getName()) || dto.getRoleName().equals(RoleType.AUDADMIN.getName()));

        } catch (BusinessException e) {
            LOGGER.error("baseAdminMgmtAPI getAdmin error!", e);
            return false;
        }


    }

    /**
     * 判断角色是是否是超级管理员权限
     *
     * @param sessionContext session上下文
     * @return boolean类型
     */
    public boolean roleIsSuperAdmin(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is not null");

        try {
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
            UUID[] roleIdArr = baseAdminDTO.getRoleIdArr();

            List<IacRoleDTO> baseRoleDTOList = baseRoleMgmtAPI.getRoleAllByRoleIds(roleIdArr);
            // 角色是超级管理员(中文)
            return baseRoleDTOList.stream().anyMatch(dto -> RoleType.ADMIN.getName().equals(dto.getRoleName()));

        } catch (BusinessException e) {
            LOGGER.error("baseAdminMgmtAPI getAdmin error!", e);
            return false;
        }


    }

    /**
     * 判断角色是是否是超级管理员权限
     *
     * @param role 角色dto
     * @return boolean类型
     */
    public boolean roleIsSuperAdmin(IacRoleDTO role) {
        Assert.notNull(role, "role is not null");

        // 角色是超级管理员(中文)
        return RoleType.ADMIN.getName().equals(role.getRoleName());
    }

    /**
     * 判断角色是是否是审计管理员权限
     *
     * @param role 角色dto
     * @return boolean类型
     */
    public boolean roleIsAudaAdmin(IacRoleDTO role) {
        Assert.notNull(role, "role is not null");

        // 角色是超级管理员(中文)
        return RoleType.AUDADMIN.getName().equals(role.getRoleName());
    }

    /**
     * 判断角色是是否是系统管理员权限
     *
     * @param role 角色dto
     * @return boolean类型
     */
    public boolean roleIsSysAdmin(IacRoleDTO role) {
        Assert.notNull(role, "role is not null");

        // 角色是超级管理员(中文)
        return RoleType.SYSADMIN.getName().equals(role.getRoleName());
    }

    /**
     * 判断角色是是否是安全管理员权限
     *
     * @param role 角色dto
     * @return boolean类型
     */
    public boolean roleIsSecAdmin(IacRoleDTO role) {
        Assert.notNull(role, "role is not null");

        // 角色是超级管理员(中文)
        return RoleType.SECADMIN.getName().equals(role.getRoleName());
    }

    /**
     * 根据管理员名称判断是否是超级管理员权限
     *
     * @param adminName 管理员名称
     * @return boolean类型
     */
    public boolean roleIsSuperAdminByName(String adminName) {
        Assert.notNull(adminName, "adminName must not null");

        try {
            IacAdminDTO baseAdminDTO = adminMgmtAPI.getAdminByUserName(adminName);
            UUID[] roleIdArr = baseAdminDTO.getRoleIdArr();
            List<IacRoleDTO> baseRoleDTOList = baseRoleMgmtAPI.getRoleAllByRoleIds(roleIdArr);

            // 角色是超级管理员(中文)
            return baseRoleDTOList.stream().anyMatch(dto -> RoleType.ADMIN.getName().equals(dto.getRoleName()));
        } catch (BusinessException e) {
            LOGGER.error(String.format("判断管理员[%s]是否为安全管理员失败", adminName), e);

            return false;
        }
    }

    /**
     * 判断角色是是否是安全管理员权限
     *
     * @param sessionContext session上下文
     * @return boolean类型
     */
    public boolean roleIsSecAdmin(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is not null");

        try {
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
            UUID[] roleIdArr = baseAdminDTO.getRoleIdArr();
            List<IacRoleDTO> baseRoleDTOList = baseRoleMgmtAPI.getRoleAllByRoleIds(roleIdArr);

            // 角色是安全管理员(中文)
            return baseRoleDTOList.stream().anyMatch(dto -> RoleType.SECADMIN.getName().equals(dto.getRoleName()));
        } catch (BusinessException e) {
            LOGGER.error("判断管理员是否为安全管理员失败：", e);

            return false;
        }
    }


    /**
     * 判断拥有的角色是否是内置系统管理员
     *
     * @param sessionContext session上下文
     * @return boolean类型
     */
    public boolean roleIsSysAdmin(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is not null");

        try {
            IacLoginUserDTO loginUserInfo = baseAdminMgmtAPI.getLoginUserInfo();
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(loginUserInfo.getId());
            UUID[] roleIdArr = baseAdminDTO.getRoleIdArr();

            List<IacRoleDTO> baseRoleDTOList = baseRoleMgmtAPI.getRoleAllByRoleIds(roleIdArr);
            // 角色是系统管理员
            return baseRoleDTOList.stream().anyMatch(dto -> dto.getRoleName().equals(RoleType.SYSADMIN.getName()));

        } catch (BusinessException e) {
            LOGGER.error("baseAdminMgmtAPI getAdmin error!", e);
            return false;
        }
    }

    /**
     * 判断是否是超级管理员 超级管理员账号为admin
     *
     * @param sessionContext session上下文
     * @return boolean类型
     */
    public boolean isAdminName(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is not null");

        return isAdminName(sessionContext.getUserName());
    }

    /**
     * 判断是否是超级管理员 超级管理员账号为admin
     *
     * @return boolean类型
     */
    public boolean isAdminName() {
        IacLoginUserDTO loginUserInfo = baseAdminMgmtAPI.getLoginUserInfo();
        return isAdminName(loginUserInfo.getUserName());
    }

    /**
     * 判断要管理员是内置超级管理员或者安全管理员
     *
     * @param username 管理员名称
     * @return boolean
     */
    public boolean validateAdminIsAdminOrSecadmin(String username) {
        Assert.notNull(username, "username is not null");
        return DefaultAdmin.SECADMIN.getName().equals(username) || DefaultAdmin.ADMIN.getName().equals(username);
    }

    /**
     * 判断是否是超级管理员 超级管理员账号为admin
     *
     * @param name name
     * @return boolean类型
     */
    public boolean isAdminName(String name) {
        Assert.notNull(name, "name is not null");

        return DefaultAdmin.ADMIN.getName().equals(name);
    }

    /**
     * 获取所有有权限的云桌面id
     *
     * @param sessionContext session上下文
     * @return boolean类型
     * @throws BusinessException 业务异常
     */
    public UUID[] getDesktopIdArr(SessionContext sessionContext) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext can not be null");

        IacLoginUserDTO loginUserInfo = baseAdminMgmtAPI.getLoginUserInfo();
        return getDesktopIdArr(loginUserInfo.getId());
    }

    /**
     * 获取所有有权限的云桌面id
     *
     * @param adminId 管理员ID
     * @return boolean类型
     * @throws BusinessException 业务异常
     */
    public UUID[] getDesktopIdArr(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId can not be null");

        GetAllByGroupIdAndIsDeleteRequest request = new GetAllByGroupIdAndIsDeleteRequest();
        // 用户组
        request.setUserGroupIdArr(getUserGroupIdArr(adminId));
        // 终端组
        request.setTerminalGroupIdArr(getTerminalGroupIdArr(adminId));
        // 镜像
        request.setImageIdArr(getImageIdArr(adminId));
        // 桌面池组
        request.setDesktopPoolArr(getPermissionIdArr(adminId, AdminDataPermissionType.DESKTOP_POOL));
        List<UUID> uuidStrList = userDesktopMgmtAPI.getDesktopIdArr(request);

        return uuidStrList.toArray(new UUID[uuidStrList.size()]);
    }

    /**
     * 获取所有有权限的用户组id
     *
     * @param adminId 管理员ID
     * @return boolean类型
     * @throws BusinessException 业务异常
     */
    public UUID[] getUserGroupIdArr(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId can not be null");

        ListUserGroupIdRequest request = new ListUserGroupIdRequest();
        request.setAdminId(adminId);
        ListUserGroupIdResponse response = adminDataPermissionAPI.listUserGroupIdByAdminId(request);
        List<String> uuidStrList = response.getUserGroupIdList();
        List<UUID> uuidList = uuidStrList.stream().filter(uuidStr -> !uuidStr.equals(PermissionConstants.ROOT_ID))
                .map(UUID::fromString).collect(Collectors.toList());

        return uuidList.toArray(new UUID[uuidList.size()]);
    }

    /**
     * 获取所有有权限的用户组id
     *
     * @param adminId 管理员ID
     * @return boolean类型
     * @throws BusinessException 业务异常
     */
    public UUID[] getTerminalGroupIdArr(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId can not be null");

        ListTerminalGroupIdRequest request = new ListTerminalGroupIdRequest();
        request.setAdminId(adminId);
        ListTerminalGroupIdResponse response = adminDataPermissionAPI.listTerminalGroupIdByAdminId(request);
        List<String> uuidStrList = response.getTerminalGroupIdList();
        List<UUID> uuidList = uuidStrList.stream().filter(uuidStr -> !uuidStr.equals(PermissionConstants.ROOT_ID))
                .map(UUID::fromString).collect(Collectors.toList());

        return uuidList.toArray(new UUID[uuidList.size()]);
    }

    /**
     * 获取所有有权限的镜像id
     *
     * @param adminId 管理员ID
     * @return boolean类型
     * @throws BusinessException 业务异常
     */
    public UUID[] getImageIdArr(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId can not be null");

        // 构造查询镜像权限请求
        ListImageIdRequest request = new ListImageIdRequest();
        request.setAdminId(adminId);
        // 获取返回镜像数据权限
        ListImageIdResponse response = adminDataPermissionAPI.listImageIdByAdminId(request);
        List<String> uuidStrList = response.getImageIdList();
        List<UUID> uuidList = uuidStrList.stream().map(UUID::fromString).collect(Collectors.toList());

        return uuidList.toArray(new UUID[0]);
    }

    /**
     * 获取所有有权限的镜像id
     *
     * @param sessionContext session上下文
     * @param permissionType 权限类型
     * @return 权限ID列表
     */
    public UUID[] getPermissionIdArr(SessionContext sessionContext, AdminDataPermissionType permissionType) {
        Assert.notNull(sessionContext, "sessionContext can not be null");
        Assert.notNull(permissionType, "permissionType can not be null");
        return getPermissionIdArr(sessionContext.getUserId(), permissionType);
    }

    /**
     * 获取所有有权限的镜像id
     *
     * @param adminId        管理员ID
     * @param permissionType 权限类型
     * @return 权限ID列表
     */
    public UUID[] getPermissionIdArr(UUID adminId, AdminDataPermissionType permissionType) {
        Assert.notNull(adminId, "adminId can not be null");
        Assert.notNull(permissionType, "permissionType can not be null");
        List<String> permissionIdList = adminDataPermissionAPI.listByAdminIdAndPermissionType(adminId, permissionType);
        return permissionIdList.stream().map(UUID::fromString).toArray(UUID[]::new);
    }

    /**
     * 保存管理员组权限
     *
     * @param sessionContext session信息
     * @param groupId        组ID
     * @param type           组类型
     * @throws BusinessException 业务异常
     */
    public void saveAdminGroupPermission(SessionContext sessionContext, UUID groupId, AdminDataPermissionType type) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext can not be null");
        Assert.notNull(groupId, "groupId can not be null");
        Assert.notNull(type, "type can not be null");

        IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());


        CreateAdminDataPermissionRequest createAdminDataPermissionRequest = new CreateAdminDataPermissionRequest();
        AdminDataPermissionDTO dto = new AdminDataPermissionDTO();
        //管理员ID
        dto.setAdminId(baseAdminDTO.getId());
        //数据
        dto.setPermissionDataId(String.valueOf(groupId));
        //数据类型
        dto.setPermissionDataType(type);
        createAdminDataPermissionRequest.setAdminDataPermissionDTO(dto);
        // 创建管理员的数据权限关联关系
        adminDataPermissionAPI.createAdminGroupPermission(createAdminDataPermissionRequest);
    }

    /**
     * 批量删除管理员组权限
     *
     * @param deleteGroupIdList 组ID列表
     * @param type              组类型
     * @throws BusinessException 业务异常
     */
    public void deleteAdminGroupPermissionList(List<UUID> deleteGroupIdList, RoleGroupPermissionType type) {
        Assert.notEmpty(deleteGroupIdList, "deleteGroupIdList can not be null");
        Assert.notNull(type, "type can not be null");

        for (UUID deleteGroupId : deleteGroupIdList) {
            DeleteRoleGroupPermissionByGroupIdRequest request = new DeleteRoleGroupPermissionByGroupIdRequest();
            request.setGroupType(type.name());
            request.setGroupId(deleteGroupId.toString());
            adminDataPermissionAPI.deleteAdminGroupPermissionByGroupId(request);
        }
    }


    /**
     * 根据数据id删除关联权限
     * 
     * @param permissionDataId 权限数据id
     */
    public void deleteByPermissionDataId(String permissionDataId) {
        Assert.notNull(permissionDataId, "permissionDataId can not be null");
        
        adminDataPermissionAPI.deleteByPermissionDataId(permissionDataId);
    }

    /**
     * 管理员是否拥有摸个数据权限
     * 
     * @param adminId 管理员id
     * @param permissionDataId 数据id
     * @param permissionDataType 数据类型
     * @return Boolean
     */
    public Boolean hasDataPermission(UUID adminId, String permissionDataId, AdminDataPermissionType permissionDataType) {
        Assert.notNull(adminId, "adminId can not be null");
        Assert.notNull(permissionDataId, "permissionDataId can not be null");
        Assert.notNull(permissionDataType, "permissionDataType can not be null");

        return adminDataPermissionAPI.hasDataPermission(adminId, permissionDataId, permissionDataType);
    }
}
