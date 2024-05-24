package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamAppTestApplicationEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月08日
 *
 * @author zhk
 */
public interface ViewUamAppTestApplicationDAO
        extends SkyEngineJpaRepository<ViewUamAppTestApplicationEntity, UUID>, PageQueryDAO<ViewUamAppTestApplicationEntity> {

    /**
     * 根据测试id查询应用测试视图
     *
     * @param testId testId
     * @return ViewUserDesktopEntity
     */
    List<ViewUamAppTestApplicationEntity> findByTestId(UUID testId);

}
