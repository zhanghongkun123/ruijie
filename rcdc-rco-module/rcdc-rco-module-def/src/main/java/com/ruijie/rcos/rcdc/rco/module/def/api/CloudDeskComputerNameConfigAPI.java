package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.UUID;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: CloudDeskComputerNameConfigAPI
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/10
 *
 * @author wjp
 */
public interface CloudDeskComputerNameConfigAPI {

    /**
     * 创建云桌面计算机名配置记录
     * 
     * @param computerName 计算机名
     * @param deskStrategyId 云桌面策略ID
     * @throws BusinessException 业务异常
     */
    void createCloudDeskComputerNameConfig(String computerName, UUID deskStrategyId) throws BusinessException;

    /**
     * 更新云桌面计算机名配置记录
     * 
     * @param computerName 计算机名
     * @param deskStrategyId 云桌面策略ID
     * @throws BusinessException 业务异常
     */
    void updateCloudDeskComputerNameConfig(String computerName, UUID deskStrategyId) throws BusinessException;

    /**
     * 删除云桌面计算机名配置记录
     *
     * @param deskStrategyId 云桌面策略ID
     * @throws BusinessException 业务异常
     */
    void deleteCloudDeskComputerNameConfig(UUID deskStrategyId) throws BusinessException;

    /**
     * 查询云桌面计算机名
     *
     * @param deskStrategyId 云桌面策略ID
     * @return 计算机名
     * @throws BusinessException 业务异常
     */
    String findCloudDeskComputerName(UUID deskStrategyId) throws BusinessException;
}
