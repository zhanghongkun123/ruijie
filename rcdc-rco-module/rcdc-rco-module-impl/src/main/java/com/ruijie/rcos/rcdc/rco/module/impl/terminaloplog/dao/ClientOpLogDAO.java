package com.ruijie.rcos.rcdc.rco.module.impl.terminaloplog.dao;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.ruijie.rcos.rcdc.rco.module.impl.terminaloplog.entity.ClientOpLogEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月18日
 *
 * @author luoyuanbin
 */
public interface ClientOpLogDAO extends SkyEngineJpaRepository<ClientOpLogEntity, UUID> {

    /**
     * 删除指定操作时间前的记录
     *
     * @param operTime 操作时间
     */
    @Transactional
    @Modifying
    void deleteByOperTimeLessThan(Date operTime);
}
