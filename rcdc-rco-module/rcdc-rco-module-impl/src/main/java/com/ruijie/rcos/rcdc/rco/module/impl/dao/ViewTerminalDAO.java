package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * 
 * Description: 终端视图DAO
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月21日
 * 
 * @author nt
 */
public interface ViewTerminalDAO extends SkyEngineJpaRepository<ViewTerminalEntity, UUID> {

    /**
     * 根据终端id查询终端
     * 
     * @param terminalId terminal id
     * @return entity
     */
    ViewTerminalEntity findFirstByTerminalId(String terminalId);

    /**
     * 根据终端组类型和分组Id以及终端状态来获取终端列表
     * 
     * @param platform IDV 或者 VDI 或者 APP 或者 PC
     * @param terminalGroupId 终端分组ID
     * @param terminalState OFFLINE - 离线，ONLINE - 在线
     * @return List<ViewTerminalEntity>
     */
    List<ViewTerminalEntity> findViewTerminalEntitiesByPlatformAndTerminalGroupIdAndTerminalState(CbbTerminalPlatformEnums platform,
            UUID terminalGroupId, CbbTerminalStateEnums terminalState);

    /**
     * 根据状态搜索集合
     * 
     * @param terminalState 终端状态
     * @return 返回终端列表
     */
    List<ViewTerminalEntity> findByTerminalState(CbbTerminalStateEnums terminalState);

    /**
     * 通过用户id查询信息
     *
     * @param userId 用户id
     * @return 终端列表
     */
    List<ViewTerminalEntity> findByBindUserId(UUID userId);

    /**
     * 根据云桌面ID和终端类型查询
     *
     * @param bindDeskId 云桌面ID
     * @param platform 终端类型
     * @return ViewTerminalEntity
     */
    ViewTerminalEntity findByBindDeskIdAndPlatform(UUID bindDeskId, CbbTerminalPlatformEnums platform);

    /**
     * 根据云桌面ID查询
     * 
     * @param bindDeskId 云桌面ID
     * @return ViewTerminalEntity
     */
    ViewTerminalEntity findByBindDeskId(UUID bindDeskId);

    /**
     * 根据终端序列号查询终端列表
     *
     * @param sn 终端序列号
     * @return ViewTerminalEntity
     */
    List<ViewTerminalEntity> findBySerialNumber(String sn);

    /**
     * 根据终端组类型和终端状态来获取终端列表
     *
     * @param platform IDV 或者 VDI 或者 APP 或者 PC
     * @param terminalState OFFLINE - 离线，ONLINE - 在线
     * @return List<ViewTerminalEntity>
     */
    List<ViewTerminalEntity> findViewTerminalEntitiesByPlatformAndTerminalState(CbbTerminalPlatformEnums platform,
            CbbTerminalStateEnums terminalState);

    /**
     * 根据镜像ID查询终端列表
     * @param imageId 镜像Id
     * @return ViewTerminalEntity
     */
    List<ViewTerminalEntity> findByImageId(UUID imageId);
}
