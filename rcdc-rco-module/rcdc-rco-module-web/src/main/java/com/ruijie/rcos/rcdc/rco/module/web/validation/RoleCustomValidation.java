package com.ruijie.rcos.rcdc.rco.module.web.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.PermissionMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleType;
import com.ruijie.rcos.rcdc.rco.module.def.enums.FunTypes;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.role.CreateRoleRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.role.UpdateRoleRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 自定义角色验证
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/21 21:24
 *
 * @author linrenjian
 */
@Service
public class RoleCustomValidation {


    private static final Logger LOGGER = LoggerFactory.getLogger(RoleCustomValidation.class);


    /**
     * RCDC 服务器模式权限API
     */
    @Autowired
    private PermissionMgmtAPI basePermissionMgmtAPI;


    /**
     * 创建角色请求参数校验
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void createRoleValidate(CreateRoleRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notEmpty(request.getPermissionIdArr(), "permissionIdArr is not empty");

        if (validateRoleNameDefaultRoleName(request.getRoleName())) {
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ROLE_NAME_IS_DEFAULT_NAME);
        }
        // 前段传来的目标的数据权限列表
        List<IacPermissionDTO> targetBasePermissionList = basePermissionMgmtAPI.listPermissionByIdArrAndServerModel(request.getPermissionIdArr());
        // 源数据全列表
        List<IacPermissionDTO> sourceBasePermissionList = basePermissionMgmtAPI.listAllPermissionByServerModel();
        // 校验自定义角色
        validCustomRoleMenuInfo(request.getPermissionIdArr(), targetBasePermissionList, sourceBasePermissionList);
    }

    /**
     * 编辑角色请求参数校验
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void editRoleValidate(UpdateRoleRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notEmpty(request.getPermissionIdArr(), "permissionIdArr is not empty");

        if (validateRoleNameDefaultRoleName(request.getRoleName())) {
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ROLE_NAME_IS_DEFAULT_NAME);
        }
        // 前段传来的目标的数据权限列表
        List<IacPermissionDTO> targetBasePermissionList = basePermissionMgmtAPI.listPermissionByIdArrAndServerModel(request.getPermissionIdArr());
        // 源数据全列表
        List<IacPermissionDTO> sourceBasePermissionList = basePermissionMgmtAPI.listAllPermissionByServerModel();
        // 校验自定义角色
        validCustomRoleMenuInfo(request.getPermissionIdArr(), targetBasePermissionList, sourceBasePermissionList);
    }

    /**
     * 校验自定义角色
     * 
     * @param permissionIdArr 前段传来的目标的数据权限UUID
     * @param targetBasePermissionList 前段传来的目标的数据权限列表
     * @param sourceBasePermissionList 源数据全列表
     * @throws BusinessException 异常
     */
    private void validCustomRoleMenuInfo(UUID[] permissionIdArr, List<IacPermissionDTO> targetBasePermissionList,
            List<IacPermissionDTO> sourceBasePermissionList) throws BusinessException {

        Assert.notEmpty(permissionIdArr, "permissionIdArr is not empty");
        Assert.notNull(targetBasePermissionList, "targetBasePermissionList is not empty");
        Assert.notNull(sourceBasePermissionList, "sourceBasePermissionList is not empty");

        // 验证权限集合长度判断
        validCustomPermissionLength(permissionIdArr, targetBasePermissionList, sourceBasePermissionList);
        // 目标数据权限UUID
        List<UUID> targetUUIDList = targetBasePermissionList.stream().map(IacPermissionDTO::getId).collect(Collectors.toList());
        // 查询验证 查询父ID不为空的UUID 集合
        validCustomParentMenuInfo(targetUUIDList, targetBasePermissionList);
        // 查询验证是否有超级管理员的权限数据
        validCustomSuperadminMenuInfo(targetBasePermissionList, sourceBasePermissionList);
        // 查询验证必须有的菜单权限
        validCustomNeedadminMenuInfo(targetUUIDList, targetBasePermissionList, sourceBasePermissionList);
        // 从顶部校验自由元素 （由于底部的自由元素会判断顶部的是否存在）

    }

