package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcaDynamicUserInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description: 应用组绑定用户API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 24/10/2022 下午 3:25
 *
 * @author gaoxueyuan
 */
public interface DynamicUserGroupMappingAPI {

    /**
     * 分页获取云应用组绑定用户列表
     *
     * @param request 分页请求
     * @throws BusinessException 业务异常
     * @return 用户列表
     */
    DefaultPageResponse<RcaDynamicUserInfoDTO> pageQuery(PageSearchRequest request) throws BusinessException;
}
