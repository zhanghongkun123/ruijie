package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.ViewDeskUserRelationEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Description: 桌面与用户关系视图DAO
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月15日
 *
 * @author wangjie9
 */
public interface ViewDeskUserRelationDAO extends SkyEngineJpaRepository<ViewDeskUserRelationEntity, UUID> {

    /**
     * 根据桌面池ID和用户ID列表查询池中桌面信息
     *
     * @param desktopPoolId 桌面池ID
     * @param userIdList        用户ID列表
     * @return List<ViewPoolDesktopInfoEntity> 云桌面列表
     */
    List<ViewDeskUserRelationEntity> findByDesktopPoolIdAndUserIdIn(UUID desktopPoolId, List<UUID> userIdList);
}
