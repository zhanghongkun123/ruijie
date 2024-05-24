package com.ruijie.rcos.rcdc.rco.module.impl.tx;

import java.util.List;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.role.IacUpdateRoleRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpdateAdminDataPermissionRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/8
 *
 * @author nt
 */
public interface AdminMgmtServiceTx {

    /**
     * 更新管理员数据权限
     * @param request 请求参数
     *
     * @throws BusinessException 业务异常
     */
    void updateAdminDataPermission(UpdateAdminDataPermissionRequest request) throws BusinessException;

    /**
     * 批量更新角色
     * 
     * @param baseUpdateRoleRequestList 批量更新角色请求集合
     * @throws BusinessException 异常
     */
    void updateRoleRequestList(List<IacUpdateRoleRequest> baseUpdateRoleRequestList) throws BusinessException;
}
