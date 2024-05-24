package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.TerminalWorkModeMappingEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/30 15:32
 *
 * @author ketb
 */
public interface TerminalWorkModeMappingDAO extends SkyEngineJpaRepository<TerminalWorkModeMappingEntity, UUID> {

    /**
     * 根据支持的工作模式，查询映射关系
     *
     * @param supportMode 终端支持的工作模式
     * @return 终端工作模式映射关系
     */
    TerminalWorkModeMappingEntity findBySupportModeLike(String supportMode);

    /**
     * 根据一级匹配规则，查找对应的支持类型
     *
     * @param matchRule 匹配规则
     * @return 终端工作模式映射关系
     */
    List<TerminalWorkModeMappingEntity> findByMatchRule(String matchRule);

}
