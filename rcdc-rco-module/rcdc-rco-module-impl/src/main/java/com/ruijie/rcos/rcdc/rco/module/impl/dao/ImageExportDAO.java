package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.imageexport.entity.ViewImageExportEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/16 21:26
 *
 * @author ketb
 */
public interface ImageExportDAO extends SkyEngineJpaRepository<ViewImageExportEntity, UUID> {
}
