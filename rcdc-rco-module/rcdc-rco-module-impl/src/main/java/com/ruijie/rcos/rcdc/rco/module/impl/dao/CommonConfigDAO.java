package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.UUID;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ConfigKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CommonConfigEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 *
 * Description: 配置DAO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author BaiGuoliang
 */
public interface CommonConfigDAO extends SkyEngineJpaRepository<CommonConfigEntity, UUID> {

    /**
     * 
     * @param key key
     * @return CommonConfigEntity
     */
    CommonConfigEntity getByConfigKey(ConfigKeyEnum key);
}