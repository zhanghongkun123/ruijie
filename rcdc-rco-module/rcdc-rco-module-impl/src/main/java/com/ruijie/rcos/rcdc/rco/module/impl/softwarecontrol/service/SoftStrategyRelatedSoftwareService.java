package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.ViewSoftRelatedSoftStrategyEntity;
import org.springframework.data.domain.Page;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/1/24 17:40
 *
 * @author linrenjian
 */
public interface SoftStrategyRelatedSoftwareService {

    /**
     *
     * @param request search request
     * @return view page
     */
    Page<ViewSoftRelatedSoftStrategyEntity> pageQuery(PageSearchRequest request);
}
