package com.ruijie.rcos.rcdc.rco.module.impl.rccm.service;

import org.springframework.data.domain.Page;

import com.ruijie.rcos.rcdc.rco.module.impl.rccm.entity.ViewRccmSyncUserEntity;
import org.springframework.data.domain.Pageable;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/24 18:51
 *
 * @author coderLee23
 */
public interface ViewRccmSyncUserService {

    /**
     * 分页获取数据
     * 
     * @param pageable 分页数据
     * @return Page<ViewRccmSyncUserEntity>
     */
    Page<ViewRccmSyncUserEntity> pageViewRccmSyncUser(Pageable pageable);

}
