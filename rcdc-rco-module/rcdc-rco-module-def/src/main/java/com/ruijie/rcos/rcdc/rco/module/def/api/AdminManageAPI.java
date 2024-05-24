package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.SuperPrivilegeRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetAdminPasswordResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.SuperPrivilegeResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年02月15日
 *
 * @author ljm
 */
public interface AdminManageAPI {


    /**
     * 判断是否为超级管理员
     *
     * @param superPrivilegeRequest 管理员角色ID数组
     * @return 响应
     * @throws BusinessException 业务异常
     */
    SuperPrivilegeResponse isSuperPrivilege(SuperPrivilegeRequest superPrivilegeRequest) throws BusinessException;

    /**
     * 根据ID获取管理员密码
     *
     * @param idRequest 管理员ID
     * @return 响应
     * @throws BusinessException 业务异常
     */
    GetAdminPasswordResponse getAdminPassword(IdRequest idRequest) throws BusinessException;
}
