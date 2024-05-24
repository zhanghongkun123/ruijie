package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service;

import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: 桌面池配置信息service
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/06/24
 *
 * @author linke
 */
public interface DesktopPoolConfigService {

    /**
     * 新增或者修改桌面池配置信息
     *
     * @param desktopPoolConfigDTO 配置信息
     */
    void saveOrUpdateDesktopPoolConfig(DesktopPoolConfigDTO desktopPoolConfigDTO);

    /**
     * 根据桌面池ID查询软件管控策略ID
     *
     * @param desktopPoolId 桌面池ID
     * @return SoftwareStrategyDTO 软件管控策略
     * @throws BusinessException BusinessException
     */
    SoftwareStrategyDTO getSoftwareStrategyByDesktopPoolId(UUID desktopPoolId) throws BusinessException;

    /**
     * 根据桌面池ID查询软件管控策略ID
     *
     * @param desktopPoolId 桌面池ID
     * @return UUID 软件管控策略ID
     */
    UUID getSoftwareStrategyIdByDesktopPoolId(UUID desktopPoolId);

    /**
     * 根据桌面池ID查询配置信息
     *
     * @param desktopPoolId 桌面池ID
     * @return  DesktopPoolConfigDTO 配置信息
     */
    DesktopPoolConfigDTO queryByDesktopPoolId(UUID desktopPoolId);

    /**
     * 删除指定桌面池id的数据
     *
     * @param desktopPoolId 桌面池id
     */
    void deleteByDesktopPoolId(UUID desktopPoolId);

    /**
     * 是否存在指定的软件管控策略
     *
     * @param softwareStrategyId 软件管控策略ID
     * @return true 存在； false 不存在
     */
    boolean existsBySoftwareStrategyId(UUID softwareStrategyId);
}
