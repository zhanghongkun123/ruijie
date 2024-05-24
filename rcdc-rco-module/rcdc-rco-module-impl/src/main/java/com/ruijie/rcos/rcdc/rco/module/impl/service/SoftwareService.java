package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoSoftwareEntity;
import org.springframework.data.domain.Page;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/03/09 10:07
 *
 * @author chenli
 */
public interface SoftwareService {


    /**
     * @param request search request
     * @return view page
     */
    Page<RcoSoftwareEntity> pageQuery(PageSearchRequest request);


}