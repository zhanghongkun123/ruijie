package com.ruijie.rcos.rcdc.rco.module.impl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchDeliveryTestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamAppTestEntity;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/04/05 23:02
 *
 * @author coderLee23
 */
public interface ViewUamAppTestService {

    /**
     * 根据查询条件
     *
     * @param searchDeliveryTestDTO 查询对象
     * @param pageable 分页参数
     * @return Page<ViewUamAppTestEntity>
     */
    Page<ViewUamAppTestEntity> pageUamAppTest(SearchDeliveryTestDTO searchDeliveryTestDTO, Pageable pageable);

}
