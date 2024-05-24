package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.ViewUserProfileChildPathEntity;
import org.springframework.data.domain.Page;

/**
 * Description: 子路径列表展示的service
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/21
 *
 * @author zwf
 */
public interface ViewUserProfileChildService {
    /**
     * 查询子路径列表
     *
     * @param request 查询请求
     * @return 子路径列表
     */
    Page<ViewUserProfileChildPathEntity> pageQuery(PageSearchRequest request);
}
