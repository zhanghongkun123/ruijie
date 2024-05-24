package com.ruijie.rcos.rcdc.rco.module.impl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ruijie.rcos.rcdc.rco.module.def.distribution.request.SearchDistributeTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewDistributeTaskEntity;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/05/04 11:38
 *
 * @author coderLee23
 */
public interface ViewDistributeTaskService {


    /**
     *
     * 查询文件分发任务列表
     *
     * @param searchDistributeTaskDTO 查询对象
     * @param pageable 分页参数
     * @return Page<ViewDistributeTaskEntity>
     */
    Page<ViewDistributeTaskEntity> pageDistributeTask(SearchDistributeTaskDTO searchDistributeTaskDTO, Pageable pageable);


}
