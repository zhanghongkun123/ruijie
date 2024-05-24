package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.IpLimitEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 * Description: VDI网段限制DAO类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/27 18:59
 *
 * @author yxq
 */
public interface IpLimitDAO extends SkyEngineJpaRepository<IpLimitEntity, UUID> {
}
