package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewPushInstallPackageEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/04/05 15:58
 *
 * @author coderLee23
 */
public interface ViewPushInstallPackageDAO
        extends SkyEngineJpaRepository<ViewPushInstallPackageEntity, UUID>, JpaSpecificationExecutor<ViewPushInstallPackageEntity> {

}
