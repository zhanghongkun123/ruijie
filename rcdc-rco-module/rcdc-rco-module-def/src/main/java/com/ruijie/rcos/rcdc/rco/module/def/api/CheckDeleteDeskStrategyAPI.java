package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: 检查云桌面策略是否可以删除API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/8 18:44
 *
 * @author yxq
 */
public interface CheckDeleteDeskStrategyAPI {

    /**
     * 校验IDV或者VOI桌面策略能否删除，若存在绑定用户、用户组、终端组则不能删除
     *
     * @param strategyId 云桌面策略id
     * @param strategyName 云桌面策略名称
     * @throws BusinessException 业务异常
     */
    void checkCanDeleteIDVOrVOIDeskStrategy(UUID strategyId, String strategyName) throws BusinessException;

    /**
     * 校验VDI桌面策略能否删除，若存在绑定云桌面、用户组则不能删除
     *
     * @param strategyId 云桌面策略id
     * @param strategyName 云桌面策略名称
     * @throws BusinessException 业务异常
     */
    void checkCanDeleteVDIDeskStrategy(UUID strategyId, String strategyName) throws BusinessException;

    /**
     * 校验第三方桌面策略能否删除，若存在绑定云桌面、用户组则不能删除
     * @param strategyId 云桌面策略id
     * @param strategyName 云桌面策略名称
     * @throws BusinessException 业务异常
     */
    void checkCanDeleteThirdPartyDeskStrategy(UUID strategyId, String strategyName) throws BusinessException;
}
