package com.ruijie.rcos.rcdc.rco.module.impl.sm.admin.update;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpdateAdminRequest;
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
 * Create Time: 2021年7月21日
 *
 * @author linrenjian
 */
@Service
public class UpdateAdminDataPermissionProcessor implements StateTaskHandle.StateProcessor {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateAdminDataPermissionProcessor.class);

    @Autowired
    private AdminDataPermissionService adminDataPermissionService;

    @Override
    public ProcessResult doProcess(StateProcessContext stateProcessContext) throws Exception {
        Assert.notNull(stateProcessContext, "stateProcessContext is not null");

        UpdateAdminRequest updateAdminRequest = stateProcessContext.get(UpdateAdminContextKey.UPDATE_ADMIN_REQUEST, UpdateAdminRequest.class);
        //获取管理员ID
        UUID updateAdminId = updateAdminRequest.getId();
        //缓存 删除的管理员数据权限集
        cacheDeleteAdminDataPermisssion(updateAdminId,  stateProcessContext);
        //执行删除
        adminDataPermissionService.deleteAdminDataPermisssionByAdminId(updateAdminId);
        List<AdminDataPermissionEntity> entityList = buildDataPermissionEntityList(stateProcessContext);
        //如果数据权限不为空 进行新增
        if (entityList.size() > 0) {
            LOGGER.info("修改管理员数据权限{}",JSON.toJSONString(entityList));
            adminDataPermissionService.saveAdminDataPermisssionList(entityList);
        }

        return ProcessResult.next();
    }

    @Override
    public ProcessResult undoProcess(StateProcessContext context) throws Exception {
        Assert.notNull(context, "stateProcessContext is not null");
        UpdateAdminRequest updateAdminRequest = context.get(UpdateAdminContextKey.UPDATE_ADMIN_REQUEST, UpdateAdminRequest.class);
        //获取管理员ID
        UUID updateAdminId = updateAdminRequest.getId();
        LOGGER.info("执行修改管理员{}失败，执行删除管理员数据权限删除", updateAdminId);
        adminDataPermissionService.deleteAdminDataPermisssionByAdminId(updateAdminId);
        LinkedList<AdminDataPermissionEntity> linkList = context
                .get(UpdateAdminContextKey.CACHE_DELETE_ADMIN_DATA_PERMISSION, LinkedList.class);
        adminDataPermissionService.saveAdminDataPermisssionList(linkList);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("create roleGroupPermission undoProcess success");
        }
        return ProcessResult.next();
    }

    /**
     * 获取缓存的数据
     * @param roleId
     * @param stateProcessContext
     */
    private void cacheDeleteAdminDataPermisssion(UUID roleId, StateProcessContext stateProcessContext) {
        List<AdminDataPermissionEntity> entityList = adminDataPermissionService.listAdminDataPermisssionByAdminId(roleId);
        stateProcessContext.put(UpdateAdminContextKey.CACHE_DELETE_ADMIN_DATA_PERMISSION,
                new LinkedList<AdminDataPermissionEntity>().addAll(entityList));
    }

    /**
     * 构造用户组 终端组 镜像 权限
     * @param stateProcessContext
     * @return
     */
    private List<AdminDataPermissionEntity> buildDataPermissionEntityList(StateProcessContext stateProcessContext) {
        UpdateAdminRequest updateAdminRequest = stateProcessContext.get(UpdateAdminContextKey.UPDATE_ADMIN_REQUEST, UpdateAdminRequest.class);
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
        return entityList;
    }
}
