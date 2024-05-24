package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamAppTestDesktopEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月08日
 *
 * @author zhk
 */
public interface ViewUamAppTestDesktopDAO
        extends SkyEngineJpaRepository<ViewUamAppTestDesktopEntity, UUID>, PageQueryDAO<ViewUamAppTestDesktopEntity> {

    /**
     * 根据桌面id和测试状态查询桌面测试信息
     * 
     * @param deskId 桌面id
     * @param desktopTestStateEnumList 测试状态
     * @return 实体类
     */
    @Query(value = "SELECT t FROM ViewUamAppTestDesktopEntity t WHERE t.deskId = ?1 and t.testState IN (?2)")
    List<ViewUamAppTestDesktopEntity> findByDeskIdAndTestStateIn(UUID deskId, List<DesktopTestStateEnum> desktopTestStateEnumList);

    /**
     * 批量查询桌面测试信息
     * 
     * @param deskIdList 桌面id
     * @param desktopTestStateEnumList 桌面测试状态
     * @return 实体类
     */
    List<ViewUamAppTestDesktopEntity> findAllByDeskIdInAndTestStateIn(List<UUID> deskIdList, List<DesktopTestStateEnum> desktopTestStateEnumList);

    /**
     * 根据桌面测试状态查询所有测试桌面信息
     * 
     * @param desktopTestStateEnumList 测试状态
     * @return 实体类
     */
    List<ViewUamAppTestDesktopEntity> findByTestStateIn(List<DesktopTestStateEnum> desktopTestStateEnumList);

    /**
     * 根据测试id和桌面id查询信息
     * 
     * @param testId 测试id
     * @param deskId 桌面id
     * @return 实体信息
     */
    Optional<ViewUamAppTestDesktopEntity> findByTestIdAndDeskId(UUID testId, UUID deskId);
}
