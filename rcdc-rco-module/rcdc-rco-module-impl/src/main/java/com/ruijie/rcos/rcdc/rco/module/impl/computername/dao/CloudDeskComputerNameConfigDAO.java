package com.ruijie.rcos.rcdc.rco.module.impl.computername.dao;

import java.util.UUID;
import com.ruijie.rcos.rcdc.rco.module.impl.computername.entity.CloudDeskComputerNameConfigEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: 云桌面计算机名配置数据接口
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public interface CloudDeskComputerNameConfigDAO extends SkyEngineJpaRepository<CloudDeskComputerNameConfigEntity, UUID> {

    /**
     * 根据deskStrategyId查询云桌面策略和计算名称关系表
     * 
     * @param deskStrategyId 云桌面策略ID
     * @return 结果
     */
    CloudDeskComputerNameConfigEntity findByDeskStrategyId(UUID deskStrategyId);

}
