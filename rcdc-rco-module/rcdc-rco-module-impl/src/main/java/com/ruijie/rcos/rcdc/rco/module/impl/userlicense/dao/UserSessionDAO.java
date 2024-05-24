package com.ruijie.rcos.rcdc.rco.module.impl.userlicense.dao;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.entity.UserSessionEntity;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.TerminalTypeEnum;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description: 用户会话记录持久化接口
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月6日
 *
 * @author lihengjing
 */
public interface UserSessionDAO extends SkyEngineJpaRepository<UserSessionEntity, UUID>, PageQueryDAO<UserSessionEntity> {

    /**
     * 根据用户ID列表进行删除
     * @param userIdList 用户ID列表
     * @return 用户会话删除条数
     */
    @Transactional
    @Modifying
    int deleteByUserIdIn(List<UUID> userIdList);

    /**
     * 根据用户ID查询用户会话列表
     * @param userId 用户ID
     * @return 用户会话列表
     */
    List<UserSessionEntity> findByUserId(UUID userId);

    /**
     * 根据资源获取关联的用户会话列表
     * @param resourceType 资源类型
     * @param resourceId 资源ID
     * @return 用户会话列表
     */
    List<UserSessionEntity> findByResourceTypeAndResourceId(ResourceTypeEnum resourceType, UUID resourceId);

    /**
     * 根据资源获取关联的用户会话列表
     * @param resourceType 资源类型
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @return 用户会话列表
     */
    List<UserSessionEntity> findByResourceTypeAndResourceIdAndUserId(ResourceTypeEnum resourceType, UUID resourceId, UUID userId);

    /**
     * 根据终端获取关联的用户会话列表
     * @param terminalType 终端类型
     * @param terminalId 终端ID
     * @return 用户会话列表
     */
    List<UserSessionEntity> findByTerminalTypeAndTerminalId(TerminalTypeEnum terminalType, String terminalId);

    /**
     * 根据终端获取关联的用户会话列表
     * @param terminalType 终端类型
     * @param terminalId 终端ID
     * @param userId 用户ID
     * @return 用户会话列表
     */
    List<UserSessionEntity> findByTerminalTypeAndTerminalIdAndUserId(TerminalTypeEnum terminalType, String terminalId, UUID userId);

    /**
     * 获取存在会话的去重资源列表
     * @return 资源列表
     */
    @Query(value = "select distinct new com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO(t.resourceId, t.resourceType) "
            + "from UserSessionEntity t")
    List<UserSessionDTO> getDistinctResourceInfoList();

    /**
     * 获取存在会话的去重终端列表
     * @return 终端列表
     */
    @Query(value = "select distinct new com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO(t.terminalId, t.terminalType) "
            + "from UserSessionEntity t")
    List<UserSessionDTO> getDistinctTerminalInfoList();

    /**
     * 更新网页版客户端用户会话终端ID
     * @param oldClusterId 旧集群ID
     * @param terminalType 终端类型
     * @param newClusterId 新集群ID
     */
    @Transactional
    @Modifying
    @Query(value = "update UserSessionEntity set terminalId = ?3, version = version + 1 " +
            "where terminalId = ?1 and terminalType = ?2 and version = version")
    void updateWebClientUserSessionInfo(String oldClusterId, TerminalTypeEnum terminalType, String newClusterId);

    /**
     * 根据会话ID列表进行删除
     * @param sessionIdList 会话ID列表
     * @return 用户会话删除条数
     */
    @Transactional
    @Modifying
    int deleteByIdIn(List<UUID> sessionIdList);
}
