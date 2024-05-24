package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.MenuType;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:管理员权限API
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/29 9:44
 *
 * @author linrenjian
 */
public interface AdminPermissionAPI {


    /**
     * 是否拥有权限
     * 
     * @param menuType 权限
     * @param adminId 管理员ID
     * @return boolean
     * @throws BusinessException 异常
     */
    boolean hasPermission(MenuType menuType, UUID adminId) throws BusinessException;

    /**
     * 是否角色名称是超级管理员
     *
     * @param id 用户id
     * @return boolean类型
     */
    boolean roleIsadmin(UUID id);


    /**
     * 角色名称是超级管理员 或者管理员名称是系统管理员
     *
     * @param id 用户id
     * @return boolean类型
     */
    boolean roleIsAdminOrAdminNameIsSysadmin(UUID id);
}
