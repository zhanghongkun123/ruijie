package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.role.IacUpdateRoleRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpdateAdminDataPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminDataPermissionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AdminDataPermissionService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.AdminMgmtServiceTx;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/8
 *
 * @author nt
 */
@Service
public class AdminMgmtServiceTxImpl implements AdminMgmtServiceTx {

    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Autowired
    private AdminDataPermissionService adminDataPermissionService;

    @Autowired
    private IacUserMgmtAPI userAPI;


    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminMgmtServiceTxImpl.class);


    @Override
    public void updateAdminDataPermission(UpdateAdminDataPermissionRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        UUID adminId = request.getId();
        if (Boolean.TRUE.equals(request.getNeedInitDefaultPermission())) {
            List<String> terminalGroupIdList = adminDataPermissionService.listTerminalGroupIdByAdminId(adminId);
            List<String> userGroupIdList = adminDataPermissionService.listUserGroupIdByAdminId(adminId);
            if (CollectionUtils.isEmpty(terminalGroupIdList)) {
                LOGGER.info("管理员[{}]没有终端组权限，默认补偿总览、未分组权限", adminId);
                request.setTerminalGroupIdArr(new String[]{CbbTerminalGroupMgmtAPI.DEFAULT_TERMINAL_GROUP_ID.toString()});
            }
            if (CollectionUtils.isEmpty(userGroupIdList)) {
                LOGGER.info("管理员[{}]没有用户组权限，默认补偿总览、未分组权限", adminId);
                request.setUserGroupIdArr(new String[]{IacUserGroupMgmtAPI.DEFAULT_USER_GROUP_ID.toString()});
            }
        } else {
            // 根据界面权限的配置项进行删除操作
            List<AdminDataPermissionType> deletePermissionTypeList =
                    Arrays.asList(AdminDataPermissionType.USER_GROUP, AdminDataPermissionType.TERMINAL_GROUP, AdminDataPermissionType.IMAGE,
                            AdminDataPermissionType.DESKTOP_POOL, AdminDataPermissionType.DISK_POOL, AdminDataPermissionType.DESKTOP_STRATEGY,
                            AdminDataPermissionType.APP_POOL, AdminDataPermissionType.APP_MAIN_STRATEGY,
                            AdminDataPermissionType.APP_PERIPHERAL_STRATEGY);
            adminDataPermissionService.deleteByAdminIdAndPermissionDataTypeIn(adminId, deletePermissionTypeList);
        }
        List<AdminDataPermissionEntity> entityList = buildDataPermissionEntityList(request);
        // 如果数据权限不为空 进行新增
        if (entityList.size() > 0) {
            LOGGER.info("修改管理员数据权限{}", JSON.toJSONString(entityList));
            adminDataPermissionService.saveAdminDataPermisssionList(entityList);
        }
    }

    /**
     * 构造用户组 终端组 镜像 权限
     *
     * @param updateAdminRequest
     * @return
     */
    private List<AdminDataPermissionEntity> buildDataPermissionEntityList(UpdateAdminDataPermissionRequest updateAdminRequest) {
        // 获取管理员ID
        UUID updateAdminId = updateAdminRequest.getId();
        LOGGER.info("执行修改管理员ID{}，管理员信息为:{}", updateAdminId, JSON.toJSONString(updateAdminRequest));
        List<AdminDataPermissionEntity> entityList = new ArrayList<>();

        // 用户组权限
        if (updateAdminRequest.getUserGroupIdArr() != null && updateAdminRequest.getUserGroupIdArr().length > 0) {
            Arrays.asList(updateAdminRequest.getUserGroupIdArr()).stream().forEach(str -> {
                AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
                // 管理员ID
                entity.setAdminId(updateAdminId);
                // 数据ID
                entity.setPermissionDataId(str);
                // 用户组权限类型
                entity.setPermissionDataType(AdminDataPermissionType.USER_GROUP);
                entity.setCreateDate(new Date());
                entity.setUpdateDate(new Date());
                entityList.add(entity);
            });
        }

        // 终端组权限
        if (updateAdminRequest.getTerminalGroupIdArr() != null && updateAdminRequest.getTerminalGroupIdArr().length > 0) {
            Arrays.asList(updateAdminRequest.getTerminalGroupIdArr()).stream().forEach(str -> {
                AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
                // 管理员ID
                entity.setAdminId(updateAdminId);
                // 数据ID
                entity.setPermissionDataId(str);
                // 终端组权限类型
                entity.setPermissionDataType(AdminDataPermissionType.TERMINAL_GROUP);
                entity.setCreateDate(new Date());
                entity.setUpdateDate(new Date());
                entityList.add(entity);
            });
        }

        // 镜像权限
        if (updateAdminRequest.getImageIdArr() != null && updateAdminRequest.getImageIdArr().length > 0) {
            Arrays.asList(updateAdminRequest.getImageIdArr()).stream().forEach(str -> {
                AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
                // 管理员ID
                entity.setAdminId(updateAdminId);
                // 数据ID
                entity.setPermissionDataId(str.toString());
                // 镜像权限类型
                entity.setPermissionDataType(AdminDataPermissionType.IMAGE);
                entity.setCreateDate(new Date());
                entity.setUpdateDate(new Date());
                entityList.add(entity);
            });
        }

        // 桌面池权限
        UUID[] desktopPoolArr = updateAdminRequest.getDesktopPoolArr();
        if (desktopPoolArr != null && desktopPoolArr.length > 0) {
            Arrays.asList(desktopPoolArr).stream().forEach(desktopPoolId -> {
                AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
                // 管理员ID
                entity.setAdminId(updateAdminId);
                // 数据ID
                entity.setPermissionDataId(desktopPoolId.toString());
                // 镜像权限类型
                entity.setPermissionDataType(AdminDataPermissionType.DESKTOP_POOL);
                entity.setCreateDate(new Date());
                entity.setUpdateDate(new Date());
                entityList.add(entity);
            });
        }

        // 磁盘池权限
        UUID[] diskPoolArr = updateAdminRequest.getDiskPoolArr();
        if (diskPoolArr != null && diskPoolArr.length > 0) {
            Arrays.asList(diskPoolArr).stream().forEach(diskPoolId -> {
                AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
                // 管理员ID
                entity.setAdminId(updateAdminId);
                // 数据ID
                entity.setPermissionDataId(diskPoolId.toString());
                // 镜像权限类型
                entity.setPermissionDataType(AdminDataPermissionType.DISK_POOL);
                entity.setCreateDate(new Date());
                entity.setUpdateDate(new Date());
                entityList.add(entity);
            });
        }

        // 云桌面策略权限
        addAdminDataPermission(updateAdminRequest.getDeskStrategyArr(), updateAdminId, AdminDataPermissionType.DESKTOP_STRATEGY, entityList);
        // 应用池权限
        addAdminDataPermission(updateAdminRequest.getAppPoolArr(), updateAdminId, AdminDataPermissionType.APP_POOL, entityList);
        // 云应用策略权限
        addAdminDataPermission(updateAdminRequest.getAppMainStrategyArr(), updateAdminId, AdminDataPermissionType.APP_MAIN_STRATEGY, entityList);
        // 云应用外设池权限
        addAdminDataPermission(updateAdminRequest.getAppPeripheralStrategyArr(), updateAdminId, AdminDataPermissionType.APP_PERIPHERAL_STRATEGY,
                entityList);

        return entityList;
    }

    private void addAdminDataPermission(UUID[] dataIdArr, UUID createAdminId, AdminDataPermissionType type,
                                        List<AdminDataPermissionEntity> entityList) {
        if (ArrayUtils.isEmpty(dataIdArr)) {
            return;
        }
        AdminDataPermissionEntity entity;
        for (UUID id : dataIdArr) {
            entity = new AdminDataPermissionEntity();
            // 管理员ID
            entity.setAdminId(createAdminId);
            // 数据ID
            entity.setPermissionDataId(id.toString());
            // 权限类型
            entity.setPermissionDataType(type);
            entity.setCreateDate(new Date());
            entity.setUpdateDate(new Date());
            entityList.add(entity);
        }
    }


    @Override
    public void updateRoleRequestList(List<IacUpdateRoleRequest> baseUpdateRoleRequestList) throws BusinessException {
        Assert.notEmpty(baseUpdateRoleRequestList, "baseUpdateRoleRequestList is not empty");

        // 更新角色
        for (IacUpdateRoleRequest baseUpdateRoleRequest : baseUpdateRoleRequestList) {
            baseRoleMgmtAPI.updateRole(baseUpdateRoleRequest);
        }
    }
}
