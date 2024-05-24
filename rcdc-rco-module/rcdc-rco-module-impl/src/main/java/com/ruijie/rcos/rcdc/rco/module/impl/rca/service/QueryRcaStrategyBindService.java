package com.ruijie.rcos.rcdc.rco.module.impl.rca.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaStrategyUserEntity;
import org.springframework.data.domain.Page;

import java.util.UUID;

/**
 * Description: 云应用策略绑定视图service
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/31 下午11:26
 *
 * @author yanlin
 */
public interface QueryRcaStrategyBindService {

    /**
     * 分页查询应用镜像已安装应用列表组装用户组
     * @param request request
     * @param rcaStrategyId rcaStrategyId
     * @return 分页查询结果
     */
    Page<ViewRcaStrategyUserEntity> pageQuery(UUID rcaStrategyId, PageSearchRequest request);
}
