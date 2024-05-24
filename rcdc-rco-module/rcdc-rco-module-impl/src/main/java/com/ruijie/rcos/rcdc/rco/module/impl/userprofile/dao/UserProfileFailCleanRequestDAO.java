package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileFailCleanRequestEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description: 保存失败的清理请求表的DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/20
 *
 * @author zwf
 */
public interface UserProfileFailCleanRequestDAO extends SkyEngineJpaRepository<UserProfileFailCleanRequestEntity, UUID> {
    /**
     * 根据云桌面ID查询对应的请求
     *
     * @param deskId 云桌面ID
     * @return 清理路径的请求对象
     */
    List<UserProfileFailCleanRequestEntity> findByDesktopId(UUID deskId);

    /**
     * 根据云桌面ID删除对应的请求
     *
     * @param deskId 云桌面ID
     */
    @Transactional
    void deleteByDesktopId(UUID deskId);

    /**
     * 根据云桌面ID列表删除对应的请求
     *
     * @param desktopIdArr 云桌面列表
     */
    @Transactional
    void deleteByDesktopIdIn(UUID[] desktopIdArr);
}
