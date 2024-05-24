package com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.UserDiskPoolEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 * Description: 用户与磁盘池操作DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/16
 *
 * @author TD
 */
public interface UserDiskPoolDAO extends SkyEngineJpaRepository<UserDiskPoolEntity, UUID> {
}
