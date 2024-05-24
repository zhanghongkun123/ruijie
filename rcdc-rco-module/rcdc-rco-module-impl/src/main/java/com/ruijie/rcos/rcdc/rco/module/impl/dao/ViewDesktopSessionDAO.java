package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewDesktopSessionEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月27日
 *
 * @author wangjie9
 */
public interface ViewDesktopSessionDAO extends SkyEngineJpaRepository<ViewDesktopSessionEntity, UUID>, PageQueryDAO<ViewDesktopSessionEntity> {


}
