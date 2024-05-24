package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalGroupDesktopRelatedEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/09 16:47
 *
 * @author coderLee23
 */
public interface ViewTerminalGroupDesktopRelatedDAO extends SkyEngineJpaRepository<ViewTerminalGroupDesktopRelatedEntity, UUID>,
        JpaSpecificationExecutor<ViewTerminalGroupDesktopRelatedEntity> {


}
