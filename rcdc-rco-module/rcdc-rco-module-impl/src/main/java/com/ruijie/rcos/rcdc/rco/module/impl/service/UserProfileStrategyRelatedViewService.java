package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.ViewRcoUserProfileStrategyRelatedEntity;
import org.springframework.data.domain.Page;

/**
 * Description: 用户配置策略关联信息详情视图
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/18
 *
 * @author WuShengQiang
 */
public interface UserProfileStrategyRelatedViewService {

    /**
     * 分页查询策略关联的路径详情
     *
     * @param request 请求对象
     * @return 路径视图对象列表
     */
    Page<ViewRcoUserProfileStrategyRelatedEntity> pageQuery(PageSearchRequest request);
}