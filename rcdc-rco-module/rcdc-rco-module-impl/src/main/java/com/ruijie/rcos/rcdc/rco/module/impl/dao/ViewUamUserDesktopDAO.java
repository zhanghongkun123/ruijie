package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamUserDesktopEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/05/01 22:02
 *
 * @author coderLee23
 */
public interface ViewUamUserDesktopDAO
        extends SkyEngineJpaRepository<ViewUamUserDesktopEntity, UUID>, JpaSpecificationExecutor<ViewUamUserDesktopEntity> {


    /**
     * 根据云桌面id查找对象
     * 
     * @param cloudDesktopId 云桌面id
     * @return entity
     */
    ViewUamUserDesktopEntity findByCloudDesktopId(UUID cloudDesktopId);

}
