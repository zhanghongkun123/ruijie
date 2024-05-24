package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: 用户配置策略变更通知云桌面API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/12
 *
 * @author WuShengQiang
 */
public interface UserProfileStrategyNotifyAPI {
    /**
     * 更新策略时通知所有云桌面
     *
     * @param strategyId 策略ID
     */
    void updateStrategyNotifyDesk(UUID strategyId);

    /**
     * 更新路径时通知所有云桌面
     *
     * @param id 路径ID
     */
    void updatePathNotifyDesk(UUID id);

    /**
     * 云桌面更新策略时通知该云桌面
     *
     * @param desktopId 云桌面ID
     */
    void updateDesktopUserProfileStrategy(UUID desktopId);

    /**
     * 获取与个性化配置关联的云桌面
     *
     * @param userProfilePathIdList 个性化配置
     * @return 关联云桌面集合
     */
    List<UUID> getRelatedDesktopIdByUserProfilePath(List<UUID> userProfilePathIdList);

    /**
     * 获取与个性化配置策略相关联的云桌面
     *
     * @param userProfileStrategyId 个性化配置策略
     * @return 关联云桌面集合
     */
    List<UUID> getRelatedDesktopIdByUserProfileStrategy(UUID userProfileStrategyId);

    /**
     * 个性化配置/策略变更时，向关联云桌面推送变更信息
     *
     * @param deskId 云桌面ID
     * @throws BusinessException 异常处理
     */
    void pushUserProfileUpdateToRunningDesk(UUID deskId) throws BusinessException;
}