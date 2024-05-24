package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.CreateAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpdateAdminDataPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpgradeAdminRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * <p>Title: AdminMgmtService</p>
 * <p>Description: Function Description</p>
 * <p>Copyright: Ruijie Co., Ltd. (c) 2020</p>
 * <p>@Author: xiejian</p>
 * <p>@Date: 2020/1/7 15:27</p>
 */
public interface AdminMgmtService {

    /**
     * 创建管理员
     * @param request 请求参数
     *
     * @throws BusinessException 业务异常
     */
    void createAdmin(CreateAdminRequest request) throws BusinessException;

    /**
     * 升级为管理员
     *
     * @param upgradeAdminRequest 请求参数
     * @throws BusinessException 业务异常
     */
    void upgradeAdmin(UpgradeAdminRequest upgradeAdminRequest) throws BusinessException;

    /**
     *  更新管理员权限数据
     *
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    void updateAdminDataPermission(UpdateAdminDataPermissionRequest request) throws BusinessException;
}
