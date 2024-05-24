package com.ruijie.rcos.rcdc.rco.module.impl.sm.admin.create;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.CreateAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminDataPermissionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AdminDataPermissionService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle.ProcessResult;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle.StateProcessContext;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月21日
 *
 * @author linrenjian
 */
@Service
public class CreateAdminDataPermissionProcessor implements StateTaskHandle.StateProcessor {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateAdminDataPermissionProcessor.class);

    @Autowired
    private AdminDataPermissionService adminDataPermissionService;

    @Override
    public ProcessResult doProcess(StateProcessContext stateProcessContext) throws Exception {
        Assert.notNull(stateProcessContext, "stateProcessContext is not null");

        List<AdminDataPermissionEntity> entityList = buildDataPermissionEntityList(stateProcessContext);
        //如果数据权限不为空 进行新增
        if (entityList.size() > 0) {
            LOGGER.info("新增管理员数据权限{}",JSON.toJSONString(entityList));
            adminDataPermissionService.saveAdminDataPermisssionList(entityList);
        }

        return ProcessResult.next();
    }

    @Override
    public ProcessResult undoProcess(StateProcessContext context) throws Exception {
        Assert.notNull(context, "stateProcessContext is not null");
        // 获取管理员ID
        UUID createAdminId = context.get(CreateAdminContextKey.CREATE_ADMIN_ID, UUID.class);
        LOGGER.info("执行新增管理员{}失败，执行删除管理员数据权限删除", createAdminId);
        adminDataPermissionService.deleteAdminDataPermisssionByAdminId(createAdminId);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("create roleGroupPermission undoProcess success");
        }

        return ProcessResult.next();
    }

    /**
     * 构造用户组 终端组 镜像 权限
     * @param stateProcessContext
     * @return
     */
    private List<AdminDataPermissionEntity> buildDataPermissionEntityList(StateProcessContext stateProcessContext) {
        CreateAdminRequest createAdminRequest = stateProcessContext.get(CreateAdminContextKey.CREATE_ADMIN_REQUEST, CreateAdminRequest.class);
        // 获取管理员ID
        UUID createAdminId = stateProcessContext.get(CreateAdminContextKey.CREATE_ADMIN_ID, UUID.class);
        LOGGER.info("执行新增管理员ID{}，管理员信息为:{}", createAdminId, JSON.toJSONString(createAdminRequest));
        List<AdminDataPermissionEntity> entityList = new ArrayList<>();

        // 用户组权限
        if (createAdminRequest.getUserGroupIdArr() != null && createAdminRequest.getUserGroupIdArr().length > 0) {
            Arrays.asList(createAdminRequest.getUserGroupIdArr()).stream().forEach(str -> {
                AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
                // 管理员ID
                entity.setAdminId(createAdminId);
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
        if (createAdminRequest.getTerminalGroupIdArr() != null && createAdminRequest.getTerminalGroupIdArr().length > 0) {
            Arrays.asList(createAdminRequest.getTerminalGroupIdArr()).stream().forEach(str -> {
                AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
                // 管理员ID
                entity.setAdminId(createAdminId);
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
        if (createAdminRequest.getImageArr() != null && createAdminRequest.getImageArr().length > 0) {
            Arrays.asList(createAdminRequest.getImageArr()).stream().forEach(str -> {
                AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
                // 管理员ID
                entity.setAdminId(createAdminId);
                // 数据ID
                entity.setPermissionDataId(str.toString());
                // 镜像权限类型
                entity.setPermissionDataType(AdminDataPermissionType.IMAGE);
                entity.setCreateDate(new Date());
                entity.setUpdateDate(new Date());
                entityList.add(entity);
            });
        }

        // 云桌面策略
        if (createAdminRequest.getDeskStrategyArr() != null && createAdminRequest.getDeskStrategyArr().length > 0) {
            Arrays.asList(createAdminRequest.getDeskStrategyArr()).stream().forEach(str -> {
                AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
                // 管理员ID
                entity.setAdminId(createAdminId);
                // 数据ID
                entity.setPermissionDataId(str.toString());
                // 镜像权限类型
                entity.setPermissionDataType(AdminDataPermissionType.DESKTOP_STRATEGY);
                entity.setCreateDate(new Date());
                entity.setUpdateDate(new Date());
                entityList.add(entity);
            });
        }

        // 桌面池
        if (createAdminRequest.getDesktopPoolArr() != null && createAdminRequest.getDesktopPoolArr().length > 0) {
            Arrays.asList(createAdminRequest.getDesktopPoolArr()).stream().forEach(str -> {
                AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
                // 管理员ID
                entity.setAdminId(createAdminId);
                // 数据ID
                entity.setPermissionDataId(str.toString());
                // 镜像权限类型
                entity.setPermissionDataType(AdminDataPermissionType.DESKTOP_POOL);
                entity.setCreateDate(new Date());
                entity.setUpdateDate(new Date());
                entityList.add(entity);
            });
        }

        // 磁盘池
        if (createAdminRequest.getDiskPoolArr() != null && createAdminRequest.getDiskPoolArr().length > 0) {
            Arrays.asList(createAdminRequest.getDiskPoolArr()).stream().forEach(str -> {
                AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
                // 管理员ID
                entity.setAdminId(createAdminId);
                // 数据ID
                entity.setPermissionDataId(str.toString());
                // 镜像权限类型
                entity.setPermissionDataType(AdminDataPermissionType.DISK_POOL);
                entity.setCreateDate(new Date());
                entity.setUpdateDate(new Date());
                entityList.add(entity);
            });
        }

        // 应用池
        if (createAdminRequest.getAppPoolArr() != null && createAdminRequest.getAppPoolArr().length > 0) {
            Arrays.asList(createAdminRequest.getAppPoolArr()).stream().forEach(str -> {
                AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
                // 管理员ID
                entity.setAdminId(createAdminId);
                // 数据ID
                entity.setPermissionDataId(str.toString());
                // 镜像权限类型
                entity.setPermissionDataType(AdminDataPermissionType.APP_POOL);
                entity.setCreateDate(new Date());
                entity.setUpdateDate(new Date());
                entityList.add(entity);
            });
        }

        // 云应用外设策略
        if (createAdminRequest.getAppPeripheralStrategyArr() != null &&
                createAdminRequest.getAppPeripheralStrategyArr().length > 0) {
            Arrays.asList(createAdminRequest.getAppPeripheralStrategyArr()).stream().forEach(str -> {
                AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
                // 管理员ID
                entity.setAdminId(createAdminId);
                // 数据ID
                entity.setPermissionDataId(str.toString());
                // 镜像权限类型
                entity.setPermissionDataType(AdminDataPermissionType.APP_PERIPHERAL_STRATEGY);
                entity.setCreateDate(new Date());
                entity.setUpdateDate(new Date());
                entityList.add(entity);
            });
        }

        // 云应用策略
        if (createAdminRequest.getAppMainStrategyArr() != null && createAdminRequest.getAppMainStrategyArr().length > 0) {
            Arrays.asList(createAdminRequest.getAppMainStrategyArr()).stream().forEach(str -> {
                AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
                // 管理员ID
                entity.setAdminId(createAdminId);
                // 数据ID
                entity.setPermissionDataId(str.toString());
                // 镜像权限类型
                entity.setPermissionDataType(AdminDataPermissionType.APP_MAIN_STRATEGY);
                entity.setCreateDate(new Date());
                entity.setUpdateDate(new Date());
                entityList.add(entity);
            });
        }

        return entityList;
    }
}
