package com.ruijie.rcos.rcdc.rco.module.impl.version;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.role.IacUpdateRoleRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPermissionDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.PermissionMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.MenuType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleType;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.AdminMgmtServiceTx;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.vc.VersionedProcessor;

/**
 * Description:6.0 R3版本升级 数据库升级
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/18 20:34
 *
 * @author linrenjian
 */
@Service
public class RcoVersionProcessorV6V0R3 implements VersionedProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcoVersionProcessorV6V0R3.class);

    /**
     * 角色API
     */
    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Autowired
    private PermissionMgmtAPI permissionMgmtAPI;

    @Autowired
    private AdminMgmtServiceTx adminMgmtServiceTx;

    @Override
    public String version() {
        return "6.0.103";
    }

    @Override
    public void process() {


        // 批量更新角色请求列表
        List<IacUpdateRoleRequest> baseUpdateRoleRequestList = new ArrayList<>();
        try {
            // 查询内置的系统管理员权限
            IacRoleDTO sysadminBaseRoleDTO = baseRoleMgmtAPI.getRoleByRoleName(RoleType.SYSADMIN.getName(), SubSystem.CDC);
            // 查询角色的权限信息
            List<IacPermissionDTO> basePermissionDTOList =
                    permissionMgmtAPI.listPermissionByIdArrAndServerModel(sysadminBaseRoleDTO.getPermissionIdArr());
            // 是否添加菜单 根据系统管理员有没有镜像快照等 菜单
            boolean enable = basePermissionDTOList.stream()
                    .anyMatch(basePermissionDTO -> MenuType.RESTORE_SNAPSHOT.getMenuName().equals(basePermissionDTO.getPermissionCode())
                            || MenuType.LIST_SNAPSHOT.getMenuName().equals(basePermissionDTO.getPermissionCode())
                            || MenuType.SNAPSHOT_QUERY.getMenuName().equals(basePermissionDTO.getPermissionCode())
                            || MenuType.LOCK_SNAPSHOT.getMenuName().equals(basePermissionDTO.getPermissionCode())
                            || MenuType.DELETE_SNAPSHOT.getMenuName().equals(basePermissionDTO.getPermissionCode())
                            || MenuType.RENAME_SNAPSHOT.getMenuName().equals(basePermissionDTO.getPermissionCode())
                            || MenuType.CONFIG_SNAPSHOT_LIMIT.getMenuName().equals(basePermissionDTO.getPermissionCode())
                            || MenuType.CANCEL_SNAPSHOT_RESTORE_TASK.getMenuName().equals(basePermissionDTO.getPermissionCode()));
            LOGGER.info("升级到6.0R3时，[{}]需要进行到6.0R3新增的菜单补齐{}", sysadminBaseRoleDTO.getRoleName(), enable);
            if (!enable) {
                // 查询出全部的权限
                List<IacPermissionDTO> allBasePermissionDTOList = permissionMgmtAPI.listAllPermissionByServerModel();
                // 过滤出目标权限
                // 镜像管理 -快照管理
                List<UUID> collectList = allBasePermissionDTOList.stream()
                        .filter(basePermissionDTO -> basePermissionDTO.getTags() != null
                                && (MenuType.RESTORE_SNAPSHOT.getMenuName().equals(basePermissionDTO.getPermissionCode())
                                        || MenuType.LIST_SNAPSHOT.getMenuName().equals(basePermissionDTO.getPermissionCode())
                                        || MenuType.SNAPSHOT_QUERY.getMenuName().equals(basePermissionDTO.getPermissionCode())
                                        || MenuType.LOCK_SNAPSHOT.getMenuName().equals(basePermissionDTO.getPermissionCode())
                                        || MenuType.DELETE_SNAPSHOT.getMenuName().equals(basePermissionDTO.getPermissionCode())
                                        || MenuType.RENAME_SNAPSHOT.getMenuName().equals(basePermissionDTO.getPermissionCode())
                                        || MenuType.CONFIG_SNAPSHOT_LIMIT.getMenuName().equals(basePermissionDTO.getPermissionCode())
                                        || MenuType.CANCEL_SNAPSHOT_RESTORE_TASK.getMenuName().equals(basePermissionDTO.getPermissionCode())

                                )).map(IacPermissionDTO::getId).collect(Collectors.toList());
                LOGGER.info("升级到6.0R3时，查询到的6.0R3新增的菜单列表{}", JSON.toJSONString(collectList));
                if (CollectionUtils.isNotEmpty(collectList)) {
                    // 获取当前系统管理员拥有的权限
                    UUID[] permissionIdArr = sysadminBaseRoleDTO.getPermissionIdArr();
                    List<UUID> newPermissionList = Stream.of(permissionIdArr).collect(Collectors.toList());
                    // 添加到
                    newPermissionList.addAll(collectList);
                    // 设置权限
                    sysadminBaseRoleDTO.setPermissionIdArr(newPermissionList.toArray(new UUID[0]));
                    IacUpdateRoleRequest baseUpdateRoleRequest = new IacUpdateRoleRequest();
                    // 将更新修正的角色信息
                    BeanUtils.copyProperties(sysadminBaseRoleDTO, baseUpdateRoleRequest);
                    // 强制修改默认角色
                    baseUpdateRoleRequest.setForceDefault(Boolean.TRUE);
                    // 添加到需要根据的角色列表
                    baseUpdateRoleRequestList.add(baseUpdateRoleRequest);
                    // 需要更新的角色不为空 进行更新
                    if (CollectionUtils.isNotEmpty(baseUpdateRoleRequestList)) {
                        baseRoleMgmtAPI.updateRole(baseUpdateRoleRequest);
                    }
                }

            }


        } catch (Exception e) {
            LOGGER.warn("升级到6.0R3时，查询到的6.0R3，系统管理员菜单自动补齐过滤失败", e);
        }
    }


}
