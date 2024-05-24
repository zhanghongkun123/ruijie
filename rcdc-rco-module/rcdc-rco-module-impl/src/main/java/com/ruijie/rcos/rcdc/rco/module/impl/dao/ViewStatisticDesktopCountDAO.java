package com.ruijie.rcos.rcdc.rco.module.impl.dao;


import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewStatisticDesktopCountEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

/**
 * Description: 统计所有桌面池中不可用、在线、总的桌面数量 DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/8 10:20
 *
 * @author yxq
 */
public interface ViewStatisticDesktopCountDAO extends SkyEngineJpaRepository<ViewStatisticDesktopCountEntity, UUID> {

    /**
     * 根据桌面池id查询结果
     *
     * @param desktopPoolId 桌面池id
     * @return ViewStatisticDesktopCountEntity
     */
    ViewStatisticDesktopCountEntity findByDesktopPoolId(UUID desktopPoolId);


    /**
     * 获取全部桌面池总量、已使用数量、不可用数量（故障状态的数量）
     *
     * @return ViewStatisticDesktopCountEntity
     */
    @Query("SELECT new com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewStatisticDesktopCountEntity(COALESCE(SUM(entity.totalCount), 0), " +
            "COALESCE(SUM(entity.usedCount), 0), COALESCE(SUM(entity.errorCount), 0)) FROM ViewStatisticDesktopCountEntity entity")
    ViewStatisticDesktopCountEntity obtainTotalInfo();

    /**
     * 根据桌面池类型查询结果
     *
     * @param cbbDesktopPoolType 桌面池类型(VDI、第三方)
     * @return List<ViewStatisticDesktopCountEntity>
     */
    @Query("SELECT new com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewStatisticDesktopCountEntity(COALESCE(SUM(entity.totalCount), 0), " +
            "COALESCE(SUM(entity.usedCount), 0), COALESCE(SUM(entity.errorCount), 0)) FROM ViewStatisticDesktopCountEntity entity " +
            "WHERE entity.desktopSourceType = ?1")
    ViewStatisticDesktopCountEntity statisticByCbbDesktopPoolType(CbbDesktopPoolType cbbDesktopPoolType);

    /**
     * 根据桌面池模式和桌面池类型查询结果
     *
     * @param desktopPoolType 桌面池模式(动态、静态)
     * @param desktopSourceType 桌面池类型(VDI、第三方)
     * @return List<ViewStatisticDesktopCountEntity>
     */
    @Query("SELECT new com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewStatisticDesktopCountEntity(COALESCE(SUM(entity.totalCount), 0), " +
            "COALESCE(SUM(entity.usedCount), 0), COALESCE(SUM(entity.errorCount), 0)) FROM ViewStatisticDesktopCountEntity entity " +
            "WHERE entity.desktopPoolType = ?1 and entity.desktopSourceType = ?2 ")
    ViewStatisticDesktopCountEntity statisticByDesktopPoolTypeAndCbbDesktopPoolType(DesktopPoolType desktopPoolType,
                                                                                    CbbDesktopPoolType desktopSourceType);
}
