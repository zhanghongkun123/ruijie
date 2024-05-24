package com.ruijie.rcos.rcdc.rco.module.impl.rca.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaDynamicUserInfoEntity;
import org.springframework.data.domain.Page;

/**
 * Description: 应用组绑定用户视图service
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 24/10/2022 下午 3:32
 *
 * @author gaoxueyuan
 */
public interface QueryDynamicUserGroupUserService {

    /**
     * 分页查询应用镜像已安装应用列表组装用户组
     * @param request request
     * @return 分页查询结果
     */
    Page<ViewRcaDynamicUserInfoEntity> pageQuery(PageSearchRequest request);
}
