package com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.entity.RcoViewUserHardwareCertificationEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 *
 * Description: 用户硬件特征码视图DAO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author linke
 */
public interface RcoViewUserHardwareCertificationDAO extends SkyEngineJpaRepository<RcoViewUserHardwareCertificationEntity, UUID> {
}
