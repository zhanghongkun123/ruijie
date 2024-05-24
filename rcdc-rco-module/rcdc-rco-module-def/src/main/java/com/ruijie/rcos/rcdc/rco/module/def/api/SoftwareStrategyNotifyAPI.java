package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskInfoSoftwareStrategyDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: 软件管控策略发生变更通知桌面
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/4
 *
 * @author chenl
 */

public interface SoftwareStrategyNotifyAPI {

    /**
     *
     * @param deskInfoSoftwareStrategyDTOList 更新配置并且通知所有桌面
     */
    void notifyAllDesk(List<DeskInfoSoftwareStrategyDTO> deskInfoSoftwareStrategyDTOList);

    /**
     *
     * @param softwareIds 软件ids
     * @throws BusinessException 异常
     */
    void updateSoftwareNotifyDesk(List<UUID> softwareIds) throws BusinessException;

    /**
     *
     * @param softwareGroupId 软件分组id
     * @throws BusinessException 异常
     */
    void updateSoftwareGroupNotifyDesk(UUID softwareGroupId);

    /**
     *
     * @param softwareStrategyId 软件策略id
     * @throws BusinessException 异常
     */
    void updateSoftwareStrategyNotifyDesk(UUID softwareStrategyId);

    /**
     * 通知所有关联了软控策略的桌面
     */
    void notifyAllSoftwareStrategyDesk();
}
