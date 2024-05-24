package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.DesktopStatisticsDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewDesktopInfoStatEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/6
 *
 * @author Jarman
 */
public interface ViewDesktopInfoStatDAO extends SkyEngineJpaRepository<ViewDesktopInfoStatEntity, UUID> {

    /**
     * 按状态分组查询统计
     * 
     * @param deskType 桌面状态
     * @return 返回统计结果
     */
    @Query("select new com.ruijie.rcos.rcdc.rco.module.impl.dto.DesktopStatisticsDTO(count(deskState)," + "deskState) " + "from "
            + "ViewDesktopInfoStatEntity where deskType=:deskType group by deskState")
    List<DesktopStatisticsDTO> statisticsByDesktopState(@Param("deskType") CbbCloudDeskType deskType);


    /**
     * 根据用户组和终端组按状态分组查询统计
     *
     * @param deskType 桌面状态
     * @param userGroupIdList 用户组列表
     * @param terminalGroupIdList 终端组列表
     * @return 返回统计结果
     */
    @Query("select new com.ruijie.rcos.rcdc.rco.module.impl.dto.DesktopStatisticsDTO(count(deskState), deskState) "
        + "from ViewDesktopInfoStatEntity "
        + "where deskType=:deskType and (userGroupId in (:userGroupId) or terminalGroupId in (:terminalGroupId)) group by deskState")
    List<DesktopStatisticsDTO> statisticsByDesktopStateAndGroupId(@Param("deskType") CbbCloudDeskType deskType,
                                                                  @Param("userGroupId") List<UUID> userGroupIdList,
                                                                  @Param("terminalGroupId") List<UUID> terminalGroupIdList);
}
