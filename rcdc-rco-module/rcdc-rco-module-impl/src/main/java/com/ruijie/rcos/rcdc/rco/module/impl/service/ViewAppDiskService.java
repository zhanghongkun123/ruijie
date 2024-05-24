package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchAppDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewAppDiskEntity;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/04/05 15:56
 *
 * @author coderLee23
 */
public interface ViewAppDiskService {


    /**
     * 根据查询条件，返回应用磁盘列表
     *
     * @param searchAppDiskDTO 查询对象
     * @param pageable 分页参数
     * @return DefaultPageResponse<UamAppDiskDTO>
     */
    Page<ViewAppDiskEntity> pageAppDisk(SearchAppDiskDTO searchAppDiskDTO, Pageable pageable);

    /**
     * 根据条件统计数量
     *
     * @param request 查询条件
     * @return 数量
     */
    long countByConditions(ConditionQueryRequest request);
}
