package com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.entity.TerminalFeatureCodeEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 *
 * Description: 终端特征码关联表DAO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author linke
 */
public interface TerminalFeatureCodeDAO extends SkyEngineJpaRepository<TerminalFeatureCodeEntity, UUID> {

    /**
     * 根据terminalId获取记录信息
     *
     * @param terminalId terminalId
     * @return TerminalFeatureCodeEntity
     */
    TerminalFeatureCodeEntity getOneByTerminalId(String terminalId);

    /**
     *  根据terminalId删除
     *
     *  @param terminalId 终端ID
     */
    @Transactional
    void deleteByTerminalId(String terminalId);
}
