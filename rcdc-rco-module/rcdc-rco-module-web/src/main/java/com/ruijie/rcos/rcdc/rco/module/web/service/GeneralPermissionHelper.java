package com.ruijie.rcos.rcdc.rco.module.web.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AdminDataPermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DefaultAdmin;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateAdminDataPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.BasePermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.CommonBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/12 16:48
 *
 * @author coderLee23
 */
@Service
public class GeneralPermissionHelper {


    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralPermissionHelper.class);

    /**
     * 用户组和终端组存储了 根id == root, 返回时需要过滤
     */
    public static final String ROOT_ID = "root";

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    /**
     * 判断是否拥有所有数据权限
     *
     * @param sessionContext session上下文
     * @return boolean
     */
    public boolean isAllPermission(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is not null");
        try {
            IacAdminDTO admin = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
            IacRoleDTO role = baseRoleMgmtAPI.getRole(admin.getRoleIdArr()[0]);
            // 角色是超级管理员(中文) 或者 内置的系统管理员（英文）
            return RoleType.ADMIN.getName().equals(role.getRoleName()) || DefaultAdmin.SYSADMIN.getName().equals(admin.getUserName());

        } catch (BusinessException e) {
            LOGGER.error("baseAdminMgmtAPI getAdmin error!", e);
            return false;
        }
    }


    /**
     * 获取管理员id
     * 
     * @param sessionContext session上下文
     * @return uuid
     * @throws BusinessException 业务异常
     */
    public UUID getAdminId(SessionContext sessionContext) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext is not null");
        IacAdminDTO admin = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
        return admin.getId();
    }

    /**
     * 获取管理员id
     *
     * @param <T> T extends BasePermissionDTO
     * @param sessionContext session上下文
     * @param dto dto
     * @throws BusinessException 业务异常
     */
    public <T extends BasePermissionDTO> void setPermissionParam(SessionContext sessionContext, T dto) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext is not null");
        Assert.notNull(dto, "dto is not null");
        boolean isAllPermission = isAllPermission(sessionContext);
        dto.setHasAllPermission(isAllPermission);
        if (!isAllPermission) {
            UUID adminId = getAdminId(sessionContext);
            dto.setAdminId(adminId);
        }
    }

    /**
     * 判定是否有操作数据权限
     * 
     * @param sessionContext session上下文
     * @param permissionDataId 资源权限id
     * @param adminDataPermissionType 权限类型
     * @return true or false
     * @throws BusinessException 业务异常
     */
    public Boolean hasPermission(SessionContext sessionContext, UUID permissionDataId, AdminDataPermissionType adminDataPermissionType)
            throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext is not null");
        Assert.notNull(permissionDataId, "permissionDataId is not null or empty");
        Assert.notNull(adminDataPermissionType, "adminDataPermissionType is not null");
        boolean isAllPermission = isAllPermission(sessionContext);
        // 拥有所有权限
        if (isAllPermission) {
            return true;
        }
        UUID adminId = getAdminId(sessionContext);
        return adminDataPermissionAPI.hasDataPermission(adminId, permissionDataId.toString(), adminDataPermissionType);
    }


    /**
     * 校验权限
     * 
     * @param sessionContext session上下文
     * @param permissionDataId 资源权限id
     * @param adminDataPermissionType 权限类型
     * @throws BusinessException 业务异常
     */
    public void checkPermission(SessionContext sessionContext, UUID permissionDataId, AdminDataPermissionType adminDataPermissionType)
            throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext is not null");
        Assert.notNull(permissionDataId, "permissionDataId is not null or empty");
        Assert.notNull(adminDataPermissionType, "adminDataPermissionType is not null");
        Boolean hasPermission = hasPermission(sessionContext, permissionDataId, adminDataPermissionType);
        if (!Boolean.TRUE.equals(hasPermission)) {
            LOGGER.error("数据[{}]不存在或没有数据权限", permissionDataId);
            throw new BusinessException(CommonBusinessKey.RCDC_NOT_EXISTS_NO_DATA_PERMISSION);
        }
    }


    /**
     * 保存管理员组权限
     *
     * @param sessionContext session信息
     * @param permissionDataId 权限id
     * @param type 组类型
     * @throws BusinessException 业务异常
     */
    public void savePermission(SessionContext sessionContext, UUID permissionDataId, AdminDataPermissionType type) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext can not be null");
        Assert.notNull(permissionDataId, "permissionDataId can not be null");
        Assert.notNull(type, "type can not be null");
        IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
        CreateAdminDataPermissionRequest createAdminDataPermissionRequest = new CreateAdminDataPermissionRequest();
        AdminDataPermissionDTO dto = new AdminDataPermissionDTO();
        // 管理员ID
        dto.setAdminId(baseAdminDTO.getId());
        // 数据
        dto.setPermissionDataId(permissionDataId.toString());
        // 数据类型
        dto.setPermissionDataType(type);
        createAdminDataPermissionRequest.setAdminDataPermissionDTO(dto);
        // 创建管理员的数据权限关联关系
        adminDataPermissionAPI.createAdminGroupPermission(createAdminDataPermissionRequest);
    }

    /**
     * 根据权限数据id列表批量删除权限
     *
     * @param permissionDataId 权限id列表
     * @param type 组类型
     */
    public void deletePermission(UUID permissionDataId, AdminDataPermissionType type) {
        Assert.notNull(permissionDataId, "permissionDataId can not be null");
        Assert.notNull(type, "type can not be null");
        adminDataPermissionAPI.deleteByPermissionDataIdAndPermissionDataType(permissionDataId.toString(), type);
    }


    /**
     * 
     * @param amdinId 上下文
     * @param type 组类型
     * @return List<UUID>
     * @throws BusinessException 业务异常
     */
    public List<UUID> listByPermissionType(UUID amdinId, AdminDataPermissionType type) throws BusinessException {
        Assert.notNull(amdinId, "amdinId can not be null");
        Assert.notNull(type, "type can not be null");
        List<String> idStrList = adminDataPermissionAPI.listByAdminIdAndPermissionType(amdinId, type);
        // 过滤 root 主要针对用户组和终端组的根id【root】需要在返回时过滤
        return idStrList.stream().filter(uuidStr -> !ROOT_ID.equals(uuidStr)).map(UUID::fromString).collect(Collectors.toList());
    }


}
