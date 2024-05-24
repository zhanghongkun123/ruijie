package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import java.util.UUID;

/**
 * Description: 机柜持久化接口
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author BaiGuoliang
 */

public interface CabinetDAO extends SkyEngineJpaRepository<CabinetEntity, UUID> {

    /**
     * 基于机柜名称获取机柜entity
     *
     * @param cabinetName 机柜名称
     * @return 响应
     */
    CabinetEntity getByName(String cabinetName);

}