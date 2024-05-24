package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.UserDesktopPoolEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 * Description: 用户桌面池表DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/08/11
 *
 * @author linke
 */
public interface UserDesktopPoolDAO extends SkyEngineJpaRepository<UserDesktopPoolEntity, UUID> {

}