    /**
     * 验证权限集合长度判断
     *
     * @param targetBasePermissionList 前段传来的目标的数据权限列表
     * @throws BusinessException 异常
     */
    private void validCustomPermissionLength(UUID[] permissionIdArr, List<IacPermissionDTO> targetBasePermissionList,
            List<IacPermissionDTO> sourceBasePermissionList) throws BusinessException {
        // 如果权限数组与数据库查询出来的权限菜单集合长度不等 抛出异常
        if (permissionIdArr.length != targetBasePermissionList.size()) {
            LOGGER.error("权限数组与数据库查询的权限菜单集合长度不等抛出异常，非法菜单权限信息 [{}]", JSON.toJSONString(targetBasePermissionList));
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_MENU_ERROR);
        }
        // 如果从前端查询出来的目标权限大于数据库存的权限列表 抛出异常
        if (targetBasePermissionList.size() > sourceBasePermissionList.size()) {
            LOGGER.error("目标权限数组超过当前服务器权限菜单集合长度抛出异常，非法菜单权限信息 [{}]", JSON.toJSONString(targetBasePermissionList));
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_MENU_ERROR);
        }
    }

    /**
     * 查询验证 查询父ID不为空的UUID 集合
     *
     * @param targetUUIDList 前段传来的目标的数据权限 UUID
     * @param targetBasePermissionList 前段传来的目标的数据权限列表
     * @throws BusinessException 异常
     */
    private void validCustomParentMenuInfo(List<UUID> targetUUIDList, List<IacPermissionDTO> targetBasePermissionList) throws BusinessException {
        // 查询父ID不为空的UUID 集合
        List<UUID> parentUUIDList = targetBasePermissionList.stream().filter(basePermissionDTO -> basePermissionDTO.getParentId() != null)
                .map(IacPermissionDTO::getParentId).collect(Collectors.toList());
        // 前段传来的目标的数据权限列表 父ID 是否也在前端的目标数据权限里
        boolean enableParentPermission = parentUUIDList.stream().allMatch(item -> targetUUIDList.contains(item));
        // 前段传来的目标的数据权限列表 父ID 没有包含在前端的目标数据权限里
        if (!enableParentPermission) {
            LOGGER.error("前段传来的目标的数据权限列表 父ID 没有包含在前端的目标数据权限里，非法菜单权限信息 [{}]", JSON.toJSONString(targetBasePermissionList));
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_MENU_ERROR);
        }
    }

    /**
     * 查询验证是否有超级管理员的权限数据
     *
     * @param targetBasePermissionList 前段传来的目标的数据权限列表
     * @param sourceBasePermissionList 源数据全列表
     * @throws BusinessException 异常
     */
    private void validCustomSuperadminMenuInfo(List<IacPermissionDTO> targetBasePermissionList, List<IacPermissionDTO> sourceBasePermissionList)
            throws BusinessException {
        // 查询是否有超级管理员的权限数据
        for (IacPermissionDTO targetPermission : targetBasePermissionList) {
            // 权限标签不为空 并且标签是超级管理员 说明异常
            if (targetPermission.getTags() != null
                    && FunTypes.YES.equals(((JSONObject) (targetPermission.getTags())).getString(FunTypes.ENABLE_SUPER_ADMIN))) {
                LOGGER.error("含有超级管理员的权限数据，非法菜单权限信息 [{}]", JSON.toJSONString(targetPermission));
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_MENU_ERROR);
            }
        }

    }

    /**
     * 查询验证必须有的菜单权限
     *
     * @param targetUUIDList 前段传来的目标的数据权限 UUID
     * @param targetBasePermissionList 前段传来的目标的数据权限列表
     * @param sourceBasePermissionList 源数据全列表
     * @throws BusinessException 异常
     */
    private void validCustomNeedadminMenuInfo(List<UUID> targetUUIDList, List<IacPermissionDTO> targetBasePermissionList,
            List<IacPermissionDTO> sourceBasePermissionList) throws BusinessException {
        // 查询必须有的菜单权限
        List<IacPermissionDTO> needBasePermissionList = new ArrayList<>();
        for (IacPermissionDTO sourcePermission : sourceBasePermissionList) {
            if (sourcePermission.getTags() != null
                    && FunTypes.YES.equals(((JSONObject) (sourcePermission.getTags())).getString(FunTypes.ENABLE_RCDC_NEED))) {
                needBasePermissionList.add(sourcePermission);
            }
        }
        // 必须有的菜单权限的UUID
        List<UUID> needUUIDList = needBasePermissionList.stream().map(IacPermissionDTO::getId).collect(Collectors.toList());
        // 前段传来的目标的数据权限列表 是否包含有全部必有的菜单权限
        boolean enableNeedPermission = needUUIDList.stream().allMatch(item -> targetUUIDList.contains(item));
        // 如果前段传来的目标的数据权限列表 没有包含全部必有的菜单权限
        if (!enableNeedPermission) {
            LOGGER.error("没有包含全部必有的菜单权限,，非法菜单权限信息 [{}]", JSON.toJSONString(needBasePermissionList));
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_MENU_ERROR);
        }
    }



    /**
     * 判断要创建的角色名是否为系统默认角色名
     *
     * @param username
     * @return
     */
    private boolean validateRoleNameDefaultRoleName(String username) {
        RoleType[] adminArr = RoleType.values();
        for (RoleType admin : adminArr) {
            if (admin.getDescribe().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
