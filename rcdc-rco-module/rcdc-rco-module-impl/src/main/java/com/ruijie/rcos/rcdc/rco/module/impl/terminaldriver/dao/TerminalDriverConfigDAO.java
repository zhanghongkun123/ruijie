package com.ruijie.rcos.rcdc.rco.module.impl.terminaldriver.dao;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.terminaldriver.entity.TerminalDriverConfigEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: 终端驱动安装配置
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/02/02
 *
 * @author luojianmo
 */
public interface TerminalDriverConfigDAO extends SkyEngineJpaRepository<TerminalDriverConfigEntity, UUID> {

    /**
     * 根据终端产品ID，获取终端驱动配置信息
     * 
     * @param productId 终端产品ID
     * @return 终端驱动配置信息
     */
    TerminalDriverConfigEntity findByProductId(String productId);
}
