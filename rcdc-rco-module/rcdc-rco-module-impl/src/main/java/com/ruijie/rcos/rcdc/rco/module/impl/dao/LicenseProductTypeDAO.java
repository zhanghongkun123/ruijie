package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.LicenseProductTypeEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: License DAO类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月4日
 * 
 * @author zouqi
 */
public interface LicenseProductTypeDAO extends SkyEngineJpaRepository<LicenseProductTypeEntity, UUID> {

    /**
     * 根据授权产品型号获取featureCode
     * @param featureId 产品型号
     * @return List 
     */
    List<LicenseProductTypeEntity> findByFeatureId(String featureId);
    
}
