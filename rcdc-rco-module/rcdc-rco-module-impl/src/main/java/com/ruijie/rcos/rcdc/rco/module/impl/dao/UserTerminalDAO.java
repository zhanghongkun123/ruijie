package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 * 
 * @author chenzj
 */
public interface UserTerminalDAO extends SkyEngineJpaRepository<UserTerminalEntity, UUID> {

    /**
     * 根据终端id查询终端记录
     * 
     * @param terminalId 终端id
     * @return 终端记录
     */
    UserTerminalEntity findFirstByTerminalId(String terminalId);

    /**
     * 根据绑定的桌面id查询桌面和终端的绑定关系
     * 
     * @param bindDeskId 绑定的桌面id
     * @return 用户和终端的关联数据
     */
    UserTerminalEntity findByBindDeskId(UUID bindDeskId);

    /**
     * 根据绑定用户获取终端数据
     * 
     * @param terminalId 终端id
     * @param bindUserId 绑定的用户id
     * @return UserTerminalEntity entity
     */
    UserTerminalEntity findByTerminalIdAndBindUserId(String terminalId, UUID bindUserId);

    /**
     * 根据绑定用户获取终端数据
     * @param bindUserId 绑定的用户id
     * @return 该用户绑定的所用终端信息
     */
    List<UserTerminalEntity> findByBindUserId(UUID bindUserId);

    /**
     * 根据用户id 获取终端信息
     * @param userId 用户id
     * @return 终端信息列表
     */
    List<UserTerminalEntity> findByUserId(UUID userId);

    /**
     * 更新终端启动模式
     * @param terminalIdList 终端
     * @param bootType 启动模式
     */
    @Transactional
    @Modifying
    @Query(value = "update UserTerminalEntity set bootType = ?1 , version = version + 1  where terminalId in ?2 and version = version ")
    void updateBootType(String bootType, List<String> terminalIdList);

    /**
     * @param userIdList 用户列表
     * @return 用户终端关系
     */
    List<UserTerminalEntity> findByUserIdIn(List<UUID> userIdList);

    /**
     * @param terminalPlatform 终端平台类型
     * @param userIdList 用户id列表
     * @return 终端用户关系列表
     */
    List<UserTerminalEntity> findByTerminalPlatformAndUserIdIn(CbbTerminalPlatformEnums terminalPlatform, List<UUID> userIdList);

    /**
     * @param terminalPlatform 平台
     * @param hasLogin 是否登录
     * @return 终端列表
     */
    List<UserTerminalEntity> findByTerminalPlatformAndHasLogin(CbbTerminalPlatformEnums terminalPlatform, Boolean hasLogin);
}
