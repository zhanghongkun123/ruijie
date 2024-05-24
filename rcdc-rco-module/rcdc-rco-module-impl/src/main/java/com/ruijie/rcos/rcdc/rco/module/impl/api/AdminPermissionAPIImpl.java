package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacPermissionMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPermissionDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.PermissionMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DefaultAdmin;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.MenuType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SuperPrivilegeRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.SuperPrivilegeResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/29 9:45
 *
 * @author linrenjian
 */
public class AdminPermissionAPIImpl implements AdminPermissionAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminPermissionAPIImpl.class);

    @Autowired
    private IacPermissionMgmtAPI basePermissionMgmtAPI;

    @Autowired
    private PermissionMgmtAPI permissionMgmtAPI;

    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private AdminManageAPI adminManageAPI;

    @Override
    public boolean hasPermission(MenuType menuType, UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");
        Assert.notNull(menuType, "menuType is not null");
        // 获取用户信息
        IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(adminId);
        // 获取角色
        IacAdminDTO[] baseAdminDTOArr = new IacAdminDTO[] {baseAdminDTO};
        // 获取权限菜单
        String[] menuNameArr = getPertmissionArr(baseAdminDTOArr[0].getRoleIdArr(), baseAdminDTO.getId());
        // 是否有权限标识
        boolean hasPermission = false;
        // 遍历菜单 如果用户有菜单权限 返回true
        for (int i = 0; i < menuNameArr.length; i++) {
            if (menuNameArr[i].equals(menuType.getMenuName())) {
                hasPermission = true;
                break;
            }
        }
        return hasPermission;
    }


    /**
     * 获取菜单
     *
     * @param roleIdArr 角色集合
     * @param adminId 管理员id
     * @return
     */
    private String[] getPertmissionArr(UUID[] roleIdArr, UUID adminId) throws BusinessException {
        if (ArrayUtils.isEmpty(roleIdArr)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        Set<String> menuNameSet = new HashSet<>();
        SuperPrivilegeRequest superPrivilegeRequest = new SuperPrivilegeRequest();
        superPrivilegeRequest.setRoleIdArr(roleIdArr);
        SuperPrivilegeResponse superPrivilegeResponse = adminManageAPI.isSuperPrivilege(superPrivilegeRequest);
        // 角色权限关联表中未记载超级管理员信息，直接通过是否为超级管理员来返回所有菜单列表
        if (superPrivilegeResponse.isSuperPrivilege()) {
            List<IacPermissionDTO> basePermissionDTOList = permissionMgmtAPI.listAllPermissionByServerModel();
            if (basePermissionDTOList != null) {
                // 遍历tag 不为空
                List<String> menuNameList = basePermissionDTOList.stream()
                        .filter(permissionDTO -> Objects.nonNull(permissionDTO.getTags()))
                        .map(IacPermissionDTO::getPermissionCode).collect(Collectors.toList());
                menuNameSet.addAll(menuNameList);
            }
        } else {
            List<IacPermissionDTO> basePermissionDTOList = basePermissionMgmtAPI.listPermissionByAdminIdAndSource(adminId, SubSystem.CDC);
            if (basePermissionDTOList != null) {
                // 遍历tag key为菜单标签 value 为菜单
                List<String> menuNameList = basePermissionDTOList.stream()
                        .filter(permissionDTO -> Objects.nonNull(permissionDTO.getTags()))
                        .map(IacPermissionDTO::getPermissionCode).collect(Collectors.toList());
                menuNameSet.addAll(menuNameList);
            }
        }
        try {
            List<String> unsupportedMenuNameList = permissionMgmtAPI.getCurrentServerModelUnsupportedMenuNameList();
            LOGGER.info("获取当前服务器模式不支持的菜单列表{}:{}", unsupportedMenuNameList.size(), unsupportedMenuNameList.toArray());
            menuNameSet.removeAll(unsupportedMenuNameList);
        } catch (Exception e) {
            LOGGER.error("获取当前服务器模式不支持的菜单列表失败", e);
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return menuNameSet.toArray(new String[0]);
    }

    /**
     * 是否是超级管理员
     *
     * @param id 用户id
     * @return boolean类型
     */
    @Override
    public boolean roleIsadmin(UUID id) {
        Assert.notNull(id, "id is not null");

        try {
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(id);
            UUID[] roleIdArr = baseAdminDTO.getRoleIdArr();
            List<IacRoleDTO> baseRoleDTOList = baseRoleMgmtAPI.getRoleAllByRoleIds(roleIdArr);
            // 角色是超级管理员(中文)
            return baseRoleDTOList.stream().anyMatch(dto -> RoleType.ADMIN.getName().equals(dto.getRoleName()));
        } catch (BusinessException e) {
            LOGGER.error("baseAdminMgmtAPI roleIsadmin error!", e);
            return false;
        }
    }


    /**
     * 角色名称是超级管理员 或者管理员名称是 系统管理员
     *
     * @param id 用户id
     * @return boolean类型
     */
    @Override
    public boolean roleIsAdminOrAdminNameIsSysadmin(UUID id) {
        Assert.notNull(id, "id is not null");

        try {
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(id);
            UUID[] roleIdArr = baseAdminDTO.getRoleIdArr();
            List<IacRoleDTO> baseRoleDTOList = baseRoleMgmtAPI.getRoleAllByRoleIds(roleIdArr);
            // 角色是超级管理员(中文) 或者管理员名称是sysadmin
            return DefaultAdmin.SYSADMIN.getName().equals(baseAdminDTO.getUserName())
                    || baseRoleDTOList.stream().anyMatch(dto -> RoleType.ADMIN.getName().equals(dto.getRoleName()));
        } catch (BusinessException e) {
            LOGGER.error(String.format("用户[%s]判断是否超级管理员 或者管理员名称是系统管理员 发生错误error!：[%s]", id, e));
            return false;
        }
    }
}
