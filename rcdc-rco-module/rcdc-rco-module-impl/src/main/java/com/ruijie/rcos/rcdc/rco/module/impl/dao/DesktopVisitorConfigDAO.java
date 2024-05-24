package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopVisitorConfigEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 * 访客用户云桌面配置操作DAO
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月17日
 * 
 * @author zhuangchenwu
 */
public interface DesktopVisitorConfigDAO extends SkyEngineJpaRepository<DesktopVisitorConfigEntity, UUID> {

    /**
     * 根据镜像模板ID获取第一个访客桌面配置信息
     * @param imageTemplateId 镜像模板ID
     * @return 访客桌面配置信息
     */
    DesktopVisitorConfigEntity findFirstByImageTemplateId(UUID imageTemplateId);
    
    /**
     * 根据策略ID获取第一个访客桌面配置信息
     * @param strategyId 策略ID
     * @return 访客桌面配置信息
     */
    DesktopVisitorConfigEntity findFirstByStrategyId(UUID strategyId);
    
    /**
     * 根据网络ID获取第一个访客桌面配置信息
     * @param networkId 网络ID
     * @return 访客桌面配置信息
     */
    DesktopVisitorConfigEntity findFirstByNetworkId(UUID networkId);
}
