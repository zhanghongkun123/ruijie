package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamAppTestDeskAppEntity;
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
public interface ViewUamAppTestDeskAppDAO
        extends SkyEngineJpaRepository<ViewUamAppTestDeskAppEntity, UUID>, PageQueryDAO<ViewUamAppTestDeskAppEntity> {



}
