package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.CustomerInfoEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * <br>
 * Description:客户信息 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/7 <br>
 *
 * @author xwx
 */
public interface CustomerInfoDAO extends SkyEngineJpaRepository<CustomerInfoEntity, UUID> {

}
