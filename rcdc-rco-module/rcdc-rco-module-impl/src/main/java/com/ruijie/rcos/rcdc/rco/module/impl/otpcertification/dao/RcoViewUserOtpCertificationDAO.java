package com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.entity.RcoViewUserOtpCertificationEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 *
 * Description: 动态口令视图DAO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月19日
 *
 * @author lihengjing
 */
public interface RcoViewUserOtpCertificationDAO extends SkyEngineJpaRepository<RcoViewUserOtpCertificationEntity, UUID> {

    /**
     * 根据用户Id获取配置信息
     * 
     * @param userId 用户ID
     * @return 用户动态口令视图对象
     */
    RcoViewUserOtpCertificationEntity findByUserId(UUID userId);
}
